package com.example.broadcasttest;

import android.arch.lifecycle.ViewModelProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()){  //判断有没有联网
            Toast.makeText(context,"你开启了网络",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"你关闭了网络",Toast.LENGTH_SHORT).show();
        }
    }
}
