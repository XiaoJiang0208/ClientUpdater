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
        Map<String, String> mods_list = new HashMap<String, String>();
        File mods_dir = new File("./mods");
        File mods[] = mods_dir.listFiles();
        if (mods != null) {
            for (File file : mods) {
                if (file.isFile()) {
                    mods_list.put(Tools.getMD5(file.getPath()), file.getName());
                }
            }
        }
        for (String key : update.mods_list)
            LOGGER.info(key);
        for (String key : update.mods_list) {
            if (mods_list.get(key) == null) {
                Tools.downloadByUrl(server_url + "api/download/" + key, "./mods");
            }
        }
        for (String key : mods_list.keySet()) {
            if (update.mods_list.indexOf(key) == -1) {
                File mod = new File("./mods/" + mods_list.get(key));
                if (!mod.delete())
                    LOGGER.error("Can't delete the mod!");
            }
        }
        isComplete = true;
    }
}
