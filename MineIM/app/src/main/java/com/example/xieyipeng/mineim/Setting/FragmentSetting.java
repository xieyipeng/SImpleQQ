package com.example.xieyipeng.mineim.Setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;

public class FragmentSetting extends Fragment {

    private ImageView headImage;


    public static FragmentSetting newInstance() {
        return new FragmentSetting();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initViews(view);
        initHead();
        return view;
    }

    private void initViews(View view) {
        headImage = view.findViewById(R.id.setting_head_image);
    }

    private void initHead() {
        String headUrl = MainActivity.host + MainActivity.nginx_port + "/default/headImg.jpg";
        Glide.with(MainActivity.context)
                .load(headUrl)
                .into(headImage);
    }
}
