from flask import *
import time

server = Flask(__name__)


#update
class Update():
    def __init__(self) -> None:
        self.update_time=time.time()
        self.dir="../mod"


#router
@server.route("/getupdate")
def getUpdate():
    return "cool"

#router
if __name__ == "__main__":
    server.run("0.0.0.0",25564)