package com.xiaojiang.clientupdater;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LogUtils.getLogger();

    public static String getMD5(String path) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(FileUtils.readFileToByteArray(new File(path)));
            byte[] b = md.digest();
            for (int i = 0; i < b.length; i++) {
                int d = b[i];
                if (d < 0) {
                    d = b[i] & 0xff;
                }
                if (d < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(d));
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String downloadByUrl(String urlStr, String savePath) {
        try {
            URL url = new URL(urlStr);

            // 打开连接
            try (InputStream inputStream = url.openStream()) {
                // 获取文件名
                String fileName = getFileName(urlStr);
                String path = "";
                // 创建保存目录
                int index = fileName.lastIndexOf("/");
                if (index > -1) {
                    // LOGGER.info(filepath.substring(0, index));
                    path = savePath + '/' + fileName.substring(0, index);
                    fileName = fileName.substring(index + 1, fileName.length());
                } else {
                    path = savePath;
                }
                File saveDir = new File(path);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }

                // 创建文件路径
                Path filePath = Path.of(path, fileName);

                // 下载文件
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                return fileName;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFileName(String href) {
        try {
            URL url = new URL(href);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect(); // 获取文件名和扩展名
            conn.getResponseCode();
            href = conn.getURL().toString();
            // 获取header 确定文件名和扩展名，并防止乱码
            String filename = "";
            if (conn.getHeaderField("Path") != null) {
                filename = conn.getHeaderField("Path");
                filename = URLDecoder.decode(URLEncoder.encode(filename, "latin1"), "utf-8");
                // LOGGER.info(filename);
                // int index = filename.indexOf("filename*=UTF-8''");
                // if (index > -1) {
                // filename = filename.substring(index + "filename*=UTF-8''".length());
                // filename = URLDecoder.decode(filename, "UTF-8");
                // } else {
                // filename = filename.substring("attachment; filename=".length());
                // filename = URLDecoder.decode(filename, "UTF-8");
                // }
            }
            return filename;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // public static String getFilePath(String href) {
    // try {
    // URL url = new URL(href);
    // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    // conn.connect(); // 获取文件名和扩展名
    // conn.getResponseCode();
    // // 获取header 确定文件名和扩展名，并防止乱码
    // String filepath = "";
    // filepath = conn.getHeaderField("Path");
    // if (conn.getHeaderField("Path") != null) {
    // int index = filepath.lastIndexOf("/");
    // if (index > -1) {
    // // LOGGER.info(filepath.substring(0, index));
    // return filepath.substring(0, index);
    // }
    // }
    // return "";
    // } catch (Exception e) {
    // e.printStackTrace();
    // return "";
    // }
    // }
}
