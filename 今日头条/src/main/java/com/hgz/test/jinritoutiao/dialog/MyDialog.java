package com.hgz.test.jinritoutiao.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hgz.test.jinritoutiao.Config;
import com.hgz.test.jinritoutiao.R;
import com.hgz.test.jinritoutiao.adapter.MyViewpagerAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/8/22.
 */

public class MyDialog extends Dialog {

    private ViewPager imageviewPager;
    private TextView tvPager;
    private View view;
    private List<String> mImgUrls;
    private ArrayList<View> list;

    public MyDialog(@NonNull Context context,List<String> imgUrls) {
        super(context, R.style.transparentBgDialog);
        initView(context);
        initData(context,imgUrls);
    }

    private void initData(Context context, List<String> imgUrls) {
        mImgUrls=imgUrls;
        list = new ArrayList<>();
        tvPager.setText("1/"+mImgUrls.size());
        for (String url: mImgUrls) {
            PhotoView photoView = new PhotoView(context);
            //单击事件  当单击photoView的时候让这个dialog消失
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    //让dialog消失
                    dismiss();
                }
            });
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setLayoutParams(layoutParams);
            DisplayImageOptions options=new DisplayImageOptions.Builder()
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .build();
            ImageLoader.getInstance().displayImage(url,photoView,options);
            list.add(photoView);
        }
        MyViewpagerAdapter myViewpagerAdapter = new MyViewpagerAdapter(list);
        imageviewPager.setAdapter(myViewpagerAdapter);
        imageviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvPager.setText(position+1+"/"+mImgUrls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
        imageviewPager = (ViewPager) view.findViewById(R.id.imageViewpager);
        tvPager = (TextView) view.findViewById(R.id.tvPager);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        setContentView(view);
        //设置dialog 是全屏展示
        //一个dialog一个window,默认dialog的这个window是窗口的形式,不是全屏
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x=0;
        layoutParams.y=0;
        layoutParams.width= Config.WINDOW_WIDTH;
        layoutParams.height=Config.WINDOW_HEIGHT;
        window.setAttributes(layoutParams);
    }
}
