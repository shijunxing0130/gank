package com.gank.android.app.ui.classify;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.ui.adapter.GankRecyclerAdapter;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.controller.ClassifyLisController;
import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.detail.DetailActivity;
import com.gank.android.app.view.FastSmoothScrollLinearLayoutManager;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.DataEventListener;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.List;

import butterknife.BindView;
import cn.gank.androidlibs.httphelper.Convert;
import cn.gank.androidlibs.httphelper.Result;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 分类
 *
 * @author shijunxing
 * @date 2017/9/8
 */

public class ClassifyListActivity extends BaseActivity<ClassifyLisController> implements GankRecyclerAdapter.OnListClickListener {

    @BindView(R.id.rv_classify_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.iv_toolbar_search)
    View mViewSearch;

    private GankRecyclerAdapter mListAdapter;

    private String mTag;
    private int mCurrentPage = 1;
    private boolean mIsLoadMore;
    public static final String TAG = "tag";

    @Override
    public void initVariables() {
        mTag = getIntent().getStringExtra(TAG);
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar(mTag);
        initList();
        initReFreshLayout();
        initStatueView();

        findViewById(R.id.ll_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

        mViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(SearchActivity.TAG, mTag);
                bundle.putString(SearchActivity.HINT, "搜索你想找的" + mTag + "干货吧");
                getUiDelegate().startActivity(bundle, SearchActivity.class);
            }
        });
    }

    @Override
    public void loadData() {
        getTodayList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_classify_list;
    }

    @Override
    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setTransparentForImageView(ClassifyListActivity.this, null);
    }

    private void initReFreshLayout() {
        final SmartRefreshLayout smartRefreshLayout = getUiDelegate().initRefreshLayout(findViewById(R.id.rl_classify_list), null);

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mIsLoadMore = true;
                controller.getClassifyList(ClassifyListActivity.this, mTag, ++mCurrentPage);
            }
        });

        smartRefreshLayout.setEnableRefresh(false);
    }

    private void initStatueView() {
        getUiDelegate().initPageStatueManager(findViewById(R.id.rl_classify_list));
        if (mTag.equals("福利")) {
            getUiDelegate().setLoadingResource(R.layout.layout_image_loading);
            mViewSearch.setVisibility(View.GONE);
        } else {
            getUiDelegate().setLoadingResource(R.layout.layout_list_loading);
            mViewSearch.setVisibility(View.VISIBLE);
        }
        TextView title = (TextView) getUiDelegate().showLoadingStatue().findViewById(R.id.tv_toolbar_title);
        title.setText(mTag);
    }

    private void initList() {
        mListAdapter = new GankRecyclerAdapter(this, this);
        if ("all".equals(mTag)) {
            mListAdapter.setShowTag(true);
        }
        if ("福利".equals(mTag)) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new FastSmoothScrollLinearLayoutManager(this));
        }
        mRecyclerView.setAdapter(mListAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    private void getTodayList() {
        String receiverIDCard = getReceiverIDCard();
        EventDispatcherUtils.addListener(receiverIDCard, RequestEventID.CLASSIFY_LIST, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().stopRefreshing();
                getUiDelegate().stopLoadMore();
                if (event.isSuccess()) {
                    getUiDelegate().showContentStatue();
                    if (mIsLoadMore) {
                        mListAdapter.addData(controller.getIModel().getClassifyLis());
                    } else {
                        mListAdapter.setData(controller.getIModel().getClassifyLis());
                    }

                } else {
                    getUiDelegate().showEmptyStatue();
                    getUiDelegate().toast(event.getMsg());
                }
            }
        });
        controller.getClassifyList(this, mTag, mCurrentPage);
    }


    @Override
    public void getToDetail(String title, String link, GanHuoEntity ganHuoEntity) {
        Bundle bundle = new Bundle();
        bundle.putString(DetailActivity.ID, ganHuoEntity.get_id());
        bundle.putString(DetailActivity.TITLE, title);
        bundle.putString(DetailActivity.LINK, link);
        bundle.putString(DetailActivity.CONTENT, Convert.toJsonWithFilter(ganHuoEntity));
        getUiDelegate().startActivity(bundle, DetailActivity.class);
    }

    @Override
    public void goToImages(List<GanHuoEntity> urls, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(ImageActivity.URLS, Convert.toJson(urls));
        bundle.putInt(ImageActivity.POSITION, position);
        getUiDelegate().startActivity(bundle, ImageActivity.class);
    }


    @Override
    public void gotToClassify(String tag) {

    }


}
