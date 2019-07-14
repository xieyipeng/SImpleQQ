package com.example.xieyipeng.sokettest.javaBean;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class GetPostUtil {

    private static String TAG = "GetPostUtil";

    /**
     * 发送post请求
     *
     * @param url  发送请求的 URL
     * @param data 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return json数据包
     */
    public static String sendPostRequest(String url, String data) {
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
            printWriter.print(data);
            printWriter.flush();
            Log.e(TAG, "sendPostRequest: Post Request Successful");

            // TODO: 定义BufferedReader输入流来读取URL的响应
            result = new StringBuilder();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            Log.e(TAG, "sendPostRequest: " + e.getMessage());
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
     * @param url eg: http://10.0.2.2:8000/get_test/
     * @return 返回服务器的响应
     */
    public static String sendGetRequest(String url) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        StringBuilder result = null;
        try {
            URL realUrl = new URL(url);
            //打开链接
            connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestMethod("GET");
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                //若链接正常（ResponseCode == 200）
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

        } catch (IOException e) {
            Log.e(TAG, "sendGetRequest: error: " + e.getMessage());
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


    /**
     * 发送post请求上传图片
     *
     * @param actionUrl url
     * @param inputStream 图片的流
     * @param fileName name
     * @return 服务器响应
     */
    public static String upLoadFiles(String actionUrl, InputStream inputStream, String fileName) {
        StringBuffer result = new StringBuffer();
        OutputStream outputStream = null;
        DataInputStream dataInputStream = null;
        try {
            final String newLine = "\r\n"; // 换行符
            final String boundaryPrefix = "--"; //边界前缀
            final String boundary = String.format("=========%s", System.currentTimeMillis()); // 定义数据分隔线
            // 连接
            URL url = new URL(actionUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            // 设置请求头参数
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Charsert", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            // 获取输出流
            outputStream = new DataOutputStream(connection.getOutputStream());
            // 文件参数
            // 参数头设置完以后需要两个换行，然后才是参数内容
            String stringBuilder = boundaryPrefix +
                    boundary +
                    newLine +
                    "Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"" + newLine +
                    "Content-Type:application/octet-stream" +
                    newLine +
                    newLine;
            // 将参数头的数据写入到输出流中
            outputStream.write(stringBuilder.getBytes());
            // 数据输入流,用于读取文件数据
            dataInputStream = new DataInputStream(inputStream);
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = dataInputStream.read(bufferOut)) != -1) {
                outputStream.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            outputStream.write(newLine.getBytes());
            //关闭流
            inputStream.close();
            dataInputStream.close();
            // 定义最后数据分隔线，即--加上boundary再加上--。
            byte[] end_data = (newLine + boundaryPrefix + boundary + boundaryPrefix + newLine).getBytes();
            // 写上结尾标识
            outputStream.write(end_data);
            outputStream.flush();
            outputStream.close();
            // 定义BufferedReader输入流来读取服务器的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            Log.e(TAG, "upLoadFiles: 响应： " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "uploadFiles: " + e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "upLoadFiles: " + e.getMessage());
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "upLoadFiles: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "upLoadFiles: " + e.getMessage());
                }
            }
        }
        return result.toString();
    }
}

