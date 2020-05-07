package com.example.broadcastbestpractice;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

class ForceOfflineReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); //新建一个弹窗
        builder.setTitle("警告");//设置弹窗标题
        builder.setMessage("你以下线");//设置弹窗内容
        builder.setCancelable(false); //设置为弹窗不可取消
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置一个弹窗的按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.finishAll();//销毁所有活动
                Intent i = new Intent(context,MainActivity.class);
                context.startActivity(i);  //重启MainActivity
            }
        });
        builder.show();//启动弹窗
    }
}
