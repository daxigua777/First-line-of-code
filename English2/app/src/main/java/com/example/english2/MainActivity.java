package com.example.english2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.english2.dao.English;

public class MainActivity extends AppCompatActivity {

    private float j;
    private float xiaofen;
    private int i = 0;
    private TextView ed_timu;
    private EditText ed_daan;
    boolean d = true;   //设置审核按钮的点击
    int cs = 0;     //记录审核的点击次数
    English[] e = {
            new English("public","公共的",true),
            new English("class","类",true),
            new English("extends","继承",true),
            new English("this","当前对象",true),
            new English("button","按钮",true)
    };

    //返回上一题
    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_timu = findViewById(R.id.ed_timu);
        ed_daan = findViewById(R.id.ed_daan);
        final TextView tv_xs = findViewById(R.id.tv_xs);
        final TextView tv_fenshu = findViewById(R.id.ed_fenshu);
        final Button bt_sh = findViewById(R.id.bt_sh);
        final Button bt_next = findViewById(R.id.bt_next);
        tv_xs.setText("当前第" + i+1 + "题," + "共" + e.length + "题");
        //  Button bt_cz = findViewById(R.id.bt_cz);
        //出题
        ed_timu.setText(e[0].getEng());
        //ed_timu.setKeyListener(null);
        ed_daan.setText("");
        tv_fenshu.setText("");
        //计算每题的分数
        xiaofen = 100/e.length;

        //审核
        bt_sh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String daan = ed_daan.getText().toString().trim();  //trim  去掉首位空格
                    if (d) {
                        d = false;
                        if (cs == e.length){
                            Toast.makeText(MainActivity.this,"你已经答完了",Toast.LENGTH_SHORT).show();
                        } else {
                            if (e[i].getChinese().equals(daan)) {
                                Toast.makeText(MainActivity.this,"回答正确",Toast.LENGTH_SHORT).show();
                                //得分
                                cs++;
                                tv_fenshu.setText(cs + "题");

                                e[i].setB(false);
                            } else {
                                Toast.makeText(MainActivity.this, "回答错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                }
            }
        });

        //下一个
        bt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下一个
                if (cs == e.length) {
                    Toast.makeText(MainActivity.this, "你已经答完了", Toast.LENGTH_SHORT).show();
                } else {
                    d = true;   //在点击下一题之后允许点击审核按钮
                    i++;
                    if (i > e.length - 1) i = 0;
                    for (int j = i; j <= e.length - 1; j++) {
                        if (e[j].isB()) {
                            ed_timu.setText(e[j].getEng());
                            ed_daan.setText("");
                            i = j;
                            break;
                        } else {
                            if (j == e.length - 1 && !e[j].isB()) {
                                j = -1;
                            }
                        }
                    }
                }
                    int hh = i + 1;
                    tv_xs.setText("当前第" + hh + "题," + "共" + e.length + "题");
                }

        });
        /*bt_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                j = 0;
                ed_timu.setText(e[0].getEng());
                ed_daan.setText("");
                ed_jieguo.setText("");
                tv_fenshu.setText("");
            }
        });  */
    }
}
