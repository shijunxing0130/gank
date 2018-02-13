package cn.gank.androidlibs.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;
import cn.gank.androidlibs.event.BusFactory;
import cn.gank.androidlibs.log.XLog;

/**
 * Created by shijunxing on 2017/11/27
 */

public abstract class XActivity extends AutoLayoutActivity implements UiCallback{
    protected Activity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        BaseAppManager.getInstance().addActivity(this);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        }
        initVariables();
        initViews(null,savedInstanceState);
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BusFactory.getBus().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
        XLog.d("base","removeActivity");
    }




}
