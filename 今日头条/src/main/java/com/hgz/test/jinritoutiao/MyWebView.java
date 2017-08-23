package com.hgz.test.jinritoutiao;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hgz.test.jinritoutiao.dialog.MyDialog;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/14.
 */

public class MyWebView extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        final TextView titles = (TextView) findViewById(R.id.titles);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        final WebView showWebview = (WebView) findViewById(R.id.showWebview);
        WebSettings settings = showWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        showWebview.loadUrl(url);
        showWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titles.setText(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });
        showWebview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                //给html中的img设置点击事件,当点击的时候遍历所有图片传给安卓
                //调用js 中的方法,去遍历, 如果没有这个方法我们需要自己写一个遍历的方法
                showWebview.loadUrl("javascript:(function(){" +
                        "var objs = document.getElementsByTagName(\"img\"); " +
                        "var imgUrl = \"\";" +
                        "var filter = [\"img//EventHead.png\",\"img//kong.png\",\"hdtz//button.png\"];" +
                        "var isShow = true;" +
                        "for(var i=0;i<objs.length;i++){" +
                        "for(var j=0;j<filter.length;j++){" +
                        "if(objs[i].src.indexOf(filter[j])>=0) {" +
                        "isShow = false; break;}}" +
                        "if(isShow && objs[i].width>100){" +
                        "imgUrl += objs[i].src + ',';isShow = true;" +
                        "    objs[i].onclick=function()  " +
                        "    {  "
                        + "        window.imageListener.openImage(imgUrl);" +
                        "    }" +
                        "}" +
                        "}" +
                        "})()");
            }
        });
        //javascript调用安卓的方法  window.安卓一个类的别名.安卓这个类的方法名(传入对应的参数)
        //通过这个addJavascriptInterface方法,将安卓中的类和他对应的别名传给js
        showWebview.addJavascriptInterface(new JavascriptInterface(),"imageListener");

        //去拿屏幕的宽高
        getWindowHeightAndWidth();
    }
    /**
     * 去哪屏幕的宽和高
     */
    public void getWindowHeightAndWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        Config.WINDOW_WIDTH=displayMetrics.widthPixels;
        Config.WINDOW_HEIGHT=displayMetrics.heightPixels;
    }
    /**
     * js调用安卓的类
     */
    public class JavascriptInterface {
        //js将所有图片url用逗号分隔拼接成字符串,通过 openImage(String imageUrl)这个方法的参数传给我们
        @android.webkit.JavascriptInterface
        public void openImage(String imageUrl) {
            String[] imgs = imageUrl.split(",");
            //将字符串解析成图片url的集合
            final ArrayList<String> imgUrlList = new ArrayList<>();
            for (String s : imgs) {
                imgUrlList.add(s);
            }
            //从js回调回来的方法默认是在js的一个线程中是子线程,所以要做更新UI的操作的话需要放到主线程
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //拿到图片url集合去显示我们的viewpager
                    new MyDialog(MyWebView.this,imgUrlList).show();
                }
            });
        }
    }
}
