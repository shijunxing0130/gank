package com.gank.android.app.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.aouth.User;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.ui.dialog.GankHuoTypeDialog;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.controller.GankHuoController;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.DataEventListener;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;

import butterknife.BindView;
import cn.gank.androidlibs.httphelper.Result;

/**
 * 提交干货
 * @author shijunxing
 * @date 2017/10/17
 */

public class CommitActivity extends BaseActivity<GankHuoController> {

    @BindView(R.id.tv_commit_commit)
    TextView mTvCommitCommit;

    @BindView(R.id.et_commit_type)
    EditText mEtCommitType;

    @BindView(R.id.et_commit_url)
    EditText mEtCommitUrl;

    @BindView(R.id.et_commit_dec)
    EditText mEtCommitDec;

    @BindView(R.id.et_commit_who)
    EditText mEtCommitWho;

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar("提交干货",false);
        mEtCommitType.setFocusable(false);
        mEtCommitType.setFocusableInTouchMode(false);
        mEtCommitType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showDialog();
            }
        });
        mEtCommitWho.setText(User.getName());
        mTvCommitCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_commit;
    }

    private void showDialog() {
        final GankHuoTypeDialog typeDialog = new GankHuoTypeDialog();
        typeDialog.setListener(new GankHuoTypeDialog.OnDialogSelectListener() {
                                   @Override
                                   public void onDialogSelect(String type) {
                                       mEtCommitType.setText(type);
                                   }
                               }
        );
        typeDialog.show(getSupportFragmentManager());
    }

    private void commit(){
        getUiDelegate().showWaitDialog("提交中");
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.ADD, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().hideWaitDialog();
                getUiDelegate().toast(event.getMsg());
            }

        });
        controller.addGankHuo(this,
                mEtCommitType.getText().toString(),
                mEtCommitUrl.getText().toString(),
                mEtCommitDec.getText().toString(),
                mEtCommitWho.getText().toString());

    }
}
