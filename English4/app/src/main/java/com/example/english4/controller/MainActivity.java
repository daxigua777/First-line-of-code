package com.example.english4.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.english4.R;
import com.example.english4.pojo.English;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActivityCollector implements View.OnClickListener {

    //获取控件
    RadioButton etoc;
    RadioButton ctoe;
    TextView timu;
    TextView tihao;
    EditText daan;
    TextView jieguo;
    TextView defen;
    Button next;
    Button sh;
    Button syg;
    boolean isFanYi;    //判断是etoc或者是ctoe
    int i;  //数组下标
    int sum;    //计算答对了多少题
    float deFen; //计算得分
    float xiaoTiFen;  //小题得分
    List list = new ArrayList();
    English[] e = {
            new English("public", "公共的", false),
            new English("class", "类", false),
            new English("extends", "继承", false),
            new English("this", "当前对象", false),
            new English("button", "按钮", false),
            new English("top", "上", false)
    };

    public MainActivity() {
        //构造函数 对成员变量进行赋值
        isFanYi = true;
        i = 0;
        sum = 0;
        deFen = 0.0f;
        xiaoTiFen =(float) 100 / e.length;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);
        initview(); //对控件进行初始化
        etoc.setOnClickListener(this);
        ctoe.setOnClickListener(this);
        next.setOnClickListener(this);
        sh.setOnClickListener(this);
        syg.setOnClickListener(this);
        daan.addTextChangedListener(new TextWatcher() {
            //判断答案的输入框
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框中有东西的时候，就屏蔽上面的两个按钮
                if (count > 0) {
                    if (isFanYi) {
                        etoc.setEnabled(true);
                        ctoe.setEnabled(false);
                    } else {
                        etoc.setEnabled(false);
                        ctoe.setEnabled(true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }   //onCreate

    private void initview() {
        //获取控件id
        etoc = findViewById(R.id.etoc);
        ctoe = findViewById(R.id.ctoe);
        timu = findViewById(R.id.ed_timu);
        tihao = findViewById(R.id.tv_timu);
        daan = findViewById(R.id.ed_daan);
        jieguo = findViewById(R.id.ed_jieguo);
        defen = findViewById(R.id.ed_defeng);
        next = findViewById(R.id.xiaYiGe);
        sh = findViewById(R.id.pingPan);
        syg = findViewById(R.id.shangYiGe);
        chuti(isFanYi, 0);
    }

    //按钮的点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etoc:
                isFanYi = true;
                //出题
                chuti(isFanYi, i);
                break;
            case R.id.ctoe:
                isFanYi = false;
                //出题
                chuti(isFanYi, i);
                break;
            case R.id.xiaYiGe:
                i++;
                if (i >= e.length) i = 0;
                if (e[i].isB()) {
                    for (int j = i; j < e.length; j++) {
                        if (!e[j].isB()) {
                            i = j;
                            chuti(isFanYi, j);
                            break;
                        }
                        if (j == e.length - 1) j = -1;
                    }
                } else chuti(isFanYi, i);
                break;
            case R.id.pingPan:
                /**
                 * 1.得到答案
                 * 2.根据当前是英译汉还是汉译英来确定正确答案
                 * 3. .setEnabled()  是否启用   true为启用  false为屏蔽当前的控件
                 */
                String daan1 = daan.getText().toString().trim();    //trim 去掉首尾空格
                ShenHe(daan1, isFanYi);
                break;
            case R.id.shangYiGe:
                i--;
                if (i < 0) i = e.length - 1;
                if (e[i].isB()) {
                    for (int j = i; j < e.length; j--) {
                        if (!e[j].isB()) {
                            i = j;
                            break;
                        }
                        if (j == 0) j = e.length;
                    }
                }
                chuti(isFanYi, i);
                break;
        }
    }

    //审核
    private void ShenHe(String str, boolean b) {
        String zqdan;
        if (b) {
            //当前模式为etoc
            if (e[i].getChinise().equals(str)) {
                bingo();
            } else jieguo.setText("错误");
        } else {
            //当前模式为ctoe
            if (e[i].getEng().equals(str)) {
                bingo();
            } else jieguo.setText("错误");
        }
    }

    //回答正确
    private void bingo() {
        e[i].setB(true);
        sum();
        jieguo.setText("正确");
        sh.setEnabled(false);   //屏蔽这个按钮
    }


    //计算答对了多少题
    private void sum() {
        deFen = deFen + xiaoTiFen;
        sum++;
        //defen.setText("你答对了" + deFen  + "题");
        defen.setText(deFen + "");
        if (sum == e.length) dhk();
    }

    //对话框
    private void dhk() {
        //新建一个对话框
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("提示");  //设置对话框的标题
        dialog.setMessage("本次训练已全部练完");   //设置对话框的内容
        dialog.setCancelable(false);    //设置back键能否退出对话框
        //设置对话框的取消按钮及点击事件
        dialog.setPositiveButton("再练一次", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("退出系统", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.finishAll();
            }
        });
        dialog.show();  //启用这个对话框
    }

    /**
     * 出题
     */
    private void chuti(boolean b, int m) {
        String Timu;
        if (b) {
            Timu = e[m].getEng();
        } else {
            Timu = e[m].getChinise();
        }
        int m1 = m + 1;
        tihao.setText("第" + m1 + "题");
        timu.setText(Timu);
        daan.setText("");
        jieguo.setText("");
        sh.setEnabled(true);
    }
}   //类结束
