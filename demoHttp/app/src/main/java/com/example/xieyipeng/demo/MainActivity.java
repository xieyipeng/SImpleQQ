package com.example.xieyipeng.demo;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xieyipeng.demo.bean.GetPostUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static Resources resources;
    private Button getRequest;
    private Button postRequest;
    private Button postFileRequest;
    private Button getFileRequest;
    private TextView getTV;
    private TextView postTV;
    private TextView postFileTV;
    private ImageView imageView;
    private Context context;

    private List list = new ArrayList();


    private static final String TAG = "TEST_FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initClick();
    }

    private void initClick() {

        getFileRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.0.2.2:81/微信图片_20190609175306.jpg";
                Glide.with(context)
                        .load(url)
                        .into(imageView);
            }
        });


        postFileRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String[] fileNames = new String[1];
                        String fileName = "IMG.JPG";
                        fileNames[0] = fileName;
                        // 获取工程目录下Assets目录里面的只读资源文件
                        // 也可获取相机里的文件流，同理
                        AssetManager assetManager = MainActivity.resources.getAssets();
                        try {
                            InputStream[] inputStreams = new InputStream[1];
                            InputStream inputStream = assetManager.open(fileName);
                            inputStreams[0] = inputStream;
                            Map<String, String> map = new HashMap<>();
                            map.put("name", "xieyipeng");
                            map.put("sex", "male");
                            map.put("id", "1607094128");
                            final String get = GetPostUtil.upLoadFiles("http://10.0.2.2:8000/post_file_test/", map, inputStreams, fileNames);
                            /*
                             TODO: 子线程中更新UI
                             */
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, "run: " + e.getMessage());
                        }
                    }
                }).start();
            }
        });


        getRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  TODO: 子线程中访问网络
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String host2 = "192.168.137.1";
                        String url = "http://" + host2 + ":8000/get_test/";
                        Log.e(TAG, "run: " + url);
                        final String get = GetPostUtil.sendGetRequest(url);

                        /*
                          TODO: 子线程中更新UI
                         */
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getTV.setText(get);
                            }
                        });
                    }
                }).start();
            }
        });

        postRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://10.0.2.2:8000/post_test/";
                        String data = "v1=v&v2=v";
                        final String get = GetPostUtil.sendPostRequest(url, data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                postTV.setText(get);
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private void initViews() {
        context = this;
        resources = this.getResources();
        getRequest = findViewById(R.id.send_get_request);
        getTV = findViewById(R.id.get_text);
        postRequest = findViewById(R.id.send_post_request);
        postTV = findViewById(R.id.post_text);
        postFileRequest = findViewById(R.id.send_post_file_request);
        postFileTV = findViewById(R.id.post_file_text);
        getFileRequest = findViewById(R.id.send_get_file_request);
        imageView = findViewById(R.id.get_file_img);
    }
}
