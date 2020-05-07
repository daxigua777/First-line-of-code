package com.example.broadcastbestpractice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private EditText et1;
    private EditText et2;
    private Button bt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = findViewById(R.id.ed1);
        et2 = findViewById(R.id.ed2);
        bt1 = findViewById(R.id.bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zh = et1.getText().toString(); //获取输入的账号
                String mm = et2.getText().toString(); //获取输入的密码
                //如果账号是admin且密码为123456就认定登录成功否则登录失败
                if (zh.equals("chen") && mm.equals("123456")){
                    Intent  intent = new Intent(MainActivity.this,Main2Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(MainActivity.this,"账号或密码不正确",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
