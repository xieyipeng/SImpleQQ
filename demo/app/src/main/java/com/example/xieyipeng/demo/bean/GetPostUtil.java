package com.example.xieyipeng.demo.bean;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class GetPostUtil {

    private static String TAG = "GetPostUtil";

    /**
     * 发送post请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return json数据包
     */
    public static String sendPostRequest(String url, String param) {
        PrintWriter printWriter = null;
        StringBuilder result = null;
        BufferedReader bufferedReader = null;
        try {
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();

            // TODO: 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            // TODO: 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // TODO: 获取URLConnection对象对应的输出流
            printWriter = new PrintWriter(connection.getOutputStream());

            // TODO: 发送请求参数
            // TODO: flush输出流的缓冲
            printWriter.print(param);
            printWriter.flush();

            // TODO: 定义BufferedReader输入流来读取URL的响应
            result = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    /**
     * 发送get请求
     *
     * @param url   http://localhost:6144/Home/RequestString/
     * @param param key=123 & v=456
     * @return json数据包
     */
    public static String sendGetRequest(String url, String param) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuilder result = null;
        try {
            String urlString = url + "?" + param;
            URL realUrl = new URL(urlString);
            connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.setRequestMethod("GET");
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                Log.e(TAG, "sendGetRequest: Get Request Successful");
                InputStream inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                result = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            } else {
                Log.e(TAG, "sendGetRequest: Get Request Failed");
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "sendGetRequest: MU " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "sendGetRequest: IO " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

}
