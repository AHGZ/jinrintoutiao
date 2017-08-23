package com.hgz.test.jinritoutiao.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.andy.library.ChannelBean;
import com.hgz.test.jinritoutiao.fragment.MyFragment;
import com.hgz.test.jinritoutiao.utils.HanZiToPinYin;

import java.util.List;

/**
 * Created by Administrator on 2017/8/2.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    private FragmentManager mFragmentManager;
    private List<ChannelBean> tabTitles;
    public MyPagerAdapter(FragmentManager fm, List<ChannelBean> tabTitles) {
        super(fm);
        mFragmentManager=fm;
        this.tabTitles=tabTitles;
    }

    @Override
    public Fragment getItem(int position) {
        MyFragment myFragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", HanZiToPinYin.getPinYinAllChar(tabTitles.get(position).getName(),1));
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position).getName();
    }
}
