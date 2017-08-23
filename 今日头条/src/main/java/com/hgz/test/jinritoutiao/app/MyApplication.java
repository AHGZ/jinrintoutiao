package com.hgz.test.jinritoutiao.app;


import android.app.Application;
import android.content.Context;

import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/8/2.
 */

public class MyApplication extends Application{
    {
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication=this;
        x.Ext.init(this);
        UMShareAPI.get(this);
        Config.DEBUG = true;
        //配置Imageloader
//        File cacheDir= StorageUtils.getOwnCacheDirectory(this,"universalimageloader/Cache");
        File cacheDir=new File(getCacheDir(),"caches");
        ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(100,100)
                .memoryCacheSize(20*1024*1024)
                .threadPoolSize(5)
                .threadPriority(1000)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheFileCount(20)
                .diskCacheSize(50*1024*1024)
                .build();
        ImageLoader.getInstance().init(configuration);
        MobSDK.init(this, "1ff5c53d64164", "253894e69a78440fc2bf67bb5ad03f8a");
        //设置调试模式
        JPushInterface.setDebugMode(true);
        //init 初始化推送SDK
        JPushInterface.init(this);
    }
    public static Context getAppContext() {
        return myApplication;
    }
}
