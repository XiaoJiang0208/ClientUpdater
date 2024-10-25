<h1 align="center">Minecraft Client Updater</h1>

[中文](#chinese)

## Highlights
1. Used to update mods from a specific Minecraft server and display update logs.
2. Provides players with an unobtrusive update experience that is more closely aligned with the original version.
3. Provides a web page to view the history of update logs.


## Demo
Senseless updates close to the original
Automatically displays the update when it's available at startup.
![up](https://cdn.modrinth.com/data/cached_images/7493ec6b9e9c4bd215a1a3c1e7ebf348d6601d09.png)
![up](https://cdn.modrinth.com/data/cached_images/e4c17b978a533f9620734a701ea50d666b44c647.png)
Webpage to view history of update logs
![updater](https://cdn.modrinth.com/data/cached_images/0719af247d6ab6ca030c1508c54ffa800f717ca2.png)


## Usage

### Client
1. Place the mod in the mod folder and start the game once to generate a config file in `game changer/clientupdater-client.toml`.
2. Edit the configuration file
```
# Synchronize the server url
server_address = “http://server‘s ip or hostname:25564/”
#last_update_time (automatically generated, do not change)
last_update_time = “”
```
3. Start the game (if there are any updates or mods missing, it will show a new line page)

### Server
`The server side of this mod is written in python, so there is no need to put clientupdater-x.x.x.jar into the server side mod folder`.
1. Install the python runtime environment, and install the `Flask` and `Flask_Cors` libraries.
```
    pip install Flask
    pip install Flask_Cors
```
2. Unzip the server `server.zip` into the server root directory
3. Open port 25564, or modify the PORT in `ClientUpdaterServer.py`
4. create the folders `clientmods` and `clientconfig` in the server root directory
5. Put the `Clientupdater-x.x.x.jar` mod into clientmods
    ```
    Game change directory
    |-mods(use mirror mode)
    |-clientmods(client side mods only, use mirror mode)
    |    |-Clientupdater-x.x.x.jar(must put this mod in)
    |
    |-clientconfig(client config file, same structure as client, use override mode)
    |
    |
    ```
6. Run the script `ClientUpdaterServer.py`.~~~~
7. Type `commit single line update description` or use `commit -f text file with update information.txt` to submit the update, after submitting the change line the client will receive the update push when restarting.
8. Use the above command to update the mod list after each mod change.
9.  Use `status` to view the list of mods that have been submitted.

### Server-side commands
| Commands | Usage |
|-----|------|
| commit | `commit <single line update log> `Commit single line log only `commit -t <multi-line update log in .txt> `Commit multi-line log | status | View the current mod list.
| status | You can check the status of the current update |

Translated with DeepL.com (free version)


# chinese
## 亮点
1. 用于从指定的我的世界服务器上更新mod和显示更新日志
2. 为玩家带来更贴经原版的无感更新体验
3. 提供了网页可供查看历史更新日志


## 效果演示
贴近原版风格的无感更新
启动时如获取到更新自动显示
![updater](https://cdn.modrinth.com/data/cached_images/2af89e9c518d3ad9341fad38e8b287c660058597.png)
![updater](https://cdn.modrinth.com/data/cached_images/8a38c3b20d2f916dd37e07b546f7b0a78fa367f8.png)
网页查看历史更新日志
![updater](https://cdn.modrinth.com/data/cached_images/0719af247d6ab6ca030c1508c54ffa800f717ca2.png)


## 使用方法
### 客户端
1. 将mod放入mod文件夹，启动一次游戏生成配置文件，在`游戏更目录/clientupdater-client.toml`
2. 编辑配置文件
```
#同步服务器url
server_address = "http://服务器的ip或者域名:25564/"
#最后更新时间(自动生成请勿更改)
last_update_time = ""
```
3. 启动游戏(如果有更新或mod有缺失都会显示更行页面)

### 服务端
`此mod的服务端使用python编写，因此不需要将clientupdater-x.x.x.jar放进服务端的mod文件夹`
1. 安装python运行环境，并安装`Flask`和`Flask_Cors`库
```
    pip install Flask
    pip install Flask_Cors
```
2. 解压服务端`server.zip`到服务端根目录
3. 开放25564端口，或者修改`ClientUpdaterServer.py`中的PORT
4. 在服务器根目录下创建文件夹`clientmods`和`clientconfig`
5. 将`Clientupdater-x.x.x.jar`mod放入clientmods
    ```
    游戏更目录
    |-mods(使用镜像模式)
    |-clientmods(仅客户端侧mod，使用镜像模式)
    |    |-Clientupdater-x.x.x.jar(必须将此mod放入)
    |
    |-clientconfig(客户端配置文件，文件结构同客户端，使用覆盖模式)
    |
    ```
6. 运行脚本`ClientUpdaterServer.py`
7. 输入`commit 单行更新说明`或者使用`commit -f 写有更新信息的文本文件.txt`来提交更新，提交完更行后客户端重启时就会收到更新推送
8. 每次改变mod后用上方命令即可更新mod列表
9. 使用`status`可以查看已提交的mod列表

### 服务端指令
| 指令 | 用法 |
|-----|------|
| commit | `commit <单行的更新日志>`仅可提交单行日志  `commit -t <多行更新日志所在.txt>`可提交多行日志 |
| status | 可查看当前的更新的内容状态 |
