package cn.gank.androidlibs.base;

import android.os.Bundle;
import android.view.View;

/**
 * Created by wanglei on 2016/12/1.
 */

public interface UiCallback {

    /**
     * 初始化变量，包括intent带的数据和activity内的变量
     */
     void initVariables();

    /**
     * 家在布局文件，初始化控价，为控件挂上事件方法
     * @param view activity 这个参数为空
     * @param savedInstanceState
     */
     void initViews(View view,Bundle savedInstanceState);

    /**
     * 调用api获取数据
     */
     void loadData();


    /**
     * 获取布局文件
     * @return
     */
    int getLayoutId();

    /**
     * 是否使用事件总线
     * @return
     */
    boolean useEventBus();
}
