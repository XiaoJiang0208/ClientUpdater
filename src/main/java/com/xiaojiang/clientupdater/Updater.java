package com.xiaojiang.clientupdater;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

public class Updater extends Thread {
    private static final Logger LOGGER = LogUtils.getLogger();
    public Boolean isComplete = false;
    public String server_url = "";
    public Update update = new Update();

    public void run() {
        Map<String, String> file_list = new HashMap<String, String>();
        File file_dir = new File("./mods");
        File files[] = file_dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file_list.put(Tools.getMD5(file.getPath()), file.getName());
                }
            }
        }
        for (String key : file_list.keySet()) {// 删除mod
            if (update.mods_list.indexOf(key) == -1) {
                File mod = new File("./mods/" + file_list.get(key));
                if (!mod.delete())
                    LOGGER.error("Can't delete the mod!");
            }
        }
        for (String key : update.mods_list) {// 下载mod
            if (file_list.get(key) == null) {
                LOGGER.info("download: " + key);
                Tools.downloadByUrl(server_url + "api/download/" + key, "./mods");
            }
        }
        file_dir = new File("./config");
        files = file_dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    file_list.put(Tools.getMD5(file.getPath()), file.getName());
                }
            }
        }
        for (String key : update.config_list) {// 下载config
            if (file_list.get(key) == null) {
                Tools.downloadByUrl(server_url + "api/download/" + key,
                        "./config");
            }
        }
        isComplete = true;
    }
}
