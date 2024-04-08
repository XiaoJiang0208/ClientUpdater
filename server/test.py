import os
dirs=[]
for f in os.listdir("./src"):
    if os.path.isdir("./src/"+f):
        dirs.append("./src/"+f)
    else:
        print("./src/"+f)
for d in dirs:
    for f in os.listdir(d):
        if os.path.isdir(d+"/"+f):
            dirs.append(d+"/"+f)
        else:
            print(d+"/"+f)