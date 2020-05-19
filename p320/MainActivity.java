package com.example.p320;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button sendquest;
    TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendquest = (Button) findViewById(R.id.send_request);
        responseText = (TextView) findViewById(R.id.respone_text);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client = new OkHttpClient();
                    /*RequestBody requestBody=new FormBody.Builder()
                            .add("userName","admin")
                            .add("password","123456")
                            .build();*/

                    Request request=new Request.Builder()
                            .url("http://192.168.1.3:8080/androidWeb_war_exploded/aaa.xml")     //运行tomcat在网址后面加上xml的文件名
                            //.post(requestBody)
                            .build();
                    Response response=client.newCall(request).execute();
                    //得到服务器的数据
                    String responseData=response.body().string();
                    parseXMLWithSAX(responseData);      //解析xml文件
                    //showResponse(responseData);       //提交数据到web后台

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseXMLWithSAX(String responseData) {
        try {
            //得到SAX解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            //得到工厂中XML解析器
            XMLReader xmlReader = factory.newSAXParser().getXMLReader();

            ContentHandler handler = new ContentHandler();
            //将ContentHandler的实例设置到XMLReader中
            xmlReader.setContentHandler(handler);
            //开始解析
            xmlReader.parse(new InputSource(new StringReader(responseData)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
}
