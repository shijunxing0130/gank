package com.gank.android.app.aouth;

import com.gank.android.app.GankApp;

import cn.gank.androidlibs.cache.SharedPref;

/**
 * Created by shijunxing on 2018/1/24.
 */

public class GlobalConfig {


    public static boolean isFirstInstall() {
        return SharedPref.getInstance(GankApp.getContext()).getBoolean("isFirstInstall", true);
    }

    public static void setFirstInstall(boolean isFirst) {
        SharedPref.getInstance(GankApp.getContext()).putBoolean("isFirstInstall", isFirst);
    }
}
