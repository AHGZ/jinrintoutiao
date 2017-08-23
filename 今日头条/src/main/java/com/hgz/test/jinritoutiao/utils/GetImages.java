package com.hgz.test.jinritoutiao.utils;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Administrator on 2017/8/11.
 */

public class GetImages {
    public static void getImage(String path, ImageView imageView){
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoader.getInstance().displayImage(path,imageView,options);
    }
}
