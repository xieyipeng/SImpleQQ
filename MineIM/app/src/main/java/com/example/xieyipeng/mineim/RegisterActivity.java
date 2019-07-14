package com.example.xieyipeng.mineim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xieyipeng.mineim.javaBean.User;
import com.example.xieyipeng.mineim.tools.GetPostUtil;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button commit;
    private static final String host = "http://192.168.137.1:";
    private static final String port = "8000";
    private final String url = host + port + "/register/";
    private static final String TAG = "RegisterActivity";
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initClick();
    }

    private void initClick() {
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameString = username.getText().toString().trim();
                final String passwordString = password.getText().toString().trim();
                if (!usernameString.equals("") && !passwordString.equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String data = "username=" + usernameString + "&password=" + passwordString;
                                String result = GetPostUtil.sendPostRequest(url, data);
                                if (result.equals("success")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                } else {
                                    Log.e(TAG, "run: 注册失败，服务端：" + result);
                                }
                            }
                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "系统开小差了...请稍后", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onClick: " + e.getMessage());
                    } finally {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "输入格式不正确", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initView() {
        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        commit = findViewById(R.id.register_commit);
        progressBar = findViewById(R.id.register_process_bar);
        progressBar.setVisibility(View.INVISIBLE);
    }
}
