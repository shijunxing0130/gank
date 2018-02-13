package com.gank.android.app.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;


import com.gank.android.app.GankApp;
import com.gank.android.app.R;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.config.Constant;
import com.gank.android.app.view.FragmentTabHost;
import com.jaeger.library.StatusBarUtil;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;


public class MainActivity extends BaseActivity implements TabHost.OnTabChangeListener {

    @BindView(android.R.id.tabhost)
    FragmentTabHost tabhost;

    private long lastBackTime = 0;
    private Intent intent = null;
    private Fragment currentFragment = null;

    private void init() {
        intent = getIntent();
    }

    private void initView() {
        tabhost.setup(this, getSupportFragmentManager(), R.id.frameLayout);
        tabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        tabhost.setOnTabChangedListener(this);
        for (UIPager pager : UIPager.values()) {
            View tabView = pager.getTabView(this);
            tabhost.addTab(tabhost.newTabSpec(pager.name()).setIndicator(tabView), pager.getPager(), null);
        }
    }

    private void initParams() {
        if (intent == null) {
            return;
        }
        int pagerIndex = intent.getIntExtra(Constant.CURRENT_FRAME_PAGER, -1);
        if (pagerIndex != -1) {
            tabhost.setCurrentTab(pagerIndex);
        }
    }


    @Override
    public void onTabChanged(String tabId) {
        currentFragment = getSupportFragmentManager().findFragmentByTag(tabId);
        UIPager type = UIPager.getType(tabId);
//        if (type == UIPager.MSG) {
//
//        }

        if (currentFragment instanceof OnHomeTabChangedListener) {
            ((OnHomeTabChangedListener) currentFragment).onHomeTabChanged();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (currentFragment != null) {
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        long currentBackTime = System.currentTimeMillis();
        if (currentBackTime - lastBackTime > 2000) {
            getUiDelegate().toast("再按一次返回键退出");
            lastBackTime = currentBackTime;
        } else {
            super.onBackPressed();
            ((GankApp) getApplication()).exitApp();
        }
    }

    @Override
    public void initVariables() {
        init();
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initView();
        initParams();
    }

    @Override
    public void loadData() {
//        checkPath();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setTransparentForImageViewInFragment(MainActivity.this, null);
    }


}

