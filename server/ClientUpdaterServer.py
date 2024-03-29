import signal
import sys
from flask import *
import time
import os
import toml
import hashlib
import threading


SERVER = Flask(__name__)
SERVER_PATH = "E:\ClientUpdater\server"

def getFileMD5(path:str) -> str:
    with open(path,"rb") as f:
        bytes = f.read() # read file as bytes
        return hashlib.md5(bytes).hexdigest();


#update
class Update():
    def __init__(self,path,uptime=time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())) -> None:
        self.update_time=uptime
        self.dir=path
        self.update_logs="test"
        self.mod_list=dict()

    def makeModList(self):
        self.mod_list=dict()
        for f in os.listdir(self.dir+"/mods"):
            self.mod_list[getFileMD5(self.dir+"/mods/"+f)]=f;
        for f in os.listdir(self.dir+"/clientmods"):
            self.mod_list[getFileMD5(self.dir+"/clientmods/"+f)]=f;

    def makeJson(self) -> Response:
        return jsonify({"update_time":self.update_time,
                        "update_logs":self.update_logs,
                        "mods_list":list(self.mod_list.keys())})

    def getModPath(self,md5) -> str:
        if os.path.exists(self.dir+"/mods/"+self.mod_list[md5]):
            return self.dir+"/mods/"+self.mod_list[md5]
        elif os.path.exists(self.dir+"/clientmods/"+self.mod_list[md5]):
            return self.dir+"/clientmods/"+self.mod_list[md5]

    def comment(self,msg,file=""):
        if file!="":
            try:
                msg=open(os.path.dirname(os.path.abspath(__file__))+"/"+file,"r",encoding="utf-8")
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
    if os.path.exists(os.path.dirname(os.path.abspath(__file__))+"/"+"data.csv"):
        with open(os.path.dirname(os.path.abspath(__file__))+"/"+"data.csv","r",encoding="utf-8") as f:
            d=f.read().split(",",2)
            UPDATE.update_time=d[0]
            UPDATE.update_logs=d[1]
            UPDATE.mod_list=eval(d[2])
    else:
        UPDATE.update_time=""
        UPDATE.update_logs=""
        UPDATE.mod_list=dict()

def saveData():
    with open(os.path.dirname(os.path.abspath(__file__))+"/"+"data.csv","w",encoding="utf-8") as f:
        d=[]
        d.append(UPDATE.update_time)
        d.append(UPDATE.update_logs)
        d.append(str(UPDATE.mod_list))
        f.write(",".join(d))

#router
@SERVER.route("/api/getupdate")
def getUpdate():
    return UPDATE.makeJson()

@SERVER.route("/api/download/<md5>")
def download(md5):
    return send_file(UPDATE.getModPath(md5), as_attachment=True)

def runAPI(ip="0.0.0.0",port=25564):
    SERVER.run(ip,port)


#main
UPDATE = Update(SERVER_PATH)
if __name__ == "__main__":
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
            for i in UPDATE.mod_list.items():
                print(i)
        else:
            print("unknow command")
            print(com)