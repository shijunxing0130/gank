package cn.gank.androidlibs.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.gank.androidlibs.event.BusFactory;

/**
 * Created by wanglei on 2016/11/27.
 */

public abstract class XFragment extends Fragment implements UiCallback {
    protected View rootView;
    protected LayoutInflater layoutInflater;
    protected Activity context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
        initVariables();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        if (rootView == null) {
            if (getLayoutId() != 0){
                rootView = inflater.inflate(getLayoutId(), null);
                ButterKnife.bind(this,rootView);
                initViews(rootView,savedInstanceState);
            }
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(rootView);
            }
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        BusFactory.getBus().unregister(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }



}
