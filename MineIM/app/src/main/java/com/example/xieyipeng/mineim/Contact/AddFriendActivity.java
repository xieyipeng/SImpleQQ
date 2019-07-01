package com.example.xieyipeng.mineim.Contact;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.tools.GetPostUtil;

public class AddFriendActivity extends AppCompatActivity {

    private String otherName;
    private TextView nameTv;
    private final String ADD_FRIEND = MainActivity.host + MainActivity.port + "/add_friend/";
    private static final String TAG = "AddFriendActivity";


    private Button add;
    private Button send;
    private ImageView headImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        getTheIntent();
        initView();
        initHead();
        initClick();

    }

    private void initClick() {

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String data = "my_name=" + MainActivity.userName + "&friend_name=" + otherName;
                            Log.e(TAG, "run: " + data);
                            String res = GetPostUtil.sendPostRequest(ADD_FRIEND, data);
                            switch (res) {
                                case "success":
                                    Looper.prepare();
                                    Toast.makeText(AddFriendActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Looper.loop();
                                    break;
                                case "have add":
                                    Looper.prepare();
                                    Toast.makeText(AddFriendActivity.this, "已经添加", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Looper.loop();
                                    break;
                                default:
                                    Looper.prepare();
                                    Toast.makeText(AddFriendActivity.this, "服务器繁忙", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "run: " + e.getMessage());
                        }
                    }
                }).start();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddFriendActivity.this, "发送消息", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        MainActivity.setToolBarColor(this);
        nameTv = findViewById(R.id.add_friend_name);
        nameTv.setText(otherName);
        add = findViewById(R.id.add_friend_add_button);
        send = findViewById(R.id.add_friend_send_button);
        headImage = findViewById(R.id.add_friend_head_image);
    }

    private void getTheIntent() {
        Intent intent = getIntent();
        otherName = intent.getStringExtra("name");
    }

    private void initHead() {
        String headUrl = MainActivity.host + MainActivity.nginx_port + "/default/headImg.jpg";
        Glide.with(MainActivity.context)
                .load(headUrl)
                .into(headImage);
    }
}
