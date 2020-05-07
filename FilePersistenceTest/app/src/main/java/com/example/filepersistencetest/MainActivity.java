package com.example.filepersistencetest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private EditText et1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = findViewById(R.id.et1);
        String inputText = load();
        if (!TextUtils.isEmpty(inputText)){
            et1.setText(inputText);
            et1.setSelection(inputText.length());
            Toast.makeText(this,"成功恢复",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String str = et1.getText().toString();//获取输入的信息
        save(str);
    }

    private void save(String str){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE); //得到FileOutputStream对象
            writer = new BufferedWriter(new OutputStreamWriter(out));  //构建出BufferedWriter对象
            writer.write(str);  //使用BufferedWriter对象将文本内容保存到文件中
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            try {
                if (writer != null){
                    writer.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private String load(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data"); //获取到一个FileInputStream对象
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
