package com.hgz.test.jinritoutiao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by Administrator on 2017/8/8.
 */

public class DownLoadActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download);
        Button btnDown = (Button) findViewById(R.id.btnDown);
        btnDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RequestParams requestParams = new RequestParams("http://tc.sinaimg.cn/maxwidth.800/tc.service.weibo.com/p3_pstatp_com/a0dc4efe4d9f66ea71dd1fee2691b6b6.jpg");
        //保存一下存储路径
        requestParams.setSaveFilePath(getExternalCacheDir().getAbsolutePath()+"/qwer");
        Log.i("DownLoadActivity=========",getExternalCacheDir().getAbsolutePath());
        //设置可以断点续传
        requestParams.setAutoRename(true);
        //设置一个线程池 有三个线程
        requestParams.setExecutor(new PriorityExecutor(3,true));
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Toast.makeText(DownLoadActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DownLoadActivity.this, "下载onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Toast.makeText(DownLoadActivity.this, "下载onFinished", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Log.i("DownLoadActivity=========",current/total*100+"%");
            }
        });
    }
}
