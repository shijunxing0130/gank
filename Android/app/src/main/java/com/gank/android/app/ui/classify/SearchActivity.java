package com.gank.android.app.ui.classify;

import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.gank.android.app.R;
import com.gank.android.app.ui.adapter.GankRecyclerAdapter;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.detail.DetailActivity;
import com.gank.android.app.controller.GankHuoController;
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


/**
 * Created by shijunxing on 2017/10/12.
 */

public class SearchActivity extends BaseActivity<GankHuoController> implements GankRecyclerAdapter.OnListClickListener {

    @BindView(R.id.rv_search_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.et_search_content)
    EditText mEtKeyword;

    @BindView(R.id.iv_toolbar_clear)
    ImageView mIvClear;

    @BindView(R.id.tv_toolbar_right)
    View mBack;

    private GankRecyclerAdapter mListAdapter;

    private String mTag;
    private String mHint;
    private int mCurrentPage = 1;
    private boolean mIsLoadMore;
    public static final String TAG = "All";
    public static final String HINT = "搜索你想找的干货吧";

    @Override
    public void initVariables() {
        mTag = getIntent().getStringExtra(TAG);
        mHint = getIntent().getStringExtra(HINT);
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initList();
        initReFreshLayout();
        initSearchView();
        findViewById(R.id.ll_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    private void initReFreshLayout() {
        final SmartRefreshLayout smartRefreshLayout = getUiDelegate().initRefreshLayout(findViewById(R.id.rl_search), null);

        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mIsLoadMore = true;
                controller.getSearchList(SearchActivity.this, mEtKeyword.getText().toString(),mTag, ++mCurrentPage);
            }
        });

    }



    private void initList() {
        mListAdapter = new GankRecyclerAdapter(this, this);
        mRecyclerView.setLayoutManager(new FastSmoothScrollLinearLayoutManager(this));
        mRecyclerView.setAdapter(mListAdapter);

        mListAdapter.setShowTag(true);
    }

    private void initSearchView(){
        mEtKeyword.setHint(mHint);
        KeyboardUtils.showSoftInput(mEtKeyword);
        mEtKeyword.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    getSearchList();
                }
                return false;
            }
        });

        mEtKeyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mEtKeyword.getText().toString().equals("")) {
                    mIvClear.setVisibility(View.GONE);
                } else {
                    mIvClear.setVisibility(View.VISIBLE);
                }
            }
        });

        mIvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtKeyword.setText("");
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void getSearchList() {
        if (mEtKeyword.getText().toString().equals("")) {
            return;
        }

        getUiDelegate().showWaitDialog("玩命搜索中");
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.SEARCH_LIST, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().stopLoadMore();
                getUiDelegate().hideWaitDialog();
                if (event.isSuccess()) {
                    if (mIsLoadMore) {
                        mListAdapter.addData(controller.getIModel().getSearchList());
                    } else {
                        mListAdapter.setData(controller.getIModel().getSearchList());
                    }

                } else {
                    getUiDelegate().toast(event.getMsg());
                }
            }
        });
        controller.getSearchList(this,mEtKeyword.getText().toString(), mTag, mCurrentPage);
    }


    @Override
    public void getToDetail(String title, String link,GanHuoEntity ganHuoEntity) {
        Bundle bundle = new Bundle();
        bundle.putString(DetailActivity.ID, ganHuoEntity.get_id());
        bundle.putString(DetailActivity.TITLE, title);
        bundle.putString(DetailActivity.LINK, link);
        bundle.putString(DetailActivity.CONTENT, Convert.toJson(ganHuoEntity));
        getUiDelegate().startActivity(bundle, DetailActivity.class);
    }

    @Override
    public void goToImages(List<GanHuoEntity> urls, int position) {

    }


    @Override
    public void gotToClassify(String tag) {

    }


}
