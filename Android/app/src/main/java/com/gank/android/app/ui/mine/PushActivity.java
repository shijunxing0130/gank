package com.gank.android.app.ui.mine;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.gank.android.app.GankApp;
import com.gank.android.app.R;
import com.gank.android.app.aouth.User;
import com.gank.android.app.ui.base.BaseActivity;
import com.kyleduo.switchbutton.SwitchButton;

import butterknife.BindView;
import cn.bmob.push.BmobPush;

/**
 * 推送
 *
 * @author shijunxing
 * @date 2017/11/14
 */

@SuppressLint("Registered")
public class PushActivity extends BaseActivity {


    @BindView(R.id.sb_push)
    SwitchButton mSbPush;

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar("干货推送",false);
        mSbPush.setChecked(User.isPush());
        mSbPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BmobPush.startWork(GankApp.getContext());
                }else {
                    BmobPush.stopWork();
                }
                User.setIsPush(isChecked);
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_push;
    }
}
