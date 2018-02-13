package com.gank.android.app.ui.mine;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.controller.CollectController;
import com.gank.android.app.ui.adapter.GankRecyclerAdapter;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.adapter.CollectMenuAdapter;
import com.gank.android.app.ui.detail.DetailActivity;
import com.gank.android.app.view.CustomPopWindow;
import com.gank.android.app.view.FastSmoothScrollLinearLayoutManager;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.DataEventListener;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import cn.gank.androidlibs.httphelper.Convert;
import cn.gank.androidlibs.httphelper.Result;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * @author shijunxing
 * @date 2017/10/12
 */

public class CollectListActivity extends BaseActivity<CollectController>
        implements GankRecyclerAdapter.OnListClickListener, GankRecyclerAdapter.OnListDelecteListener {

    @BindView(R.id.rv_collect_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;

    @BindView(R.id.ll_toolbar_right)
    View mllToolbarRight;

    private CustomPopWindow mCustomPopWindow;

    private GankRecyclerAdapter mListAdapter;

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initStatueView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mListAdapter = new GankRecyclerAdapter(this, this));
        mListAdapter.setShowCancel(true);
        mListAdapter.setShowTag(true);
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        initToolBar("我的收藏");
        mllToolbarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu();
            }
        });
        findViewById(R.id.ll_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

    }

    @Override
    public void loadData() {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.GET_COLLECTS, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                if (event.isSuccess() && controller.getIModel().getCollectsByTag("All") != null ) {
                    getUiDelegate().showContentStatue();
                    mListAdapter.setData(controller.getIModel().getCollectsByTag("All"));
                } else {
                    getUiDelegate().showEmptyStatue();
                }
            }
        });
        controller.getCollects(this, 1, 20);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_collect;
    }


    @Override
    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setTransparentForImageView(CollectListActivity.this, null);
    }

    @Override
    public void getToDetail(String title, String link, GanHuoEntity ganHuoEntity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DetailActivity.SHOWCOLLECT, false);
        bundle.putString(DetailActivity.TITLE, title);
        bundle.putString(DetailActivity.LINK, link);
        bundle.putString(DetailActivity.CONTENT, Convert.toJson(ganHuoEntity));
        getUiDelegate().startActivity(bundle, DetailActivity.class);
    }

    @Override
    public void goToImages(List<GanHuoEntity> ganks, int position) {

    }

    @Override
    public void gotToClassify(String tag) {

    }

    private void initStatueView() {
        getUiDelegate().initPageStatueManager(findViewById(R.id.rl_collect));
        getUiDelegate().setLoadingResource(R.layout.layout_list_loading);
        getUiDelegate().setEmptyResource(R.layout.base_empty_title);
        TextView title = (TextView) getUiDelegate().showLoadingStatue().findViewById(R.id.tv_toolbar_title);
        title.setText("我的收藏");
    }

    private void showPopMenu() {
        if (mCustomPopWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.layout_pop_gank, null);
            //处理popWindow 显示内容
            handleLogic(contentView);
            //创建并显示popWindow
            mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(this)
                    .setView(contentView)
                    .create();
        }

        mCustomPopWindow.showAsDropDown(mllToolbarRight, -100, 3);
    }

    private void handleLogic(View contentView) {

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.rv_collect_menu);
        CollectMenuAdapter mMenuAdapter = new CollectMenuAdapter(new CollectMenuAdapter.OnListClickListener() {
            @Override
            public void toClassifyList(String tag) {
                mListAdapter.setData(controller.getIModel().getCollectsByTag(tag));
                mCustomPopWindow.dissmiss();
            }
        });
        mMenuAdapter.setList(controller.getIModel().getCollectsTag());
        recyclerView.setLayoutManager(new FastSmoothScrollLinearLayoutManager(this));
        recyclerView.setAdapter(mMenuAdapter);
    }


    @Override
    public void onDelete(String uid, final GanHuoEntity ganHuoEntity) {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.UN_COLLECT, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                if (event.isSuccess()) {
                    mListAdapter.removeItem(ganHuoEntity);
                } else {
                    getUiDelegate().toast(event.getMsg());
                }
            }

        });
        controller.unCollect(this, uid);
    }
}
