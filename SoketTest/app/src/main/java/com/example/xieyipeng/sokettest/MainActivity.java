package com.example.xieyipeng.sokettest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xieyipeng.sokettest.javaBean.GetPostUtil;
import com.example.xieyipeng.sokettest.javaBean.Message;
import com.google.gson.Gson;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private static final String type = "chat_message";
    private Button conn;
    //TODO: 默认聊天室：xie_yang
    private static final String roomName = "xie_yang";
    private WebSocketClient webSocketClient;
    public static Context context;
    private StringBuilder stringBuilder = new StringBuilder();

    private EditText editText;
    private Button send;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initClick();

    }


    private void initClick() {
        send.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                final String content = editText.getText().toString().trim();
                if (content.equals("")) {
                    Toast.makeText(MainActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onClick: state: " + webSocketClient.getReadyState());
                    if (webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                        Message message = new Message();
                        message.setMessage(content);
                        message.setMy_type(Message.MESSAGE_TYPE_QW);
                        message.setRoom(roomName);
                        message.setType(type);

                        Gson gson = new Gson();
                        String messageJson = gson.toJson(message);
                        Log.e(TAG, "onClick: " + messageJson);
                        webSocketClient.send(messageJson);
                        Log.e(TAG, "onClick: context: " + content);
                        stringBuilder.append("发送：")
                                .append(content)
                                .append("\n");
                        textView.setText(stringBuilder.toString());
                        Toast.makeText(MainActivity.this, "发送成功！", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "连接断开！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initWebSocket();
            }
        });
    }

    private void initView() {
        context = MainActivity.this;
        editText = findViewById(R.id.edit_content);
        send = findViewById(R.id.send);
        textView = findViewById(R.id.content);
        conn = findViewById(R.id.conn);
    }

    private void initWebSocket() {
        String url = "ws://10.0.2.2:8000/ws/chat/" + roomName + "/";
        try {
            URI uri = new URI(url);
            webSocketClient = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.e(TAG, "onOpen: " + handshakedata.getHttpStatusMessage());
                }

                @Override
                public void onMessage(String message) {
                    Log.e(TAG, "onMessage: " + message);
                    try {

                        JSONObject jsonObject = new JSONObject(message);
                        String mes = jsonObject.getString("message");

                        if (jsonObject.getString("my_type").equals(Message.MESSAGE_TYPE_WQ_OK)){
                            stringBuilder.append("接收：")
                                    .append(mes)
                                    .append("\n");
                            textView.setText(stringBuilder.toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onMessage: " + e.getMessage());
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.e(TAG, "onClose: connect closed!  code:" + code + "  reason: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    Log.e(TAG, "onError: " + ex.getMessage());
                }
            };
            webSocketClient.connect();
            while (!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
//                Toast.makeText(this, "连接房间： " + roomName + "中,请稍后", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "initWebSocket: 连接房间..." );
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onClick: " + e.getMessage());
                }
            }
            if (webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.e(TAG, "run: " + e.getMessage());
                        }
                        Looper.prepare();
                        Toast.makeText(MainActivity.context, "连接成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }).start();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Log.e(TAG, "initWebSocket: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocketClient.close();
        Log.e(TAG, "onDestroy: closed");
    }
}

/**
 * errorCode:
 * 1000	        CLOSE_NORMAL	            正常关闭; 无论为何目的而创建, 该链接都已成功完成任务.
 * 1001	        CLOSE_GOING_AWAY	        终端离开, 可能因为服务端错误, 也可能因为浏览器正从打开连接的页面跳转离开.
 * 1002	        CLOSE_PROTOCOL_ERROR	    由于协议错误而中断连接.
 * 1003	        CLOSE_UNSUPPORTED	        由于接收到不允许的数据类型而断开连接 (如仅接收文本数据的终端接收到了二进制数据).
 * 1004	         	                        保留. 其意义可能会在未来定义.
 * 1005	        CLOSE_NO_STATUS	            保留.  表示没有收到预期的状态码.
 * 1006	        CLOSE_ABNORMAL	            保留. 用于期望收到状态码时连接非正常关闭 (也就是说, 没有发送关闭帧).
 * 1007	        Unsupported Data	        由于收到了格式不符的数据而断开连接 (如文本消息中包含了非 UTF-8 数据).
 * 1008	        Policy Violation	        由于收到不符合约定的数据而断开连接. 这是一个通用状态码, 用于不适合使用 1003 和 1009 状态码的场景.
 * 1009	        CLOSE_TOO_LARGE	            由于收到过大的数据帧而断开连接.
 * 1010	        Missing Extension	        客户端期望服务器商定一个或多个拓展, 但服务器没有处理, 因此客户端断开连接.
 * 1011	        Internal Error	            客户端由于遇到没有预料的情况阻止其完成请求, 因此服务端断开连接.
 * 1012	        Service Restart	            服务器由于重启而断开连接. [Ref]
 * 1013	        Try Again Later	            服务器由于临时原因断开连接, 如服务器过载因此断开一部分客户端连接. [Ref]
 * 1014	 	                                由 WebSocket 标准保留以便未来使用.
 * 1015	        TLS Handshake	            保留. 表示连接由于无法完成 TLS 握手而关闭 (例如无法验证服务器证书).
 * 1016–1999	 	                        由 WebSocket 标准保留以便未来使用.
 * 2000–2999	 	                        由 WebSocket 拓展保留使用.
 * 3000–3999	 	                        可以由库或框架使用. 不应由应用使用. 可以在 IANA 注册, 先到先得.
 * 4000–4999	 	                        可以由应用使用.
 */
