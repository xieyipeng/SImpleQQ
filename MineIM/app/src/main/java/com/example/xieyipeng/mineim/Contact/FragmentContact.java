package com.example.xieyipeng.mineim.Contact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.Message.FragmentMessage;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.User;
import com.example.xieyipeng.mineim.javaBean.UserFriend;
import com.example.xieyipeng.mineim.tools.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentContact extends Fragment {

    private ImageView addFriend;
    private SearchView searchView;
    private LinearLayout recentFriend;
    private static final String TAG = "FragmentContact";
    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                Log.e(TAG, "handleMessage: "+ res);
                try {
                    JSONArray jsonArray=new JSONArray(res);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        UserFriend userFriend=new UserFriend(jsonObject.get("name").toString(),jsonObject.get("headImg").toString(),(boolean)jsonObject.get("isOnline"));
                        users.add(userFriend);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "handleMessage: "+e.getMessage() );
                }
                initRecycleView();
            }
        }
    };
    private String res;

    private LinearLayout group;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private final String ADD_SHOW_FRIEND =MainActivity.host+MainActivity.port+"/check_is_friend/";
    private final String GET_ALL_FRIEND = MainActivity.host+MainActivity.port+"/get_all_friend/";
    private List<UserFriend> users = new ArrayList<>();


    public static FragmentContact newInstance() {
        return new FragmentContact();
    }

    @SuppressLint("HandlerLeak")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        initViews(view);
        initSearchView();
        initClick();
        initData();
        return view;
    }




    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = "my_name=" + MainActivity.userName;
                    res = GetPostUtil.sendPostRequest(GET_ALL_FRIEND, data);
                    Message message=new Message();
                    message.what=1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: error: "+e.getMessage() );
                }
            }
        }).start();
    }

    private void initRecycleView() {
        ContactUserAdapter adapter = new ContactUserAdapter(MainActivity.context, users);
        LinearLayoutManager manager = new LinearLayoutManager(MainActivity.context);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initClick() {
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.context, "添加朋友", Toast.LENGTH_SHORT).show();

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                if (query.equals(MainActivity.userName)) {
                    Toast.makeText(MainActivity.context, "自己不能添加自己", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        final String s = "friend_name=" + query;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String if_have_user = GetPostUtil.sendPostRequest(ADD_SHOW_FRIEND, s);
                                if (if_have_user.equals("exist")) {
                                    Intent intent = new Intent(MainActivity.context, AddFriendActivity.class);
                                    intent.putExtra("name", query);
                                    startActivity(intent);
                                } else {
                                    Looper.prepare();
                                    Toast.makeText(MainActivity.context, "用户不存在", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "onQueryTextSubmit: " + e.getMessage());
                    }
                }
                searchView.setQuery("", false);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        recentFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.context, "show 最近添加的朋友", Toast.LENGTH_SHORT).show();
            }
        });

        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.context, "show all 群组", Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //模拟网络请求需要3000毫秒，请求完成，设置setRefreshing 为false
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });
    }

    private void initSearchView() {
        //TODO: SearchView: https://www.jianshu.com/p/00cb87a2964f
        //设置搜索框展开时是否显示提交按钮，可不显示
        searchView.setSubmitButtonEnabled(false);
        //让键盘的回车键设置成搜索
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

    }

    private void initViews(View view) {
        addFriend = view.findViewById(R.id.contact_add_friend);
        searchView = view.findViewById(R.id.contact_search_view);
        recentFriend = view.findViewById(R.id.contact_recent_layout);
        group = view.findViewById(R.id.contact_group_layout);
        refreshLayout = view.findViewById(R.id.contact_refresh);
        recyclerView = view.findViewById(R.id.contact_recycle_view);
    }
}
