package com.gank.android.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.blankj.utilcode.util.Utils;
import com.gank.android.app.aouth.User;
import com.gank.android.app.config.Constant;
import com.gank.android.app.config.gallery.GlideImageLoader;
import com.gank.android.app.config.gallery.GlidePauseOnScrollListener;
import com.orhanobut.logger.Logger;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.zhy.autolayout.config.AutoLayoutConifg;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.gank.androidlibs.XConf;
import cn.gank.androidlibs.base.BaseAppManager;
import cn.gank.androidlibs.httphelper.HttpFactory;
import cn.gank.androidlibs.image.loader.ImageLoader;
import cn.gank.androidlibs.log.XLog;

import static com.gank.android.app.config.Constant.BUGLY_ID;


/**
 * @author Administrator
 * @date 2017/3/9
 */

public class GankApp extends Application {


    private static Context context;

    public interface MsgDisplayListener {
        void handle(String msg);
    }

    public static MsgDisplayListener msgDisplayListener = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        init();
    }


    /**
     * 初始化应用的其他组件
     */
    private void init() {

        Utils.init(this);
        HttpFactory.getHelper().init(this);
        initBugly();
        BGASwipeBackManager.getInstance().init(this);
        AutoLayoutConifg.getInstance().useDeviceSize();
        initFinalGallry();
        initImageLoader();
        initBmob();
//        initCrashCatch();

    }

    private void initBmob() {
        Bmob.initialize(this, Constant.BOMB_ID);
        // 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    XLog.i("initBmob",bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
                } else {
                    XLog.e("initBmob",e.getMessage());
                }
            }
        });

        if (User.isPush()) {
            // 启动推送服务
            BmobPush.startWork(this);
        }

    }


    private void initImageLoader() {
        ImageLoader.init(getApplicationContext());
    }

    private void initCrashCatch() {

    }

    /**
     * 相册选择
     */
    private void initFinalGallry() {
        //设置主题
        ThemeConfig theme = ThemeConfig.DARK;
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new GlideImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
    }



    /**
     * bugly配置
     */
    private void initBugly() {

        /**
         * Beta高级设置
         * true表示app启动自动初始化升级模块；
         * false不好自动初始化
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
         * 在后面某个时刻手动调用
         */
        Beta.autoInit = true;

        /**
         * true表示初始化时自动检查升级
         * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
         */
        Beta.autoCheckUpgrade = false;

        /**
         * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
         */
        Beta.initDelay = 1 * 1000;
        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源；
         */
        Beta.largeIconId = R.drawable.icon_gank;
        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源id;
         */
        Beta.smallIconId = R.drawable.icon_gank;
        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.drawable.bg;

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true;

        Bugly.init(getApplicationContext(), BUGLY_ID, XConf.IS_DEBUG);

    }


    public void exitApp() {
        BaseAppManager.getInstance().clear();
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static Context getContext() {
        return context;
    }
}
