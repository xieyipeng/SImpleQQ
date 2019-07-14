package com.example.xieyipeng.mineim.Find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.Message.FragmentMessage;
import com.example.xieyipeng.mineim.R;

public class FragmentFind extends Fragment {


    private LinearLayout dynamic;


    public static FragmentFind newInstance() {
        return new FragmentFind();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        initViews(view);
        initClick();
        return view;
    }

    private void initClick() {
        dynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.context, DynamicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initViews(View view) {
        dynamic = view.findViewById(R.id.find_dynamic);
    }
}
