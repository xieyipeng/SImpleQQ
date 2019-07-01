package com.example.xieyipeng.mineim.Message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.MessageSendReceive;
import com.example.xieyipeng.mineim.javaBean.UserMessage;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    private Context context;
    private List<MessageSendReceive> messages = new ArrayList<>();
    private static final String TAG = "ChatAdapter";

    public ChatAdapter(Context context, List<MessageSendReceive> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void myNotify(MessageSendReceive message) {
        messages.add(message);
        Log.e(TAG, "myNotify: ");
        notifyDataSetChanged();
        ChatActivity.scrollToBottom();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MessageSendReceive messageSendReceive = messages.get(i);
        if (messageSendReceive.getMy_sender().equals(MainActivity.userName)) {
            viewHolder.receive.setVisibility(View.GONE);
            Glide.with(context)
                    .load(MainActivity.host + MainActivity.nginx_port + "/" + MainActivity.headImg)
                    .into(viewHolder.sendImageView);
            viewHolder.sendTextView.setText(messageSendReceive.getMessage());
        } else {
            viewHolder.send.setVisibility(View.GONE);
            Glide.with(context)
                    .load(MainActivity.host + MainActivity.nginx_port + "/" + MainActivity.headImg)
                    .into(viewHolder.receiveImageView);
            viewHolder.receiveTextView.setText(messageSendReceive.getMessage());
        }
        //TODO: 调用viewHolder.setIsRecyclable(false);就可以禁止复用了，问题得到初步解决。
        //TODO: 用if  else控制不同的item显示，有if一定要有else。一开始就没注意这个，一直出问题。
        //TODO: 但是貌似禁止复用后性能会变差，还有一种设置tag记录item状态的的不知道要怎么使用。还是要接着学习把~
        viewHolder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout send;
        private RoundedImageView sendImageView;
        private TextView sendTextView;
        private RelativeLayout receive;
        private RoundedImageView receiveImageView;
        private TextView receiveTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            send = itemView.findViewById(R.id.item_send);
            sendImageView = itemView.findViewById(R.id.item_send_chat_photo);
            sendTextView = itemView.findViewById(R.id.item_send_chat_Text);

            receive = itemView.findViewById(R.id.item_receive);
            receiveImageView = itemView.findViewById(R.id.item_receive_chat_photo);
            receiveTextView = itemView.findViewById(R.id.item_receive_chat_Text);

        }
    }
}
