package com.example.shardpreferences;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.bt1);
        Button button2 = findViewById(R.id.bt2);
        final TextView tv1 = findViewById(R.id.tv1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor =  getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("name","tom");
                editor.putInt("age",28);
                editor.putBoolean("married",false);
                editor.apply();
                Toast.makeText(MainActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                String name = sharedPreferences.getString("name","");
                int age = sharedPreferences.getInt("age",0);
                boolean married = sharedPreferences.getBoolean("married",false);
                tv1.setText("姓名： " + name + "\n" + "年龄： " + age + "\n" + "是否结婚： " + married);
            }
        });
    }
}
