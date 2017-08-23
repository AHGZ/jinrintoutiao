package com.hgz.test.jinritoutiao;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by Administrator on 2017/8/5.
 */

public class SMSActivity extends Activity implements View.OnClickListener {

    private EditText etPhoneNumber;
    private EditText etVerificationCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_activity);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        TextView tvGetVerificationCode = (TextView) findViewById(R.id.tv_get_verification_code);
        TextView tvVerificationCode = (TextView) findViewById(R.id.tv_verification_code);
        TextView tvShowVerificationPagerV = (TextView) findViewById(R.id.tv_show_verification_pager);

        tvGetVerificationCode.setOnClickListener(this);
        tvVerificationCode.setOnClickListener(this);
        tvShowVerificationPagerV.setOnClickListener(this);
        //注册短信回调
        SMSSDK.registerEventHandler(eh);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_get_verification_code:
                SMSSDK.getVerificationCode("86",etPhoneNumber.getText().toString().trim(), new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        return false;
                    }
                });
                break;
            case R.id.tv_verification_code:
                SMSSDK.submitVerificationCode("86",etPhoneNumber.getText().toString().trim(),etVerificationCode.getText().toString().trim());
                break;
            //跳转到别人已经定义好的页面
            case R.id.tv_show_verification_pager:
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                            String country = (String) phoneMap.get("country");
                            String phone = (String) phoneMap.get("phone");
                        }
                    }
                });
                registerPage.show(SMSActivity.this);
                break;
        }
    }
    EventHandler eh=new EventHandler(){

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SMSActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //提交验证码成功
                }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SMSActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //获取验证码成功
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){

                    //返回支持发送验证码的国家列表
                }
            }else{
                ((Throwable)data).printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
