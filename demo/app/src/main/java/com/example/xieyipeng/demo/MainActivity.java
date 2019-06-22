package com.example.xieyipeng.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xieyipeng.demo.bean.GetPostUtil;


public class MainActivity extends AppCompatActivity {

    private Button getRequest;
    private Button postRequest;
    private TextView getTV;
    private TextView postTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initClick();
    }

    private void initClick() {
        getRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  TODO: 子线程中访问url
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String get = GetPostUtil.sendGetRequest("http://10.0.2.2:8000/get_test/", "");

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
                        final String get = GetPostUtil.sendPostRequest("http://10.0.2.2:8000/post_test/", "v1=v&v2=v");
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
        getRequest = findViewById(R.id.send_get_request);
        getTV = findViewById(R.id.get_text);
        postRequest = findViewById(R.id.send_post_request);
        postTV = findViewById(R.id.post_text);
    }
}
