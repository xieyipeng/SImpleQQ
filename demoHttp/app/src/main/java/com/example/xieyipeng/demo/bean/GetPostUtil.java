package com.example.xieyipeng.demo.bean;

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
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;


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

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "sendGetRequest: error: " + e.getMessage() + " " + e.toString());
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
     * @param actionUrl   url
     * @param inputStream 图片的流
     * @param fileName    name
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

            String string_text = boundaryPrefix +
                    boundary +
                    newLine +
                    "Content-Disposition: form-data;name=\"v1\"" +
                    newLine +
                    newLine +
                    "v" +
                    newLine;


            outputStream.write(string_text.getBytes());





            String stringBuilder = boundaryPrefix +
                    boundary +
                    newLine +
                    "Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\""
                    + newLine +
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
            Log.e(TAG, "upLoadFiles: 1" );

            outputStream.flush();
            outputStream.close();
            // 定义BufferedReader输入流来读取服务器的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.e(TAG, "upLoadFiles: "+line );
                result.append(line);
            }
            Log.e(TAG, "upLoadFiles: 响应： " + result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "uploadFiles: " + e.getMessage()+" "+e.toString());
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

    /**
     *      * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     *      *   <FORM METHOD=POST ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet" enctype="multipart/form-data">
     *             <INPUT TYPE="text" NAME="name">
     *             <INPUT TYPE="text" NAME="id">
     *             <input type="file" name="imagefile"/>
     *             <input type="file" name="zip"/>
     *          </FORM>
     *      * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
     *      * @param params 请求参数 key为参数名,value为参数值
     *      * @param file 上传文件
     *      
     */


    //TODO: https://blog.csdn.net/fcaiqing/article/details/46390383
//    public static String post(String path, Map<String, String> params, FormFile[] files) throws Exception {
//        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
//        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志
//
//        int fileDataLength = 0;
//        if (files != null || files.length > 0) {
//            for (FormFile uploadFile : files) {//得到文件类型数据的总长度
//                StringBuilder fileExplain = new StringBuilder();
//                fileExplain.append("--");
//                fileExplain.append(BOUNDARY);
//                fileExplain.append("\r\n");
//                fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
//                fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
//                fileExplain.append("\r\n");
//                fileDataLength += fileExplain.length();
//
//                if (uploadFile.getInStream() != null) {
//                    fileDataLength += uploadFile.getFile().length();
//                } else {
//                    fileDataLength += uploadFile.getData().length;
//                }
//            }
//        }
//        StringBuilder textEntity = new StringBuilder();
//        for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
//            textEntity.append("--");
//            textEntity.append(BOUNDARY);
//            textEntity.append("\r\n");
//            textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
//            textEntity.append(entry.getValue());
//            textEntity.append("\r\n");
//        }
//        //计算传输给服务器的实体数据总长度
//        int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;
//        URL url = new URL(path);
//        int port = url.getPort() == -1 ? 80 : url.getPort();
//        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
//        OutputStream outStream = socket.getOutputStream();
//        //下面完成HTTP请求头的发送
//        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
//        outStream.write(requestmethod.getBytes());
//        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
//        outStream.write(accept.getBytes());
//        String language = "Accept-Language: zh-CN\r\n";
//        outStream.write(language.getBytes());
//        String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
//        outStream.write(contenttype.getBytes());
//        String contentlength = "Content-Length: " + dataLength + "\r\n";
//        outStream.write(contentlength.getBytes());
//        String alive = "Connection: Keep-Alive\r\n";
//        outStream.write(alive.getBytes());
//        String host = "Host: " + url.getHost() + ":" + port + "\r\n";
//        outStream.write(host.getBytes());
//        //写完HTTP请求头后根据HTTP协议再写一个回车换行
//        outStream.write("\r\n".getBytes());
//        //把所有文本类型的实体数据发送出来
//        outStream.write(textEntity.toString().getBytes());
//        //把所有文件类型的实体数据发送出来
//        for (FormFile uploadFile : files) {
//            StringBuilder fileEntity = new StringBuilder();
//            fileEntity.append("--");
//            fileEntity.append(BOUNDARY);
//            fileEntity.append("\r\n");
//            fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
//            fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
//            outStream.write(fileEntity.toString().getBytes());
//            if (uploadFile.getInStream() != null) {
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
//                    outStream.write(buffer, 0, len);
//                }
//                uploadFile.getInStream().close();
//            } else {
//                outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
//            }
//            outStream.write("\r\n".getBytes());
//        }
//        //下面发送数据结束标志，表示数据已经结束
//        outStream.write(endline.getBytes());
//
//    }
}

