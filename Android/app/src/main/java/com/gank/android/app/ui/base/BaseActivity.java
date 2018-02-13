package com.gank.android.app.ui.base;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.ui.mine.CollectListActivity;
import com.gank.android.mvc.BaseController;
import com.gank.android.mvc.MVCActivity;
import com.jaeger.library.StatusBarUtil;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 * Created by sjx on 2016/12/1
 */
public abstract class BaseActivity<TC extends BaseController> extends MVCActivity<TC> implements BGASwipeBackHelper.Delegate{

    protected UiDelegate uiDelegate;
    protected BGASwipeBackHelper mSwipeBackHelper;
    private int statusBarColor = R.color.colorThemeBlack;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        setStatusBarColor(getResources().getColor(statusBarColor));
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUiDelegate().start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUiDelegate().resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getUiDelegate().pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getUiDelegate().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getUiDelegate().destory();
    }

    /**
     * 代替finish
     */
    protected void back(){
        mSwipeBackHelper.backward();
    }


    protected UiDelegate getUiDelegate() {
        if (uiDelegate == null) {
            uiDelegate = UiDelegateBase.create(this);
        }
        return uiDelegate;
    }

    public BGASwipeBackHelper getSwipeBackHelper() {
        return mSwipeBackHelper;
    }


    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        mSwipeBackHelper.backward();
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(@ColorInt int color) {
//        setStatusBarColor(color, 0);
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    public void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);
    }


    public void initToolBar(String s){
        initToolBar(s,true);
    }

    public void initToolBar(String s,boolean isShowRight){
        if (findViewById(R.id.ll_toolbar_left) != null){
            final View back = findViewById(R.id.ll_toolbar_left);
            final View right = findViewById(R.id.ll_toolbar_right);
            TextView title = (TextView) findViewById(R.id.tv_toolbar_title);
            if (title != null) {
                title.setText(s);
            }
            if (back != null) {
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back();
                    }
                });
            }
            if (right != null) {
                right.setVisibility(isShowRight?View.VISIBLE:View.GONE);
            }

        }

    }

    protected void addFragment(int containerViewId, Fragment fragment , String tag) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(containerViewId, fragment , tag);
        fragmentTransaction.commit();
    }
}
