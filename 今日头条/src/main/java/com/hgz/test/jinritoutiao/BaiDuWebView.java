package com.hgz.test.jinritoutiao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2017/8/15.
 */

public class BaiDuWebView extends Activity {

    private WebView baiduWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baidu_webview);
        baiduWebView = (WebView) findViewById(R.id.baidu_webview);
        baiduWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
    }
    public void ssss(View v){

        baiduWebView.loadUrl("http://www.baidu.com");
        //设置该方法,在当前app中显示网页

        WebSettings settings = baiduWebView.getSettings();
        //设置缓存模式
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //允许webview使用缩放功能
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);

    }

}
