package com.gank.android.app.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.utils.DialogHelp;
import com.gank.android.app.view.StateView;
import com.gank.android.app.utils.Toasty;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import cn.gank.androidlibs.base.XFragment;


/**
 * 使activity fragment 都拥有相同的UI，而且只要维护一个类就可以
 * @author shijunxing
 */

public class UiDelegateBase implements UiDelegate {

    public Context mContext;

    private Dialog mWaitDialog;

    private SmartRefreshLayout mRefreshLayout;
    private StateView mStateView;

    private UiDelegateBase(Context context) {
        this.mContext = context;
    }

    public static UiDelegateBase create(Context context) {
        return new UiDelegateBase(context);
    }




    @Override
    public void showCommonDialog(String msg,DialogInterface.OnClickListener listener) {
        DialogHelp.showCommonDialog(mContext,"取消","确定",msg,null,listener);
    }

    @Override
    public void showCommonDialog(String msg) {
        showCommonDialog(msg,null);
    }

    @Override
    public void showWaitDialog() {
        showWaitDialog("");
    }

    @Override
    public void showWaitDialog(String message) {
        if (mWaitDialog == null) {
            mWaitDialog = DialogHelp.getProgressDialog(mContext, message);
        }
        if (mWaitDialog != null) {
            mWaitDialog.show();
        }
    }

    @Override
    public void hideWaitDialog() {
        if (mWaitDialog != null) {
            try {
                mWaitDialog.dismiss();
                mWaitDialog = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void initPageStatueManager(View view) {
        mStateView = StateView.inject(view);

    }


    @Override
    public void setLoadingResource(@LayoutRes int loadingResource) {
        if (mStateView != null) {
            mStateView.setLoadingResource(loadingResource);
        }
    }

    @Override
    public void setEmptyResource(@LayoutRes int emptyResource) {
        if (mStateView != null) {
            mStateView.setEmptyResource(emptyResource);
        }
    }

    @Override
    public void setErrorResource(@LayoutRes int errorResource) {
        if (mStateView != null) {
            mStateView.setRetryResource(errorResource);
        }
    }

    @Override
    public void setLoadingViewListener(int id, StateView.OnLoadingClickListener loadingClickListener) {
        if (mStateView != null) {
            mStateView.setLoadingClickListener(id, loadingClickListener);
        }
    }


    @Override
    public void setEmptyViewListener(int id, StateView.OnEmptyClickListener emptyClickListener) {
        if (mStateView != null) {
            mStateView.setOnEmptyClickListener(id, emptyClickListener);
        }
    }

    @Override
    public void setErrorViewListener(int id, StateView.OnRetryClickListener onRetryClickListener) {
        if (mStateView != null) {
            mStateView.setOnRetryClickListener(id, onRetryClickListener);
        }
    }

    @Override
    public void setErrorViewListener(int id, boolean isLoading, StateView.OnRetryClickListener onRetryClickListener) {
        if (mStateView != null) {
            mStateView.setOnRetryClickListener(id, isLoading, onRetryClickListener);
        }
    }


    @Override
    public View showLoadingStatue() {
        if (mStateView != null) {
            return mStateView.showLoading();
        }
        return null;
    }

    @Override
    public View showErrorStatue() {
        if (mStateView != null) {
            return mStateView.showRetry();
        }
        return null;
    }

    @Override
    public View showEmptyStatue() {
        if (mStateView != null) {
            return mStateView.showEmpty();
        }
        return null;
    }

    @Override
    public void showContentStatue() {
        if (mStateView != null) {
            mStateView.showContent();
        }
    }


    @Override
    public void toast(String text) {
        Toasty.getInstance().showToast(mContext,text);

//        Alerter.create((Activity) mContext)
//                .setText(text)
//                .setBackgroundColorRes(R.color.toolbar_color) // or setBackgroundColorInt(Color.CYAN)
//                .setDuration(500)
//                .show();
    }


    /**
     * 注意id 一定是 R.id.swipe_refresh_layout
     *
     * @param view
     * @param listener
     */
    @Override
    public SmartRefreshLayout initRefreshLayout(View view, OnRefreshListener listener) {
        if (view.findViewById(R.id.srl_layout) != null) {
            mRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.srl_layout);
            ClassicsHeader header = new ClassicsHeader(view.getContext());
            header.setAccentColor(view.getContext().getResources().getColor(R.color.colorThemeBlack));
            header.setSpinnerStyle(SpinnerStyle.FixedBehind);

            BallPulseFooter footer = new BallPulseFooter(view.getContext());
            footer.setPrimaryColors(view.getContext().getResources().getColor(R.color.colorThemeBlack));

            mRefreshLayout.setRefreshHeader(header);
            mRefreshLayout.setHeaderHeight(80);
            mRefreshLayout.setRefreshFooter(footer);
            mRefreshLayout.setEnableOverScrollBounce(false);
            mRefreshLayout.setOnRefreshListener(listener);


            return mRefreshLayout;
        }

        return null;
    }

    @Override
    public SmartRefreshLayout initCustomRefreshLayout(View view, OnRefreshListener listener) {
        if (view.findViewById(R.id.srl_layout) != null) {
            mRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.srl_layout);
            if (listener != null) {
                mRefreshLayout.setOnRefreshListener(listener);
            }
            return mRefreshLayout;
        }

        return null;
    }

    @Override
    public void stopRefreshing() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishRefresh(true);
        }
    }

    @Override
    public void stopLoadMore() {
        if (mRefreshLayout != null) {
            mRefreshLayout.finishLoadmore(true);
        }
    }

    @Override
    public void setEnableRefresh(boolean canRefresh) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(canRefresh);
        }
    }

    @Override
    public void setEnableLoadMore(boolean canLoadMore) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableLoadmore(canLoadMore);
        }
    }

    @Override
    public void startActivity(Class<?> cls) {
        startActivity(null,cls);
    }

    @Override
    public void startActivity(Bundle data, Class<?> cls) {
        Router.newIntent()
                .from((Activity) mContext)
                .to(cls)
                .data(data)
                .launch();
    }

    @Override
    public void start() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destory() {

    }

    @Override
    public void visible(boolean flag, View view) {

    }

    @Override
    public void gone(boolean flag, View view) {

    }

    @Override
    public void inVisible(View view) {

    }


}
