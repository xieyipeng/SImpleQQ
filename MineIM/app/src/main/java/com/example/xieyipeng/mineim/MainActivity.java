package com.example.xieyipeng.mineim;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.xieyipeng.mineim.Contact.FragmentContact;
import com.example.xieyipeng.mineim.Find.FragmentFind;
import com.example.xieyipeng.mineim.Message.FragmentMessage;
import com.example.xieyipeng.mineim.Setting.FragmentSetting;
import com.example.xieyipeng.mineim.tools.GetPostUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private Fragment save_fragment_message;
    private Fragment save_fragment_contact;
    private Fragment save_fragment_find;
    private Fragment save_fragment_setting;

    public static final String host = "http://10.0.2.2:";
    public static final String port = "8000";
    public static final String nginx_port = "81";

    private static final String LOGOUT = host + port + "/user_logout/";

    private static final String TAG = "MainActivity";
    private FragmentMessage fragmentMessage;
    private FragmentContact fragmentContact;
    private FragmentFind fragmentFind;
    private FragmentSetting fragmentSetting;
    private FragmentManager fragmentManager;
    private Fragment fragment = new Fragment();
    public static Context context;
    public static String userName;
    public static String headImg;
    public static String date_joined;
    public static String last_login;

    private static String[] allFragment = new String[]{
            "fragmentMessage",
            "fragmentContact",
            "fragmentFind",
            "fragmentSetting"
    };

//    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        getData();
        setContentView(R.layout.activity_main);
        context = this;
        fragmentManager = getSupportFragmentManager();
        initBottom();
        setToolBarColor(this);

    }

    private void getData() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("user_data");
        try {
            JSONObject jsonObject = new JSONObject(data);
            userName = jsonObject.getString("username");
            headImg = jsonObject.getString("headImg");
            date_joined = jsonObject.getString("date_joined");
            last_login = jsonObject.getString("last_login");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "getData: " + e.getMessage());
        }
    }

    public static void setToolBarColor(Activity activity) {
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintColor(Color.parseColor("#6969d4"));
    }

    private void initBottom() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(listener);
        navigation.setSelectedItemId(navigation.getMenu().getItem(0).getItemId());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            hideAll();
            switch (item.getItemId()) {
                case R.id.navigation_message:
                    if (save_fragment_message == null) {
                        fragment = FragmentMessage.newInstance();
                        save_fragment_message = fragment;
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, fragment, "fragmentMessage")
                                .commit();
                    } else {
                        fragment = save_fragment_message;
                    }
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .show(fragment)
                            .commit();
                    return true;
                case R.id.navigation_contact:
                    if (save_fragment_contact == null) {
                        fragment = FragmentContact.newInstance();
                        save_fragment_contact = fragment;
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, fragment, "fragmentContact")
                                .commit();
                    } else {
                        fragment = save_fragment_contact;
                    }
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .show(fragment)
                            .commit();
                    return true;
                case R.id.navigation_find:
                    if (save_fragment_find == null) {
                        fragment = FragmentFind.newInstance();
                        save_fragment_find = fragment;
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, fragment, "fragmentFind")
                                .commit();
                    } else {
                        fragment = save_fragment_find;

                    }
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .show(fragment)
                            .commit();
                    return true;
                case R.id.navigation_setting:
                    if (save_fragment_setting == null) {
                        fragment = FragmentSetting.newInstance();
                        save_fragment_setting = fragment;
                        fragmentManager.beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, fragment, "fragmentSetting")
                                .commit();
                    } else {
                        fragment = save_fragment_setting;

                    }
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .show(fragment)
                            .commit();
                    return true;
            }
            return false;
        }
    };


    /**
     * 隐藏掉所有fragment
     */
    public void hideAll() {
        for (String tag : allFragment) {
            if (fragmentManager.findFragmentByTag(tag) != null) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .hide(fragmentManager.findFragmentByTag(tag))
                        .commit();
            }
        }
    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String data = "username=" + userName;
                    String logout = GetPostUtil.sendPostRequest(LOGOUT, data);
                    Log.e(TAG, "run: " + logout);
                    if (logout.equals("success")) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "退出错误", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: " + e.getMessage());
                }

            }
        }).start();
        super.onDestroy();
    }

    /**
     * 让fragment中的toolbar的menu菜单显示图标
     *
     * @param view
     * @param menu
     * @return
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "onPrepareOptionsPanel: " + e.getMessage());
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment f = fragmentManager.findFragmentByTag("fragmentSetting");
        assert f != null;
        f.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment f = fragmentManager.findFragmentByTag("fragmentSetting");
        assert f != null;
        f.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
