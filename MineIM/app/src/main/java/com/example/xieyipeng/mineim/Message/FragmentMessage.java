package com.example.xieyipeng.mineim.Message;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.UserMessage;
import com.example.xieyipeng.mineim.tools.GetPostUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentMessage extends Fragment {

    //TODO: 下拉刷新 https://github.com/tohodog/QSRefreshLayout
    //TODO: SearchView: https://www.jianshu.com/p/00cb87a2964f

    private Toolbar toolbar;
    private static final String TAG = "FragmentMessage";
    private final String GET_FRIEND = MainActivity.host + MainActivity.port + "/get_all_friend/";
    private SearchView searchView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<UserMessage> users = new ArrayList<>();
    private String res;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    JSONArray jsonArray = new JSONArray(res);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        UserMessage userMessage = new UserMessage(jsonObject.get("name").toString(), jsonObject.get("headImg").toString(), (boolean) jsonObject.get("isOnline"));
                        users.add(userMessage);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "handleMessage: " + e.getMessage());
                }
                initRecycleView();
            }
        }
    };

    public static FragmentMessage newInstance() {
        return new FragmentMessage();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        initViews(view);
        initToolBar();
        setSearchView();
        initClick();
        initData();
        initRecycleView();

        return view;
    }


    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = "my_name=" + MainActivity.userName;
                    res = GetPostUtil.sendPostRequest(GET_FRIEND, data);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: error: " + e.getMessage());
                }
            }
        }).start();
    }

    private void initRecycleView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.context));
        recyclerView.setAdapter(new UserAdapter(MainActivity.context, users));
    }

    /**
     * 隐藏searchview的默认下划线
     */
    private void setSearchView() {
        //设置搜索框展开时是否显示提交按钮，可不显示
        searchView.setSubmitButtonEnabled(false);
        //让键盘的回车键设置成搜索
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

    }

    private void initClick() {

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //TODO: 搜索已有好友

                Toast.makeText(MainActivity.context, "搜索好友" + query, Toast.LENGTH_SHORT).show();
                searchView.setQuery("", false);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });


    }

    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar_message);
        searchView = view.findViewById(R.id.message_search_view);
        recyclerView = view.findViewById(R.id.message_user_recycle_view);
        refreshLayout = view.findViewById(R.id.message_refresh);
    }

    private void initToolBar() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        assert appCompatActivity != null;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            String title = "消息";
            actionBar.setTitle(title);
            //返回箭头
            //actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    //tollBar
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_message_friend:

                //TODO: 添加好友

                Toast.makeText(MainActivity.context, "toolbar_message_friend", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.toolbar_message_group:

                //TODO: 添加群

                Toast.makeText(MainActivity.context, "toolbar_message_group", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_message, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>


}
