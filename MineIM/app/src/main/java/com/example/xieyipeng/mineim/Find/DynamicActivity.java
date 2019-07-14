package com.example.xieyipeng.mineim.Find;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.xieyipeng.mineim.Contact.ContactUserAdapter;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.DynamicReceive;
import com.example.xieyipeng.mineim.javaBean.UserFriend;
import com.example.xieyipeng.mineim.tools.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DynamicActivity extends AppCompatActivity {

    private static final String TAG = "DynamicActivity";
    public static DynamicAdapter userAdapter;
    private RecyclerView recyclerView;
    private ImageView add;
    private SwipeRefreshLayout refreshLayout;
    private List<DynamicReceive> dynamicReceives = new ArrayList<>();
    private static final String GET_ALL_DYNAMIC = MainActivity.host + MainActivity.port + "/get_all_dynamic/";
    private String res;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    Log.e(TAG, "handleMessage: "+res );
                    JSONArray jsonArray = new JSONArray(res);

                    dynamicReceives.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(jsonArray.length() - i - 1);
                        dynamicReceives.add(new DynamicReceive(jsonObject.getString("headImage"), jsonObject.getString("username"),
                                jsonObject.getString("context"), jsonObject.getString("image"), jsonObject.getString("time")));
                    }
                    initRecycleView();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "handleMessage: " + e.getMessage());
                }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        MainActivity.setToolBarColor(this);

        initView();
        initData();
        initClick();
    }

    private void initClick() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DynamicActivity.this, DynamicAddActivity.class);
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = "username=" + MainActivity.userName;
                    res = GetPostUtil.sendPostRequest(GET_ALL_DYNAMIC, data);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: " + e.getMessage());
                }
            }
        }).start();
    }

    private void initRecycleView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        userAdapter = new DynamicAdapter(this, dynamicReceives);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userAdapter);
        refreshLayout.setRefreshing(false);
    }

    private void initView() {
        recyclerView = findViewById(R.id.dynamic_recycle_view);
        add = findViewById(R.id.dynamic_add_find);
        refreshLayout = findViewById(R.id.dynamic_refresh);
    }
}
