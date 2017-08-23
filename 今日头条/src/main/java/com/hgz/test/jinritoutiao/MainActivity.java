package com.hgz.test.jinritoutiao;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andy.library.ChannelActivity;
import com.andy.library.ChannelBean;
import com.example.city_picker.CityListActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hgz.test.jinritoutiao.adapter.MyPagerAdapter;
import com.hgz.test.jinritoutiao.bean.NewsInfo;
import com.hgz.test.jinritoutiao.dao.SQLiteDao;
import com.hgz.test.jinritoutiao.dao.SQLiteDao2;
import com.hgz.test.jinritoutiao.utils.NetUtils;
import com.hgz.test.jinritoutiao.utils.ThemeUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.limxing.xlistview.view.XListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @ViewInject(R.id.tabLayout)
    TabLayout tabLayout;
    @ViewInject(R.id.viewPager)
    ViewPager viewPager;
    @ViewInject(R.id.slidingmenu_toggle)
    ImageView slidingmenuToggle;
    private SlidingMenu slidingMenu;
    private SlidingMenu slidingMenu2;
    private LinstenerNewWorkBroadcastReceiver broadcastReceiver;
    private ImageView qqLogin;
    private ImageView smsYanZheng;
    private ImageView changeTheme;
    private ImageView downLoad;
    private ImageView setting;
    private Button btnMoreLogin;
    private ImageView seek;
    private ImageView refresh;
    private ArrayList<Fragment> fragments;
    private ImageView loadPinDao;
    List<ChannelBean> allPinDaoList;
    List<ChannelBean> userPinDaoList;
    private MyPagerAdapter myPagerAdapter;
    private SQLiteDao dao;
    private String[] tabTitles = new String[]{"头条", "社会", "国内", "国际", "娱乐", "体育", "军事", "科技", "财经", "时尚", "游戏", "汽车", "百家号", "搞笑", "金融", "数码", "养生", "教育", "家居", "政务", "星座", "文化", "手机"};
    private SQLiteDao2 dao2;
    private XListView lv;
    private List<NewsInfo.ResultBean.DataBean> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeUtil.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        dao2 = new SQLiteDao2(this);
        //调用网络监听广播
        registerBroadcastReceiver();
        x.view().inject(this);
        //调用添加数据的方法
        initData();
        //将viewpager和tablayout关联
        tabLayout.setupWithViewPager(viewPager);
        //调用左右侧拉的设置
        initSlidingmenu();
        initSlidingmenu2();
        //调用找控件的方法
        init();
        //设置按钮监听
        slidingmenuToggle.setOnClickListener(this);
        loadPinDao.setOnClickListener(this);
        seek.setOnClickListener(this);
        btnMoreLogin.setOnClickListener(this);
        qqLogin.setOnClickListener(this);
        smsYanZheng.setOnClickListener(this);
        changeTheme.setOnClickListener(this);
        downLoad.setOnClickListener(this);
        setting.setOnClickListener(this);
        refresh.setOnClickListener(this);
        HashSet<String> strings = new HashSet<>();
        strings.add("买飞机");
        strings.add("买火车");
        JPushInterface.setAliasAndTags(this, "heguozhong", strings, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });
    }

    private void initData() {
        allPinDaoList = new ArrayList<>();
        userPinDaoList = new ArrayList<>();
        dao = new SQLiteDao(this);

        List<ChannelBean> allPindao = dao.findAllPindao();
        if (allPindao == null || allPindao.size() < 1) {
            ChannelBean channelBean;
            for (int i=0;i<23;i++){
                if (i<10) {
                    channelBean= new ChannelBean(tabTitles[i], true);
                    userPinDaoList.add(channelBean);
                }else{
                    channelBean=new ChannelBean(tabTitles[i], false);
                }
                allPinDaoList.add(channelBean);
            }
            //保存数据库
            dao.addPindao(allPinDaoList);
        } else {
            //如果有数据的话 把查询出来的数据赋值到这个集合中
            allPinDaoList.addAll(allPindao);
            List<ChannelBean> userPindao = dao.findUserPindao();
            userPinDaoList.addAll(userPindao);
        }
        //设置适配器
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), userPinDaoList);
        viewPager.setAdapter(myPagerAdapter);

    }
    //找控件的方法
    private void init() {
        refresh = (ImageView) findViewById(R.id.refresh);
        seek = (ImageView) findViewById(R.id.seek);
        loadPinDao = (ImageView) findViewById(R.id.loadPinDao);
        qqLogin = (ImageView) findViewById(R.id.qqLogin);
        smsYanZheng = (ImageView) findViewById(R.id.smsYanZheng);
        btnMoreLogin = (Button) findViewById(R.id.btnMoreLogin);
        changeTheme = (ImageView) findViewById(R.id.changeTheme);
        downLoad = (ImageView) findViewById(R.id.downLoad);
        setting = (ImageView) findViewById(R.id.setting);
    }

    //左侧slidingmenu的设置
    private void initSlidingmenu() {
        slidingMenu = new SlidingMenu(this);
        //设置一下侧滑菜单的位置
        slidingMenu.setMode(SlidingMenu.LEFT);
        //设置菜单打开时，内容区域的宽度
        slidingMenu.setBehindOffset(300);
        //设置触摸的区域
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //范围是 0 - 1f ，当设置成1的时候菜单栏有明显的褪色效果
        slidingMenu.setFadeDegree(1f);
        //将slidingMenu和Activity关联起来
        slidingMenu.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);
        //给侧拉菜单设置布局
        slidingMenu.setMenu(R.layout.slidingmenu);
    }

    //右侧slidingmenu的设置
    private void initSlidingmenu2() {
        slidingMenu2 = new SlidingMenu(this);
        //设置一下侧滑菜单的位置
        slidingMenu2.setMode(SlidingMenu.RIGHT);
        //设置菜单打开时，内容区域的宽度
        slidingMenu2.setBehindOffset(300);
        //设置触摸的区域
        slidingMenu2.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //范围是 0 - 1f ，当设置成1的时候菜单栏有明显的褪色效果
        slidingMenu2.setFadeDegree(1f);
        //将slidingMenu和Activity关联起来
        slidingMenu2.attachToActivity(MainActivity.this, SlidingMenu.SLIDING_CONTENT);
        //给侧拉菜单设置布局
        slidingMenu2.setMenu(R.layout.slidingmenu2);
    }

    //友盟第三方登陆监听设置
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
            LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.linear2);
            linearLayout.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);

            ImageView qqImage = (ImageView) findViewById(R.id.qqImage);
            TextView tvname = (TextView) findViewById(R.id.name);
            TextView tvsex = (TextView) findViewById(R.id.sex);
            String name = data.get("name");
            String gender = data.get("gender");
            String iconurl = data.get("iconurl");
            tvname.setText(name);
            tvsex.setText(gender);
            getImage(iconurl, qqImage);
            getImage(iconurl, slidingmenuToggle);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            if (UMShareAPI.get(MainActivity.this).isInstall(MainActivity.this, SHARE_MEDIA.QQ)) {
                Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "没有安装相应的应用程序", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    //友盟
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == ChannelActivity.REQUEST_CODE && resultCode == ChannelActivity.RESULT_CODE) {
            String json = data.getStringExtra(ChannelActivity.RESULT_JSON_KEY);
            List<ChannelBean> list = new Gson().fromJson(json, new TypeToken<List<ChannelBean>>() {
            }.getType());
            allPinDaoList.clear();
            userPinDaoList.clear();

            allPinDaoList.addAll(list);
            for (ChannelBean channelBean : list) {
                if (channelBean.isSelect()) {
                    userPinDaoList.add(channelBean);
                }
            }
            myPagerAdapter.notifyDataSetChanged();
            //保存数据库
            dao.deletePindao();
            dao.addPindao(allPinDaoList);

        }
    }

    //按钮监听
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slidingmenu_toggle:
                //自动控制开关，当前是开会关闭，如果是关闭就会打开
                slidingMenu.toggle();
                //第二种方式，通过判断是否正在显示
