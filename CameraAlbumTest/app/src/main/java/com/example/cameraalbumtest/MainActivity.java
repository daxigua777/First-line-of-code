package com.example.cameraalbumtest;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ImageView iv1;      //图片控件;
    private Uri uri;    //照片的地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt1 = findViewById(R.id.button1);    //获取照相的按钮控件
        iv1 = findViewById(R.id.imageView1);    //获取拍摄的图片控件
        Button bt2 = findViewById(R.id.button2);    //获取选择图片的按钮控件
        bt1.setOnClickListener(new View.OnClickListener() {     //编写点击事件
            @Override
            public void onClick(View v) {
                //创建File对象，用于存储拍照后的照片，并将他存放在应用关联缓存目录下
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try {       //凡是对磁盘、卡进行操作的都用try
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                //判断版本号是否大于Android7.0
                if (Build.VERSION.SDK_INT >= 24) {
                    //将File对象转换成uri对象
                    //FileProvider定义了一个内容提供器，要在manifest注册
                    uri = FileProvider.getUriForFile(MainActivity.this,"com.example.cameraalbumtest.fileprovider", outputImage);
                } else {
                    uri = Uri.fromFile(outputImage);
                }

                //启动相机的程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, TAKE_PHOTO);
                //使用了startActivityForResult活动的，结果会返回到onActivityResult中
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //判断是否有对手机存储具有读写的权限，若没有则添加权限；
                    //WRITE_EXTERNAL_STORAGE表示授予程序对手机存储的读写权限
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                } else {
                    openAlbum();
                }
            }
        });
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);   //打开相册
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "你拒绝了这个权限", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
                        // 使用BitmapFactory.decodeStream将图片解析成bitmap对象，并且设置到Image View中显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        iv1.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {      //判断手机系统版本号
                        //4.4及以上的版本使用这个方法
                        handleImageOnKiKat(data);
                    } else {
                        //4.4以下的版本使用这个方法
                        handleImageBeforKiKat(data);
                    }
                }
                break;
                default:
                    break;
        }
    }

    private void handleImageBeforKiKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImaePath(uri, null);
        displayImage(imagePath);
    }

    private String getImaePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
           cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {   //显示图片
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            iv1.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "无法获取图片", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleImageOnKiKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];    //解析出数字格式的id
                String seletion = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImaePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, seletion);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contenturi = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImaePath(contenturi, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content的uri，则使用普通的方式处理
            imagePath = getImaePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }


}
