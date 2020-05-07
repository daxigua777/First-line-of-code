package com.example.broadcastbestpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
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
        pref = getPreferences(MODE_PRIVATE);  //获取SharedPreferences对象
        final CheckBox cb1 = findViewById(R.id.cb1); //获取复选框
        boolean bl = pref.getBoolean("boolean",false);
        if (bl) {
            //将账号与密码读取到文本框中
            String zh = pref.getString("zh","");
            String mm = pref.getString("mm","");
            et1.setText(zh);
            et2.setText(mm);
            cb1.setChecked(true);
        }
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zh = et1.getText().toString(); //获取输入的账号
                String mm = et2.getText().toString(); //获取输入的密码
                //如果账号是admin且密码为123456就认定登录成功否则登录失败
                if (zh.equals("chen") && mm.equals("123456")){
                    editor = pref.edit();
                    if (cb1.isChecked()) {
                        //判断复选框是否被选中
                        editor.putBoolean("boolean", true);
                        editor.putString("zh",zh);
                        editor.putString("mm",mm);
                    } else {
                        editor.clear(); //将数据清除
                    }
                    editor.apply();  //提交editor
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