//                if (slidingMenu.isMenuShowing()) {
//                    slidingMenu.showContent();
//                } else {
//                    slidingMenu.showMenu();
//                }
                break;
            case R.id.loadPinDao:
                ChannelActivity.startChannelActivity(this, allPinDaoList);
                break;
            case R.id.seek:
                Intent intent1 = new Intent(MainActivity.this, BaiDuWebView.class);
                startActivity(intent1);
                break;
            case R.id.refresh:
                CityListActivity.startCityActivityForResult(this);
                break;
            case R.id.qqLogin:
                UMShareAPI.get(MainActivity.this).getPlatformInfo(MainActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.smsYanZheng:
                Intent intent = new Intent(MainActivity.this, SMSActivity.class);
                startActivity(intent);
                break;
            case R.id.btnMoreLogin:
                slidingMenu2.toggle();
                break;
            case R.id.changeTheme:
                ThemeUtil.ChangeCurrentTheme(MainActivity.this);
                break;
            case R.id.downLoad:
                Intent intent2 = new Intent(MainActivity.this, DownLoadActivity.class);
                startActivity(intent2);
                break;
            case R.id.setting:
                Intent intent3 = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent3);
                break;
        }
    }

    //获取图片的方法
    private void getImage(String path, ImageView image) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new CircleBitmapDisplayer())
                .build();
        ImageLoader.getInstance().displayImage(path, image, options);
    }

    //注册广播
    private void registerBroadcastReceiver() {
        //动态注册广播,创建一个IntentFilter
        broadcastReceiver = new LinstenerNewWorkBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        //注册
        registerReceiver(broadcastReceiver, filter);
    }

    public void updataData(XListView lv, List<NewsInfo.ResultBean.DataBean> data) {
        this.lv=lv;
        this.data=data;
    }


    //接收网络状态的改变
    public class LinstenerNewWorkBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean isNetWorkConnctionState = true;
                //如果能走到这，说明网络已经发生变化
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    if (ConnectivityManager.TYPE_WIFI == activeNetworkInfo.getType()) {
                        isNetWorkConnctionState = false;
                        Toast.makeText(MainActivity.this, "wifi可用，下载吧", Toast.LENGTH_SHORT).show();
                    } else if (connectivityManager.TYPE_MOBILE == activeNetworkInfo.getType()) {
                        isNetWorkConnctionState = true;
                        Toast.makeText(MainActivity.this, "现在是移动网络，当心", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "网络不可用，请检查网络", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    showSettingNetWorkDialog();
                }
                //改变一下网络状态
                NetUtils.getInstance().changeNetState(isNetWorkConnctionState);
            }
        }
    }

    //网络设置对话框
    private void showSettingNetWorkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("没有设置网络，请您设置网络！！！");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //跳转网络设置界面  隐士意图
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }


    //销毁方式
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解注册
        unregisterReceiver(broadcastReceiver);
    }

}
