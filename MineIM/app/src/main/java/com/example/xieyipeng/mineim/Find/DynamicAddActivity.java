package com.example.xieyipeng.mineim.Find;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xieyipeng.mineim.MainActivity;
import com.example.xieyipeng.mineim.R;
import com.example.xieyipeng.mineim.javaBean.DynamicReceive;
import com.example.xieyipeng.mineim.tools.GetPostUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DynamicAddActivity extends AppCompatActivity {


    private TextView cancel;
    private TextView commit;
    private static final String TAG = "DynamicAddActivity";
    private final int N = 1;
    private final String add_dynamic = MainActivity.host + MainActivity.port + "/add_dynamic/";
    private EditText editText;
    private InputStream inputStream;
    private ImageView imageView;
    private String res;
    private String context;

    private static final int TAKE_PHOTO = 1;
    private Uri imageUri;
    private File output_image;

    private static final int CHOOSE_PHOTO = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (res.equals("success")) {
                    Toast.makeText(DynamicAddActivity.this, "发表成功", Toast.LENGTH_SHORT).show();
//                    image在本地
//                    DynamicActivity.userAdapter.myNotify(new DynamicReceive(MainActivity.headImg,MainActivity.userName,context,));
                    finish();
                } else {
                    Toast.makeText(DynamicAddActivity.this, "服务器未响应", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_add);
        MainActivity.setToolBarColor(this);

        initView();
        initClick();

    }


    private void initClick() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context = editText.getText().toString().trim();
                if (context.equals("")) {
                    Toast.makeText(DynamicAddActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // map
                            Map<String, String> map = new HashMap<>();
                            map.put("username", MainActivity.userName);
                            map.put("context", context);
                            // stream && file_name
                            InputStream[] inputStreams = null;
                            String[] fileNames = null;
                            if (inputStream != null) {
                                inputStreams = new InputStream[N];
                                inputStreams[0] = inputStream;
                                Date date = new Date();
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
                                String time = format0.format(date.getTime());//这个就是把时间戳经过处理得到期望格式的时间
                                fileNames = new String[1];
                                fileNames[0] = "Dynamic-" + time+".jpg";
                            }
                            // 请求
                            res = GetPostUtil.upLoadFiles(add_dynamic, map, inputStreams, fileNames);
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }).start();
                }


//                try {
//                    if (inputStream != null) {
//                        DataInputStream dataInputStream = new DataInputStream(inputStream);
//                        byte[] bufferOut = new byte[1024];
//                        int bytes = 0;
//                        // 每次读1KB数据,并且将文件数据写入到输出流中
//                        while ((bytes = dataInputStream.read(bufferOut)) != -1) {
//                            Log.e(TAG, "onClick: " + Arrays.toString(bufferOut));
//                        }
//                        Log.e(TAG, "onClick: over");
//                    } else {
//                        Looper.prepare();
//                        Toast.makeText(DynamicAddActivity.this, "输入流为空", Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e(TAG, "onClick: " + e.getMessage());
//                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DynamicAddActivity.this);
                builder.setTitle("选择拍照或相册选择")
                        .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                output_image = new File(getExternalCacheDir(), "output_image.jpg");
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
                                    imageUri = FileProvider.getUriForFile(DynamicAddActivity.this, "com.example.unique.string", output_image);
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
                                if (ContextCompat.checkSelfPermission(DynamicAddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(DynamicAddActivity.this, new String[]{
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "权限拒绝", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                Log.e(TAG, "onActivityResult: take photo");
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
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
                break;
            default:
                Log.e(TAG, "onActivityResult: 相机返回失败");
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
        if (DocumentsContract.isDocumentUri(this, uri)) {
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.e(TAG, "displayImage: " + e.getMessage());
            }
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        //通过uri和selection来获取真实路径
        Cursor cursor = getContentResolver().query(externalContentUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }
            cursor.close();
        }
        return path;
    }


    private void initView() {
        cancel = findViewById(R.id.add_dynamic_cancel);
        commit = findViewById(R.id.add_dynamic_commit);
        editText = findViewById(R.id.add_dynamic_edit);
        imageView = findViewById(R.id.add_dynamic_image);
    }
}
