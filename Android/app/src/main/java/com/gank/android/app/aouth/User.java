package com.gank.android.app.aouth;

import com.gank.android.app.GankApp;
import com.gank.android.app.entity.UserEntity;

import cn.gank.androidlibs.cache.SharedPref;

/**
 *
 * @author shijunxing
 * @date 2017/9/20
 */

public class User {

    public static void loginOut() {
        setIsLogin(false);
        setHead("");
        setName("");
        setToken("");
        setPhone("");
    }
    public static void setInfo(UserEntity userEntity){
        setIsLogin(true);
        setName(userEntity.getUsername());
        setPhone(userEntity.getPhone());
        setToken(userEntity.getToken());
        setHead(userEntity.getUserhead());
    }


    public static boolean isLogin() {
        return SharedPref.getInstance(GankApp.getContext()).getBoolean("login", false);
    }

    public static void setIsLogin(boolean isLogin) {
        SharedPref.getInstance(GankApp.getContext()).putBoolean("login", isLogin);
    }


    public static boolean isPush() {
        return SharedPref.getInstance(GankApp.getContext()).getBoolean("Push", true);
    }

    public static void setIsPush(boolean isPush) {
        SharedPref.getInstance(GankApp.getContext()).putBoolean("Push", isPush);
    }


    public static void setToken(String token) {
        SharedPref.getInstance(GankApp.getContext()).putString("token", token);
    }

    public static String getToken() {
        return SharedPref.getInstance(GankApp.getContext()).getString("token", "");
    }

    public static void setPhone(String phone) {
        SharedPref.getInstance(GankApp.getContext()).putString("phone", phone);
    }

    public static String getPhone() {
        return SharedPref.getInstance(GankApp.getContext()).getString("phone", "");
    }

    public static void setName(String name) {
        SharedPref.getInstance(GankApp.getContext()).putString("name", name);
    }

    public static String getName() {
        return SharedPref.getInstance(GankApp.getContext()).getString("name", "");
    }


    public static void setHead(String avatar) {
        SharedPref.getInstance(GankApp.getContext()).putString("oauth_avatar", avatar);
    }

    public static String getHead() {
        return SharedPref.getInstance(GankApp.getContext()).getString("oauth_avatar", "");
    }

    public static void setHasGankHuo(boolean hasGankHuo) {
        SharedPref.getInstance(GankApp.getContext()).putBoolean("HasGankHuo", hasGankHuo);
    }

    public static boolean isHasGankHuo() {
        return SharedPref.getInstance(GankApp.getContext()).getBoolean("HasGankHuo", false);
    }

    public static void setLastGankHuoDate(String lastGankHuoDate) {
        SharedPref.getInstance(GankApp.getContext()).putString("LastGankHuoDate", lastGankHuoDate);
    }

    public static String getLastGankHuoDate() {
        return SharedPref.getInstance(GankApp.getContext()).getString("LastGankHuoDate", "");
    }


}
