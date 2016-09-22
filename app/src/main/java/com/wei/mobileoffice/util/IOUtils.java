package com.wei.mobileoffice.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lx on 15/12/27.
 */
public class IOUtils {
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//网页的二进制数据
        outStream.close();
        inStream.close();
        return data;
    }

    public static int getFileSize(String updateUrl) {
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        try {
            URL url = new URL(updateUrl); //URL对象
            conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("POST"); //使用get请求
            conn.setConnectTimeout(10000);//连接超时 单位毫秒
            conn.setReadTimeout(10000);//读取超时 单位毫秒

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return conn.getContentLength();
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
