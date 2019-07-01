package com.example.xieyipeng.mineim.Find;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.xieyipeng.mineim.Contact.ContactUserAdapter;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.Message.UserAdapter;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.User;
import com.example.xieyipeng.mineim.javaBean.UserFriend;

import java.util.ArrayList;
import java.util.List;

public class DynamicActivity extends AppCompatActivity {

    private static final String TAG = "DynamicActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic);
        MainActivity.setToolBarColor(this);





        List<UserFriend> users=new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(new UserFriend("xieyipeng"," 1234",false));
        }
        LinearLayoutManager manager=new LinearLayoutManager(this);
        ContactUserAdapter userAdapter=new ContactUserAdapter(this,users);
        RecyclerView recyclerView=findViewById(R.id.find_recycle_view);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(userAdapter);



    }
}
