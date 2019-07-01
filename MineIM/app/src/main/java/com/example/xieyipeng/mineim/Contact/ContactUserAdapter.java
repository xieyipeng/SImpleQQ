package com.example.xieyipeng.mineim.Contact;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.UserFriend;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ContactUserAdapter extends RecyclerView.Adapter<ContactUserAdapter.ViewHolder> {

    private Context context;
    private List<UserFriend> users = new ArrayList<>();

    public ContactUserAdapter(Context context, List<UserFriend> users) {
        this.context = context;
        this.users = users;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final UserFriend user = users.get(i);
        viewHolder.name.setText(user.getName());
        viewHolder.isOnline.setText(user.getOnline() ? "在线" : "离线");
        Glide.with(MainActivity.context)
                .load(MainActivity.host + MainActivity.nginx_port + "/" + user.getHeadImg())
                .into(viewHolder.imageView);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, user.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        RoundedImageView imageView;
        TextView name;
        TextView isOnline;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            name = itemView.findViewById(R.id.item_contact_user_name);
            isOnline = itemView.findViewById(R.id.item_contact_user_is_online);
            imageView = itemView.findViewById(R.id.item_contact_user_head_image);
        }
    }
}
