package com.gank.android.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.view.View;
import android.widget.Toast;

import com.gank.android.app.R;
import com.gank.android.app.aouth.GlobalConfig;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.view.SplashView;
import com.jaeger.library.StatusBarUtil;

import cn.gank.androidlibs.log.XLog;


/**
 * 广告页
 *
 * @author Administrator
 * @date 2017/3/22
 */

public class AdActivity extends BaseActivity {

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        // call after setContentView(R.layout.activity_sample);
        SplashView.showSplashView(this, 3, R.drawable.welcome_bg, new SplashView.OnSplashViewActionListener() {
            @Override
            public void onSplashImageClick(String actionUrl) {
                XLog.d("SplashView", "img clicked. actionUrl: " + actionUrl);
                Toast.makeText(AdActivity.this, "img clicked.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSplashViewDismiss(boolean initiativeDismiss) {
                XLog.d("SplashView", "dismissed, initiativeDismiss: " + initiativeDismiss);
                if (GlobalConfig.isFirstInstall()) {
                    GlobalConfig.setFirstInstall(false);
                    startActivity(new Intent(AdActivity.this,WelcomeActivity.class));
                }else {
                    startActivity(new Intent(AdActivity.this,MainActivity.class));
                }
                finish();
            }
        });

        // call this method anywhere to update splash view data
        SplashView.updateSplashData(this, "http://ww2.sinaimg.cn/large/72f96cbagw1f5mxjtl6htj20g00sg0vn.jpg", "http://jkyeo.com");
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }


    @Override
    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setTransparentForImageView(this,null);
    }
}
