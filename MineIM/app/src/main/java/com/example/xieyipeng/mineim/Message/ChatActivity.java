package com.example.xieyipeng.mineim.Message;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.MessageSendReceive;
import com.example.xieyipeng.mineim.javaBean.UserMessage;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private String others;
    private EditText send;
    private RecyclerView recyclerView;
    private static final String TAG = "ChatActivity";
    private TextView sendButton;
    private List<MessageSendReceive> userMessages = new ArrayList<>();
    private static final String LOBBY = "ws://192.168.137.1:8000/ws/chat/lobby/";
    private WebSocketClient webSocketClient;
    private static ChatAdapter chatAdapter;
    private List<MessageSendReceive> messageSendReceives = new ArrayList<>();
    private static LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        manager = new LinearLayoutManager(this);

        initIM();
        initView();
        initRecycleView();
        initClick();
    }


    private void initClick() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = send.getText().toString().trim();
                if (message.equals("")) {
                    Toast.makeText(ChatActivity.this, "消息不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    MessageSendReceive messageSendReceive = new MessageSendReceive("chat_message",
                            "qq-web", message, "lobby", others, MainActivity.userName);
                    Gson gson = new Gson();
                    String data = gson.toJson(messageSendReceive);
                    if (webSocketClient.getReadyState() != WebSocketClient.READYSTATE.OPEN) {
                        Toast.makeText(ChatActivity.this, "连接关闭,正在重连,请稍后...", Toast.LENGTH_SHORT).show();
                        webSocketClient.close();
                        initIM();
                    } else {
                        webSocketClient.send(data);
                        chatAdapter.myNotify(messageSendReceive);
                        send.setText("");
                        hideSoftInputView();
                    }
                }
            }
        });
    }

    private void initIM() {
        try {
            URI uri = new URI(LOBBY);
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.e(TAG, "onOpen: handshake...");
                }

                @Override
                public void onMessage(String message) {
                    Log.e(TAG, "onMessage: onMessage...");
                    try {
                        Log.e(TAG, "onMessage: " + message);
                        JSONObject jsonObject = new JSONObject(message);
                        final MessageSendReceive messageSendReceive = new MessageSendReceive(jsonObject.getString("type"),
                                jsonObject.getString("my_type"),
                                jsonObject.getString("message"),
                                jsonObject.getString("room"),
                                jsonObject.getString("send_to"),
                                jsonObject.getString("my_sender"));
                        if (messageSendReceive.getSend_to().equals(MainActivity.userName)) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatAdapter.myNotify(messageSendReceive);
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onMessage: " + e.getMessage());
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.e(TAG, "onClose: 连接关闭...");
                }

                @Override
                public void onError(Exception ex) {
                    Log.e(TAG, "onError: " + ex.getMessage());
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        webSocketClient.connect();
        Toast.makeText(ChatActivity.this, "IM连接成功", Toast.LENGTH_SHORT).show();
    }


    private void initRecycleView() {
        recyclerView.setLayoutManager(manager);
        chatAdapter = new ChatAdapter(ChatActivity.this, messageSendReceives);
        recyclerView.setAdapter(chatAdapter);
    }


    private void initView() {
        MainActivity.setToolBarColor(this);
        Intent intent = getIntent();
        others = intent.getStringExtra("others");
        TextView otherName = findViewById(R.id.chat_other_name);
        otherName.setText(others);
        send = findViewById(R.id.chat_activity_edit_text);
        recyclerView = findViewById(R.id.chat_activity_message_recycle_view);
        sendButton = findViewById(R.id.chat_activity_send_button);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: close");
        webSocketClient.close();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void scrollToBottom() {
        manager.scrollToPositionWithOffset(chatAdapter.getItemCount() - 1, 0);
    }

}
