package com.example.xieyipeng.mineim;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xieyipeng.mineim.javaBean.User;
import com.example.xieyipeng.mineim.tools.GetPostUtil;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private EditText username;
    private EditText password;
    private ProgressBar progressBar;
    private TextView register;
    private static final String host = "http://192.168.137.1:";
    private static final String port = "8000";
    private final String url = host + port + "/user_login/";
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initClick();


    }

    private void initClick() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameString = username.getText().toString().trim();
                final String passwordString = password.getText().toString().trim();
                if (!usernameString.equals("") && !passwordString.equals("")) {

                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String data = "username=" + usernameString + "&password=" + passwordString;
                                String result = GetPostUtil.sendPostRequest(url, data);
                                if (!result.equals("password_error")) {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("user_data", result);
                                    startActivity(intent);
                                    finish();
                                } else if (result.equals("password_error")) {
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "密码验证失败", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }

                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "onClick: " + e.getMessage());
                    } finally {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "输入格式不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        progressBar = findViewById(R.id.login_progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

    }
}
