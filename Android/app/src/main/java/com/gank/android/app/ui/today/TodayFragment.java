package com.gank.android.app.ui.today;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gank.android.app.R;
import com.gank.android.app.aouth.User;
import com.gank.android.app.controller.GankHuoController;
import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.even.BaseEven;
import com.gank.android.app.even.EvenBusTag;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.adapter.GankRecyclerAdapter;
import com.gank.android.app.ui.adapter.TodayFragmentImageHolder;
import com.gank.android.app.ui.base.BaseFragment;
import com.gank.android.app.ui.classify.ClassifyListActivity;
import com.gank.android.app.ui.classify.ImageActivity;
import com.gank.android.app.ui.detail.DetailActivity;
import com.gank.android.app.view.FastSmoothScrollLinearLayoutManager;
import com.gank.android.app.view.OnRecycleViewScrollListener;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.DataEventListener;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;
import com.scwang.smartrefresh.layout.internal.ProgressDrawable;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.gank.androidlibs.event.IBus;
import cn.gank.androidlibs.httphelper.Convert;
import cn.gank.androidlibs.httphelper.Result;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * Created by shijunxing on 2017/9/2
 *
 * @author shijunxing
 */

public class TodayFragment extends BaseFragment<GankHuoController> implements GankRecyclerAdapter.OnListClickListener,TodayFragmentImageHolder.OnListClickListener {

    @BindView(R.id.ll_toolbar)
    View mToolBar;

    @BindView(R.id.rv_today)
    RecyclerView mRecyclerView;

    @BindView(R.id.iv_toolbar_new)
    ImageView ivToolbarNew;

    @BindView(R.id.iv_toolbar_new_draw)
    ImageView ivToolbarNewDraw;

    @BindView(R.id.iv_toolbar_calendar)
    ImageView ivToolbarCalendar;

    @BindView(R.id.iv_toolbar_calendar_draw)
    ImageView ivToolbarCalendarDraw;

    private TodayFragmentImageHolder mImageVH;

    private ProgressDrawable mProgressDrawable;

    private GankRecyclerAdapter mListAdapter;

    private List<String> mHistoryList;

    private boolean isFirstLoad = true;


    @Override
    public void initVariables() {
        mHistoryList = new ArrayList<>();
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initList();
        initToolbar(view);
        initStatueView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        hasGank();
    }

    @Override
    public void loadData() {
        getHistory();
        isFirstLoad = false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_today;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * @param event
     */
    @Subscribe
    public void handlerEven(IBus.IEvent event) {
        if (event.getTag() == EvenBusTag.HISTORY_DATE) {
            showProgressDrawable(ivToolbarCalendar, ivToolbarCalendarDraw);
            controller.getDailyList(getContext(), ((BaseEven) event).data);
        }
    }


    private void initToolbar(View view) {
        ivToolbarNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDrawable(ivToolbarNew, ivToolbarNewDraw);
                getHistory();
            }
        });

        ivToolbarCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHistoryList != null && mHistoryList.size() != 0) {
                    Bundle data = new Bundle();
                    data.putString(HistoryActivity.HISTORY_LIST, Convert.toJson(mHistoryList));
                    getUiDelegate().startActivity(data, HistoryActivity.class);
                }
            }
        });

        view.findViewById(R.id.ll_toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

    }


    private void initStatueView(View view) {
        getUiDelegate().initPageStatueManager(view);
        getUiDelegate().setLoadingResource(R.layout.layout_today_loading);
        getUiDelegate().showLoadingStatue();
    }

    private void initList() {
        mRecyclerView.setHasFixedSize(true);
        mListAdapter = new GankRecyclerAdapter(getContext(), this);
        mRecyclerView.setLayoutManager(new FastSmoothScrollLinearLayoutManager(getContext()));
        mImageVH = new TodayFragmentImageHolder(inflate(mRecyclerView,R.layout.item_today_list_image),this);
        mListAdapter.setParallaxHeader(mImageVH.getView(), mRecyclerView);
        mRecyclerView.setAdapter(mListAdapter);
        mToolBar.setAlpha(0);
        mRecyclerView.addOnScrollListener(new OnRecycleViewScrollListener(getContext()){
            @Override
            public void onTranslucent(float alpha) {
                mToolBar.setAlpha(1 - alpha);
            }
        });
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    private void hasGank(){
        ivToolbarNew.setVisibility(User.isHasGankHuo()?View.VISIBLE:View.GONE);
        if (User.isHasGankHuo() && !isFirstLoad) {
            showProgressDrawable(ivToolbarNew, ivToolbarNewDraw);
            getHistory();
            User.setHasGankHuo(false);
        }
    }

    private void getTodayList(String date) {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.TODAY_LIST, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                hideProgressDrawable(ivToolbarNew, ivToolbarNewDraw);
                hideProgressDrawable(ivToolbarCalendar, ivToolbarCalendarDraw);
                getUiDelegate().showContentStatue();
                if (event.isSuccess()) {
                    mImageVH.setData(controller.getIModel().getImageEntity());
                    mListAdapter.setData(controller.getIModel().getDailyList());
                    mRecyclerView.smoothScrollToPosition(0);
                } else {
                    getUiDelegate().showCommonDialog("今日干货未更新，有新干货会第一时间推送给你~");
                }
            }


        });
        controller.getDailyList(getContext(), date);
    }

    private void getHistory() {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.HISTORY_LIST, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                if (event.isSuccess()) {
                    mHistoryList.addAll(controller.getIModel().getHistoryList());
                    getTodayList(mHistoryList.get(0));
                } else {
                    getUiDelegate().toast(event.getMsg());
                }
            }

        });
        controller.getHistory(getContext());
    }


    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(getContext()).inflate(layoutRes, parent, false);
    }


    @Override
    public void getToDetail(String title, String link, GanHuoEntity ganHuoEntity) {
        Bundle bundle = new Bundle();
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
    public void goToHistory() {
        if (mHistoryList != null && mHistoryList.size() != 0) {
            Bundle data = new Bundle();
            data.putString(HistoryActivity.HISTORY_LIST, Convert.toJsonWithFilter(mHistoryList));
            getUiDelegate().startActivity(data, HistoryActivity.class);
        }
    }

    @Override
    public void gotToClassify(String tag) {
        Bundle bundle = new Bundle();
        bundle.putString(ClassifyListActivity.TAG, tag);
        getUiDelegate().startActivity(bundle, ClassifyListActivity.class);
    }


    public void showProgressDrawable(ImageView imageView, ImageView draw) {
        mProgressDrawable = new ProgressDrawable();
        mProgressDrawable.setColor(getResources().getColor(R.color.white));
        draw.setImageDrawable(mProgressDrawable);
        imageView.setVisibility(View.GONE);
        draw.setVisibility(View.VISIBLE);
        mProgressDrawable.start();
    }

    public void hideProgressDrawable(ImageView imageView, ImageView draw) {
        imageView.setVisibility(View.VISIBLE);
        draw.setVisibility(View.GONE);
        if (mProgressDrawable != null) {
            mProgressDrawable.stop();
        }
    }

}
