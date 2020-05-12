package com.example.palyaudiotest;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        videoView = findViewById(R.id.vv1);
        Button bt1 = findViewById(R.id.buttonPanel);
        Button bt2 = findViewById(R.id.buttonPane2);
        Button bt3 = findViewById(R.id.buttonPane3);

        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
        bt3.setOnClickListener(this);
        if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        } else {
            initViedeoPath();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initViedeoPath();
                } else {
                    Toast.makeText(Main2Activity.this, "无权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void initViedeoPath() {

        /*String uri = ("android.resource://" + getPackageName() + "/" + R.raw.movie);
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();*/

        File file = new File(Environment.getExternalStorageDirectory(), "2.mp4");
        videoView.setVideoPath(file.getPath());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null){
            videoView.suspend();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonPanel:
                if (!videoView.isPlaying()){
                    videoView.start();
                }
                break;
            case R.id.buttonPane2:
                if (videoView.isPlaying()) {
                    videoView.pause();
                }
                break;
            case R.id.buttonPane3:

                    videoView.resume();

                break;
        }
    }
}
