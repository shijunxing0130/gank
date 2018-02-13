package com.gank.android.app.ui.base;

import com.gank.android.mvc.BaseController;
import com.gank.android.mvc.MVCFragment;



/** fragment基类
 * Created on 2016/12/13
 * @author shijunxing
 */
public abstract class BaseFragment<TC extends BaseController> extends MVCFragment<TC> {

    protected UiDelegate uiDelegate;

    @Override
    public void onStart() {
        super.onStart();
        getUiDelegate().start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getUiDelegate().resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        getUiDelegate().pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        getUiDelegate().stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getUiDelegate().destory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getUiDelegate().destory();
    }

    protected UiDelegate getUiDelegate() {
        if (uiDelegate == null) {
            uiDelegate = UiDelegateBase.create(getContext());
        }
        return uiDelegate;
    }



}
