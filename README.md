<h1 align="center">Minecraft Client Updater</h1>


## 亮点
1. 用于从指定的我的世界服务器上更新mod和显示更新日志
2. 为玩家带来更贴经原版的无感更新体验
3. 提供了网页可供查看历史更新日志


## 效果演示
贴近原版风格的无感更新
启动时如获取到更新自动显示
![更新日志](./img/2024-09-27%20233409.png)
![更新日志](./img/2024-09-27%20233454.png)
网页查看历史更新日志
![更新日志](./img/2024-09-27%20233426.png)


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
1. 安装python运行环境，并安装`flask`和`toml`库
2. 解压服务端`server.zip`到服务端根目录
3. 在服务器根目录下创建文件夹`clientmods`和`clientconfig`
4. 将`Clientupdater-x.x.x.jar`mod放入clientmods
    ```
    游戏更目录
    |-mods(使用镜像模式)
    |-clientmods(仅客户端侧mod，使用镜像模式)
    |    |-Clientupdater-x.x.x.jar(必须将此mod放入)
    |
    |-clientconfig(客户端配置文件，文件结构同客户端，使用覆盖模式)
    |
    ```
5. 运行脚本`ClientUpdaterServer.py`
6. 输入`commit 单行更新说明`或者使用`commit -f 写有更新信息的文本文件.txt`来提交更新，提交完更行后客户端重启时就会收到更新推送
7. 每次改变mod后用上方命令即可更新mod列表
8. 使用`status`可以查看已提交的mod列表

### 服务端指令
| 指令 | 用法 |
|-----|------|
| commit | `commit <单行的更新日志>`仅可提交单行日志  `commit -t <多行更新日志所在.txt>`可提交多行日志 |
| status | 可查看当前的更新的内容状态 |