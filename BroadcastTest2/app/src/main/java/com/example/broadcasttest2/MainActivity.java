package com.example.broadcasttest2;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IntentFilter intentFilter;
    private LocalBroadcastManager localBroadcastManager;
    private LocalReceiver localReceiver;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastManager = localBroadcastManager.getInstance(this);//获取本地广播的实例
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.broadcasttest2.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver , intentFilter);//注册本地广播监听器
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                Intent intent = new Intent("com.example.broadcasttest2.LOCAL_BROADCAST");
                localBroadcastManager.sendBroadcast(intent);//发送本地广播
                break;
                default:
                    break;
        }
    }
}
