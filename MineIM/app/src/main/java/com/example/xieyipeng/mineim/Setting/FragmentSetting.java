package com.example.xieyipeng.mineim.Setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xieyipeng.mineim.Find.DynamicAddActivity;
import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.tools.GetPostUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class FragmentSetting extends Fragment {

    private ImageView headImage;
    private final int N = 1;
    private static final String TAG = "FragmentSetting";
    private String res;
    private String COMMIT_HEAD_IMAGE = MainActivity.host + MainActivity.port + "/change_head_image/";

    private static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    private InputStream inputStream;
    private File output_image;

    private static final int CHOOSE_PHOTO = 2;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                Log.e(TAG, "handleMessage: " + res);
            }
        }
    };


    public static FragmentSetting newInstance() {
        return new FragmentSetting();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        initViews(view);
        initHead();
        initClick();
        return view;
    }

    private void initClick() {
        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.context);
                builder.setTitle("选择拍照或相册选择")
                        .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                output_image = new File(MainActivity.context.getExternalCacheDir(), "output_image.jpg");
                                try {
                                    if (output_image.exists()) {
                                        output_image.delete();
                                    }
                                    output_image.createNewFile();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "onClick: " + e.getMessage());
                                }
                                if (Build.VERSION.SDK_INT >= 24) {
                                    imageUri = FileProvider.getUriForFile(MainActivity.context, "com.example.unique.string", output_image);
                                } else {
                                    imageUri = Uri.fromFile(output_image);
                                }
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, TAKE_PHOTO);
                            }
                        })
                        .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (ContextCompat.checkSelfPermission(MainActivity.context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions((Activity) MainActivity.context, new String[]{
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                } else {
                                    openAlbum();
                                }
                            }
                        })
                        .show();
            }
        });
    }


    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    private void changeHeadImage() {
//        try {
//            if (inputStream != null) {
//                DataInputStream dataInputStream = new DataInputStream(inputStream);
//                byte[] bufferOut = new byte[1024];
//                int bytes = 0;
//                // 每次读1KB数据,并且将文件数据写入到输出流中
//                while ((bytes = dataInputStream.read(bufferOut)) != -1) {
//                    Log.e(TAG, "onClick: " + Arrays.toString(bufferOut));
//                }
//                Toast.makeText(MainActivity.context, "成功", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "onClick: over");
//            } else {
//                Looper.prepare();
//                Toast.makeText(MainActivity.context, "输入流为空", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "onClick: " + e.getMessage());
//        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // map
                    Map<String, String> map = new HashMap<>();
                    map.put("username", MainActivity.userName);
                    // stream && file_name
                    InputStream[] inputStreams = null;
                    String[] fileNames = null;
                    if (inputStream != null) {
                        inputStreams = new InputStream[N];
                        inputStreams[0] = inputStream;
                        fileNames = new String[1];
                        fileNames[0] = "/user_" + MainActivity.userName + "headImg.jpg";
                    }
                    // 请求
                    res = GetPostUtil.upLoadFiles(COMMIT_HEAD_IMAGE, map, inputStreams, fileNames);
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "changeHeadImage: " + e.getMessage());
                }
            }
        }).start();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(MainActivity.context, "权限拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: request_code: " + requestCode);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                        changeHeadImage();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Log.e(TAG, "onActivityReenter: " + e.getMessage());
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        assert data != null;
                        handlerImageOnKitKat(data);
                    } else {
                        assert data != null;
                        handlerImageBeforeKitKat(data);
                    }
                }
                Toast.makeText(MainActivity.context, "相册选择", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void handlerImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handlerImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(MainActivity.context, uri)) {
            //doc类型的uri
            String docId = DocumentsContract.getDocumentId(uri);
            assert uri != null;
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else {
            assert uri != null;
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                //content类型的uri
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //file类型的uri
                imagePath = uri.getPath();
            }
        }
        displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            File temp = new File(imagePath);
            try {
                inputStream = new FileInputStream(temp);
                changeHeadImage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "displayImage: " + e.getMessage());
            }
        } else {
            Toast.makeText(MainActivity.context, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        //通过uri和selection来获取真实路径
        Cursor cursor = getActivity().getContentResolver().query(externalContentUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }
        return path;
    }

    private void initViews(View view) {
        headImage = view.findViewById(R.id.setting_head_image);
        TextView username = view.findViewById(R.id.setting_user_name);
        username.setText(MainActivity.userName);
        TextView createTime = view.findViewById(R.id.setting_create_time);
        createTime.setText("创建时间： " + MainActivity.date_joined);

    }

    private void initHead() {
        String headUrl = MainActivity.host + MainActivity.nginx_port + "/default/headImg.jpg";
        Glide.with(MainActivity.context)
                .load(headUrl)
                .into(headImage);
    }
}
