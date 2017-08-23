package com.hgz.test.jinritoutiao.utils;

import android.content.Context;

import com.hgz.test.jinritoutiao.app.MyApplication;

/**
 * Created by Administrator on 2017/8/10.
 */

public class NetUtils {

    public static final String SP_NAME = "SP_NAME";
    public static final String PICTURE_LOAD_MODE_KEY = "PICTURE_LOAD_MODE_KEY";
    private boolean isNetWorkConnctionState = true;

    private static final String BASE_URL_BIG_PICTURE = "http://www.big.picture(最佳效果)";
    private static final String BASE_URL_SMALL_PICTURE = "http://www.small.picture(智能效果)";
    private static final String BASE_URL_NO_PICTURE = "http://www.no.picture(极省流量)";
    //baseurl默认使用大图的模式
    private String BASE_URL = BASE_URL_BIG_PICTURE;

    private static NetUtils mNetUtils;

    public NetUtils() {

    }

    //单例模式
    public static NetUtils getInstance() {
        if (mNetUtils == null) {
            synchronized (NetUtils.class) {
                if (mNetUtils == null) {
                    mNetUtils = new NetUtils();
                }
            }
        }
        return mNetUtils;
    }


    public String getBASE_URL() {

        //根据网络状态和用户选择，选择对应的baseurl
        if (isNetWorkConnctionState) {
            //根据用户选择 返回对应的url
            int mode = MyApplication.getAppContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).getInt(PICTURE_LOAD_MODE_KEY, 0);
            switch (mode) {
                case 0:
                    BASE_URL = BASE_URL_BIG_PICTURE;
                    break;
                case 1:
                    BASE_URL = BASE_URL_SMALL_PICTURE;
                    break;
                case 2:
                    BASE_URL = BASE_URL_NO_PICTURE;
                    break;
            }
        } else {
            BASE_URL = BASE_URL_BIG_PICTURE;
        }
        return BASE_URL;
    }

    //更改我们当前的网络状态
    public void changeNetState(boolean isNetWorkConnctionState) {
        this.isNetWorkConnctionState = isNetWorkConnctionState;
    }
}
