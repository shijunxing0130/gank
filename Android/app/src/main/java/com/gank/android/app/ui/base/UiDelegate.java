package com.gank.android.app.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.gank.android.app.view.StateView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 *  使activity fragment 都拥有相同的UI，而且只维护一个类
 * Created by shijunxing
 */

public interface UiDelegate {



    /**通用对话框
     * @param msg
     */
     void showCommonDialog( String msg,DialogInterface.OnClickListener listener);

     void showCommonDialog( String msg);

    /**
     * 显示加载对话框
     */
    void showWaitDialog();

    /**
     * 显示加载对话框
     * @param msg
     */
    void showWaitDialog(String msg);

    /**
     * 隐藏加载对话框
     */
    void hideWaitDialog();

    /**
     * 初始化页面状态
     * @param view
     */
    void initPageStatueManager(View view);

    /**
     * 设置loadingView的自定义Layout
     * @param loadingResource loadingView的layoutResource
     */
     void setLoadingResource(@LayoutRes int loadingResource);

    /**
     * 设置emptyView的自定义Layout
     * @param emptyResource emptyView的layoutResource
     */
     void setEmptyResource(@LayoutRes int emptyResource);

    /**
     * 设置retryView的自定义Layout
     * @param retryResource retryView的layoutResource
     */
     void setErrorResource(@LayoutRes int retryResource);

     void setLoadingViewListener(int id, StateView.OnLoadingClickListener loadingClickListener);

     void setEmptyViewListener(int id, StateView.OnEmptyClickListener emptyClickListener);

     void setErrorViewListener(int id, StateView.OnRetryClickListener onRetryClickListener);

     void setErrorViewListener(int id, boolean isLoading, StateView.OnRetryClickListener onRetryClickListener);


    /**
     * 显示加载状态
     */
    View showLoadingStatue();

    /**
     * 显示错误状态
     */
    View showErrorStatue();

    /**
     * 显示为空状态
     */
    View showEmptyStatue();

    /**
     * 显示内容状态
     */
    void showContentStatue();


    /**
     * 吐司
     * @param text
     */
    void toast(String text);

    /**
     * 初始化刷新布局
     * @param view
     * @param listener
     */
    SmartRefreshLayout initRefreshLayout(View view, OnRefreshListener listener);

    SmartRefreshLayout initCustomRefreshLayout(View view, OnRefreshListener listener);

    /**
     * 停止刷新
     */
    void stopRefreshing();

    /**
     * 停止加载更多
     */
    void stopLoadMore();

    /**
     * 设置是否可以刷新
     * @param canRefresh
     */
    void setEnableRefresh(boolean canRefresh);

   /**
     * 设置是否可以加载更多
     * @param canLoadMore
     */
    void setEnableLoadMore(boolean canLoadMore);

    /**
     * 打开activity
     * @param cls
     */
    void startActivity(Class<?> cls);

    /**
     * 打开activity
     * @param data
     * @param cls
     */
    void startActivity(Bundle data, Class<?> cls);

    void start();

    void resume();

    void pause();

    void stop();

    void destory();

    void visible(boolean flag, View view);

    void gone(boolean flag, View view);

    void inVisible(View view);

}
