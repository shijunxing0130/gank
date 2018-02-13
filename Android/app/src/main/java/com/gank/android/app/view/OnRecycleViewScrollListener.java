package com.gank.android.app.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import cn.gank.androidlibs.log.XLog;

/**
 * @author shijunxing
 * @date 2017/12/1
 */

public abstract class OnRecycleViewScrollListener extends RecyclerView.OnScrollListener {

    private Context mContext;

    private int distanceY;

    public OnRecycleViewScrollListener(Context context) {
        mContext = context;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        distanceY += dy;
        float heightPixels = mContext.getResources().getDisplayMetrics().heightPixels;
        // 0~1  透明度是1~0，这里选择的screenHeight的1/3 是alpha改变的速率 （根据你的需要你可以自己定义）
        float alpha = 1 - distanceY / (heightPixels / 3);
        onTranslucent(alpha);
        XLog.d("OnRecycleViewScrollListener", distanceY + "");
    }

    /**
     * 透明度回调
     * @param alpha
     */
    public abstract void  onTranslucent(float alpha);
}
