package com.example.xieyipeng.mineim.Message;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.User;
import com.example.xieyipeng.mineim.javaBean.UserMessage;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<UserMessage> users = new ArrayList<>();

    public UserAdapter(Context context, List<UserMessage> users) {
        this.context = context;
        this.users = users;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_user, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final UserMessage user = users.get(i);
        viewHolder.name.setText(user.getName());
        viewHolder.isOnline.setText(user.getOnline() ? "在线" : "离线");
        Glide.with(MainActivity.context)
                .load(MainActivity.host + MainActivity.nginx_port + "/" + user.getHeadImg())
                .into(viewHolder.imageView);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("others", user.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        RoundedImageView imageView;
        TextView name;
        TextView isOnline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.item_message_user_head_image);
            name = itemView.findViewById(R.id.item_message_user_name);
            isOnline = itemView.findViewById(R.id.item_message_user_add_data);
        }
    }
}
