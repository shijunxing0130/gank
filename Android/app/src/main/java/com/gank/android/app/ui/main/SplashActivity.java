package com.gank.android.app.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gank.android.app.aouth.GlobalConfig;

/**
 * 开屏页
 * @author shijunxing
 * @date 2017/3/23
 */

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GlobalConfig.isFirstInstall()) {
            GlobalConfig.setFirstInstall(false);
            startActivity(new Intent(SplashActivity.this,WelcomeActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
        }
        finish();
    }
}
