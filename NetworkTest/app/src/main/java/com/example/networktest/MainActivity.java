package com.example.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParserFactory;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView responseText;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendRequest = (Button) findViewById(R.id.button1);
        Button bt2 = findViewById(R.id.button2);
        bt2.setOnClickListener(this);
        responseText = (TextView) findViewById(R.id.text1);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                sendRequestWithHttpURLConnection();
                break;
            case R.id.button2:
                sendRequestWithHttpOkHttp();
                break;
                default:
                    break;
        }
    }

    private void sendRequestWithHttpOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            //指定访问的服务器是电脑本机
                            .url("http://192.168.1.3/get_data.xml")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseXMLWithSAX(responseData);      //使用sax方法解析
                    //parseXMLWithPull(responseData);     //使用pull方法解析
                    //showResponse(responseData);       //使用OkHttp
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void parseXMLWithSAX(String xmlData) {
                try {
                    SAXParserFactory factory = SAXParserFactory.newInstance();
                    XMLReader xmlReader = factory.newSAXParser().getXMLReader();
                    MyHandler handler = new MyHandler();
                    //将ContentHandler的实例设置到XMLReader中
                    xmlReader.setContentHandler(handler);
                    xmlReader.parse(new InputSource(new StringReader(xmlData)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void parseXMLWithPull(String xmlData) {
                try {
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xmlPullParser = factory.newPullParser();
                    xmlPullParser.setInput(new StringReader(xmlData));
                    int enentType = xmlPullParser.getEventType();
                    String id = "";
                    String name = "";
                    String version = "";
                    while (enentType != XmlPullParser.END_DOCUMENT) {
                        String nodeName = xmlPullParser.getName();
                        switch (enentType) {
                            //开始解析某个节点
                            case XmlPullParser.START_TAG: {
                                if ("id".equals(nodeName)) {
                                    id = xmlPullParser.nextText();
                                } else if ("name".equals(nodeName)) {
                                    name = xmlPullParser.nextText();
                                } else if ("version".equals(nodeName)) {
                                    version = xmlPullParser.nextText();
                                }
                                break;
                            }
                            //完成解析某个节点
                            case XmlPullParser.END_TAG: {
                                if ("app".equals(nodeName)) {
                                    Log.d(TAG, "id is:" + id);
                                    Log.d(TAG, "name id:" + name);
                                    Log.d(TAG, "version is:" + version);

                                }
                                break;
                            }
                            default:
                                break;
                        }
                        enentType = xmlPullParser.next();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendRequestWithHttpURLConnection() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //在这里进行ui操作，将结果显示在界面上
                responseText.setText(response);
            }
        });
    }
}