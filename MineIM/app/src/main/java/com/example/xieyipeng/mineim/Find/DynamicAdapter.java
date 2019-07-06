package com.example.xieyipeng.mineim.Find;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.DynamicReceive;
import com.example.xieyipeng.mineim.javaBean.Love;
import com.example.xieyipeng.mineim.tools.GetPostUtil;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class DynamicAdapter extends RecyclerView.Adapter<DynamicAdapter.ViewHolder> {

    private Context context;
    private String res;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            if (msg.what == 1) {
                if (res.equals("success")) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    dynamicReceives.remove(msg.arg1);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private List<DynamicReceive> dynamicReceives = new ArrayList<>();
    private List<Love> is = new ArrayList<>();

    public DynamicAdapter(Context context, List<DynamicReceive> receives) {
        this.context = context;
        this.dynamicReceives = receives;
        for (int i = 0; i < dynamicReceives.size(); i++) {
            is.add(new Love(0));
        }
    }

//    public void myNotify(DynamicReceive receive) {
//        dynamicReceives.add(receive);
//        notifyDataSetChanged();
//    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dynamic, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final DynamicReceive receive = dynamicReceives.get(i);


        Glide.with(context)
                .load(MainActivity.host + MainActivity.nginx_port + "/" + receive.getHeadImage())
                .into(viewHolder.imageHead);
        viewHolder.userName.setText(receive.getUsername());
        viewHolder.context.setText(receive.getContext());


        if (receive.getImage().equals("")) {
            viewHolder.contextImage.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(MainActivity.host + MainActivity.nginx_port + "/" + receive.getImage())
                    .into(viewHolder.contextImage);
        }
        viewHolder.time.setText(receive.getTime());

        viewHolder.cancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String data = "username=" + MainActivity.userName + "&time=" + receive.getTime();
                        res = GetPostUtil.sendPostRequest(MainActivity.host + MainActivity.port + "/delete_dynamic/", data);

                        Message message = new Message();
                        message.what = 1;
                        message.arg1 = i;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });
        viewHolder.love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is.get(i).getIs()==1){
                    viewHolder.love.setImageResource(R.drawable.ic_love_none);
                    is.get(i).setIs(0);
                }else {
                    viewHolder.love.setImageResource(R.drawable.ic_love_have);
                    is.get(i).setIs(1);

                }
            }
        });
        viewHolder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return dynamicReceives.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView imageHead;
        TextView userName;
        TextView context;
        ImageView contextImage;
        TextView time;
        ImageView cancer;
        ImageView love;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageHead = itemView.findViewById(R.id.item_dynamic_head_image);
            userName = itemView.findViewById(R.id.item_dynamic_username);
            context = itemView.findViewById(R.id.item_dynamic_context);
            contextImage = itemView.findViewById(R.id.item_dynamic_context_image);
            time = itemView.findViewById(R.id.item_dynamic_time);
            cancer = itemView.findViewById(R.id.item_dynamic_cancer);
            love=itemView.findViewById(R.id.item_dynamic_love);
        }
    }
}
