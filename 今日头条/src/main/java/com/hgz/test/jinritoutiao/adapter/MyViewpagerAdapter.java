package com.hgz.test.jinritoutiao.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class MyViewpagerAdapter extends PagerAdapter {
    private List<View> imgs;
    public MyViewpagerAdapter(List<View> imgs){
        this.imgs=imgs;
    }
    @Override
    public int getCount() {
        return imgs==null?0:imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(imgs.get(position));
        return imgs.get(position);
    }
}
