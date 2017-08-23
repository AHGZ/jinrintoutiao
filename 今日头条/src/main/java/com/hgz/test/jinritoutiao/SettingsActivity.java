package com.hgz.test.jinritoutiao;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hgz.test.jinritoutiao.app.MyApplication;
import com.hgz.test.jinritoutiao.utils.NetUtils;

import java.io.File;
import java.math.BigDecimal;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2017/8/9.
 */

public class SettingsActivity extends Activity implements View.OnClickListener {

    private SharedPreferences.Editor edit;
    private TextView tvHuancun;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        ImageView settingGoBack = (ImageView) findViewById(R.id.settingGoBack);
        TextView feiWifi = (TextView) findViewById(R.id.feiWifi);
        TextView pinglun = (TextView) findViewById(R.id.pinglun);
        CheckBox tuisong = (CheckBox) findViewById(R.id.tuisong);
        tvHuancun = (TextView) findViewById(R.id.tvHuancun);
        TextView qingli = (TextView) findViewById(R.id.qingli);
        boolean isTuisong = getSharedPreferences("tuisong", MODE_PRIVATE).getBoolean("isTuisong", true);
        SharedPreferences sp = getSharedPreferences("tuisong", MODE_PRIVATE);
        edit = sp.edit();
        settingGoBack.setOnClickListener(this);
        feiWifi.setOnClickListener(this);
        pinglun.setOnClickListener(this);
        qingli.setOnClickListener(this);
        tuisong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edit.putBoolean("isTuisong", true).commit();
                    //恢复推送服务
                    JPushInterface.resumePush(SettingsActivity.this);
                } else {
                    edit.putBoolean("isTuisong", false).commit();
                    //停止推送服务
                    JPushInterface.stopPush(SettingsActivity.this);
                }
            }
        });
        if (isTuisong == true) {
            tuisong.setChecked(true);
        } else {
            tuisong.setChecked(false);
        }
        try {
            //去计算缓存大小
            String totalCacheSize = getTotalCacheSize(this);
            tvHuancun.setText(totalCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingGoBack:
                finish();
                break;
            case R.id.feiWifi:
                String[] strings = {"最佳效果", "较省流量", "极省流量"};
                int mode = MyApplication.getAppContext().getSharedPreferences(NetUtils.SP_NAME, Context.MODE_PRIVATE).getInt(NetUtils.PICTURE_LOAD_MODE_KEY, 0);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("非wifi网络流量");
                builder.setSingleChoiceItems(strings, mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //要保存我们的图片加载模式
                        MyApplication.getAppContext().getSharedPreferences(NetUtils.SP_NAME, Context.MODE_PRIVATE).edit().putInt(NetUtils.PICTURE_LOAD_MODE_KEY, which).commit();
                        dialog.dismiss();
                        //测试一下,下载模式是否发生变化
                        Toast.makeText(SettingsActivity.this, NetUtils.getInstance().getBASE_URL(), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;
            case R.id.qingli:
                showClearDialog();
                break;
            case R.id.pinglun:

                break;
        }
    }

    //计算app的缓存大小其实计算的是 getCacheDir()这个file和getExternalCacheDir()这个file大小的和
    public String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * 计算文件夹的大小
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    //格式化得到的总大小 默认是byte,  然后根据传入的大小,自动转化成合适的大小单位
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    //清理app的缓存 其实是清除的getCacheDir 和getExternalCacheDir这两个文件
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    //删除一个文件夹
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //清除缓存的对话框
    private void showClearDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("清除缓存");
        builder.setMessage("是否确定清除缓存？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String totalCacheSize =null;
                try {
                    //清除缓存
                    clearAllCache(SettingsActivity.this);
                    totalCacheSize = getTotalCacheSize(SettingsActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tvHuancun.setText(totalCacheSize);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }
}