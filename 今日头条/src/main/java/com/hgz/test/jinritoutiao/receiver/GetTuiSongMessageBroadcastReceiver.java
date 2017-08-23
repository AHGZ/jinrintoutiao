package com.hgz.test.jinritoutiao.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/8/8.
 */

public class GetTuiSongMessageBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            String string = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Toast.makeText(context, "得到的字段为"+json, Toast.LENGTH_SHORT).show();
        }
    }
}
