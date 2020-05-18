package com.example.servicebestpractice;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;   //表示下载成功
    public static final int TYPE_FAILED = 1;    //表示下载失败
    public static final int TYPE_PAUSED = 2;    //表示暂停下载
    public static final int TYPE_CANCELED = 3;  //表示取消下载

    private DownloadListener listener;

    private boolean isCanceld = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;   //将下载的状态通过这个参数进行回调
    }



    @Override
    protected Integer doInBackground(String... strings) {
        //用于在后台执行具体的下载逻辑
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try {
            long downloadedLength = 0;  //记录已下载的文件长度
            String downloadUrl = strings[0];    //获取下载的uri地址
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));  //根据uri地址解析出下载的文件名
            //将指定文件下载到Environment.DIRECTORY_DOWNLOADS目录，也就是sd卡的DOWNLOADS目录
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);     //获取文件长度
            if (contentLength == 0){
                return TYPE_FAILED;
            } else if (contentLength == downloadedLength) {
                //已下载字节和文件总字节相等，说明下载完成
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //断点下载，指定从哪个字节开始下载
                    .addHeader("RANGE", "bytes=" + downloadedLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null) {
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);   //跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                //读取服务器的数据写入到本地
                while ((len = is.read(b)) != -1) {
                    if (isCanceld) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(b, 0, len);
                        //计算已下载的百分比
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);  //进行通知
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if ( is != null) {
                    is.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceld && file != null) {
                    file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }




    @Override
    protected void onProgressUpdate(Integer... values) {
        //用于在界面上更新下载进度
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);  //获取当前的下载进度
            lastProgress = progress;        //更新下载进度
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        //用于通知最终的下载结果
        switch (integer) {
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
                default:
                    break;
        }
    }

    public void pauseDownload(){
        isPaused = true;
    }

    public void canceDownload(){
        isCanceld = true;
    }

    private long getContentLength(String downloadUrl) throws IOException {
        //获取要下载文件的长度
        OkHttpClient client = new OkHttpClient();   //创建一个OkHttpClient实例
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();                           //发起一条HTTP请求
        Response response = client.newCall(request).execute();  //获取服务器返回的数据
        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }
}
