package com.xiaojiang.clientupdater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.Gson;
import com.mojang.logging.LogUtils;

import java.util.List;

import org.slf4j.Logger;

public class Update {
    public String update_time;
    public String update_logs;
    public List<String> mods_list;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static Update loadJsonFromURL(String url) {
        // 字符串工具类
        StringBuilder json = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/4.76");
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "utf-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info(json.toString());
        return Update.complexJsonToObj(json.toString(), Update.class);
    }

    public static <T> T complexJsonToObj(String json, Class<T> cls) {
        Gson gson = new Gson();
        if (json == null)
            return null;
        T obj = gson.fromJson(json, cls);
        if (obj == null) {
            return null;
        } else {
            return obj;
        }
    }
}