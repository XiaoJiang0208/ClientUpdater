from flask import *
import time
import os
import toml
import hashlib


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
        print(self.dir)
        for f in os.listdir(self.dir+"/mods"):
            self.mod_list[getFileMD5(self.dir+"/mods/"+f)]=f;
            print(self.mod_list)

    def makeJson(self) -> Response:
        return jsonify({"update_time":self.update_time,
                        "update_logs":self.update_logs,
                        "mods_list":list(self.mod_list.keys())})

    def getModPath(self,md5) -> str:
        return self.dir+"/mods/"+self.mod_list[md5]





#router
@SERVER.route("/api/getupdate")
def getUpdate():
    return UPDATE.makeJson()

@SERVER.route("/api/download/<md5>")
def download(md5):
    return send_file(UPDATE.getModPath(md5), as_attachment=True)


#main
UPDATE = Update(SERVER_PATH)
if __name__ == "__main__":
    UPDATE.makeModList()
    SERVER.run("0.0.0.0",25564)