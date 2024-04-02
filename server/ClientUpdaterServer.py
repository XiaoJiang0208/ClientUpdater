import signal
import sys
from flask import *
from flask_cors import CORS
import time
import os
import toml
import hashlib
import threading
import sqlite3


SERVER_PATH = "E:\ClientUpdater\server"


def getFileMD5(path:str) -> str:
    with open(path,"rb") as f:
        bytes = f.read() # read file as bytes
        return hashlib.md5(bytes).hexdigest();

def getFileDir():
    return os.path.dirname(os.path.abspath(__file__)).replace("\\","/")

#update
class Update():
    def __init__(self,path) -> None:
        self.update_time="0"
        self.dir=path
        self.update_logs="test"
        self.mods_list=dict()
        self.config_list=dict()

    def makeModList(self):
        self.mods_list=dict()
        for f in os.listdir(self.dir+"/mods"):
            self.mods_list[getFileMD5(self.dir+"/mods/"+f)]=f;
        if os.path.exists(self.dir+"/clientmods"):
            for f in os.listdir(self.dir+"/clientmods"):
                self.mods_list[getFileMD5(self.dir+"/clientmods/"+f)]=f;
        if os.path.exists(self.dir+"/clientconfig"):
            for f in os.listdir(self.dir+"/clientconfig"):
                self.config_list[getFileMD5(self.dir+"/clientconfig/"+f)]=f;

    def makeJson(self) -> Response:
        return jsonify({"update_time":self.update_time,
                        "update_logs":self.update_logs,
                        "mods_list":list(self.mods_list.keys()),
                        "config_list":list(self.config_list.keys())})

    def getModPath(self,md5) -> str:
        if self.mods_list.get(md5,False):
            if os.path.exists(self.dir+"/mods/"+self.mods_list[md5]):
                return self.dir+"/mods/"+self.mods_list[md5]
            elif os.path.exists(self.dir+"/clientmods/"+self.mods_list[md5]):
                return self.dir+"/clientmods/"+self.mods_list[md5]
        else:
            if os.path.exists(self.dir+"/clientconfig/"+self.config_list[md5]):
                return self.dir+"/clientconfig/"+self.config_list[md5]
        return None

    def comment(self,msg,file=""):
        if file!="":
            try:
                msg=open(getFileDir()+"/"+file,"r",encoding="utf-8")
                self.update_logs=msg.read()
                print("已提交更新\n"+self.update_logs)
                msg.close()
            except:
                print("读取文件失败")
        else:
            self.update_logs=msg
            print("已提交更新\n"+msg)
        self.update_time=time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
        self.makeModList()
        saveData()


def readData():
    CUR.execute("""SELECT * FROM update_list
        ORDER BY update_time DESC""")
    data=CUR.fetchone()
    if data:
        UPDATE.update_time=data[0]
        UPDATE.update_logs=data[1]
        UPDATE.mods_list=eval(data[2])
        UPDATE.config_list=eval(data[3])
    else:
        UPDATE.update_time=""
        UPDATE.update_logs=""
        UPDATE.mods_list=dict()
        UPDATE.config_list=dict()
    '''if os.path.exists(getFileDir()+"/"+"data.csv"):
        with open(getFileDir()+"/"+"data.csv","r",encoding="utf-8") as f:
            d=f.read().split(",",2)
            UPDATE.update_time=d[0]
            UPDATE.update_logs=d[1]
            UPDATE.mods_list=eval(d[2])
    else:
        UPDATE.update_time=""
        UPDATE.update_logs=""
        UPDATE.mods_list=dict()'''

def saveData():
    CUR.execute("INSERT INTO update_list VALUES (?,?,?,?)",(UPDATE.update_time,UPDATE.update_logs,str(UPDATE.mods_list),str(UPDATE.config_list)))
    CONN.commit()
    '''CUR.execute("SELECT * FROM update_list")
    print(CUR.fetchall())
    with open(getFileDir()+"/"+"data.csv","w",encoding="utf-8") as f:
        d=[]
        d.append(UPDATE.update_time)
        d.append(UPDATE.update_logs)
        d.append(str(UPDATE.mods_list))
        f.write(",".join(d))'''

def initDatabase():
    try:
        CUR.execute("""SELECT * FROM update_list;""")
    except:
        CUR.execute("""CREATE TABLE update_list
            (update_time TEXT,
            update_logs TEXT,
            mods_list TEXT,
            config_list);""")
        CONN.commit()
        print("creat database")


SERVER = Flask(__name__,
            template_folder=getFileDir()+"/web",
            static_folder=getFileDir()+"/web/assets")
CORS(SERVER, resources=r'/*')

CONN = sqlite3.connect(getFileDir()+"/data.db")
CUR = CONN.cursor()


#router
@SERVER.route("/api/getupdate")
def getUpdate():
    return UPDATE.makeJson()

@SERVER.route("/api/getrangeupdate/<rg>")
def getRangeUpdate(rg):
    conn = sqlite3.connect(getFileDir()+"/"+"data.db")
    cur = conn.cursor()
    cur.execute("""SELECT COUNT(*) FROM update_list;""")
    size=cur.fetchone()[0]
    cur.execute("""SELECT * FROM update_list
        ORDER BY update_time DESC""")
    try:
        rg=int(rg)
    except:
        rg=0
    if rg < 0:
        rg=0
    if rg >= size:
        rg=size-1
    for _ in range(rg+1):
        data=cur.fetchone()
    update = Update(SERVER_PATH)
    update.update_time=data[0]
    update.update_logs=data[1]
    update.mods_list=eval(data[2])
    update.config_list=eval(data[3])
    return update.makeJson()

@SERVER.route("/api/download/<md5>")
def download(md5):
    return send_file(UPDATE.getModPath(md5), as_attachment=True)

@SERVER.route("/")
def root():
    return render_template("index.html")

def runAPI(ip="0.0.0.0",port=25564):
    SERVER.run(ip,port)


#main
UPDATE = Update(SERVER_PATH)
if __name__ == "__main__":
    initDatabase()
    readData()
    api = threading.Thread(target= runAPI, args=("0.0.0.0",25564))
    api.start()
    while True:
        com = input("").split(maxsplit=1)
        if len(com) < 1:
            com.append("")
        if com[0] == "stop":
            os.kill(os.getpid(),signal.SIGTERM)
        elif com[0] == "comment":
            if len(com)==1:
                print("请提供更新日志")
            else:
                if com[1].split()[0]=="-f":
                    UPDATE.comment("",com[1].split()[1])
                else:
                    UPDATE.comment(com[1])
        elif com[0] == "status":
            print("当前状态")
            print(UPDATE.update_time)
            print(UPDATE.update_logs)
            for i in UPDATE.mods_list.items():
                print(i)
            for i in UPDATE.config_list.items():
                print(i)
        else:
            print("unknow command")
            print(com)