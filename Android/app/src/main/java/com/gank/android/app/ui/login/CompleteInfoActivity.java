package com.gank.android.app.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.controller.UserController;
import com.gank.android.app.even.BaseEven;
import com.gank.android.app.even.EvenBusTag;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.DataEventListener;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import cn.gank.androidlibs.event.IBus;
import cn.gank.androidlibs.httphelper.Result;

/**
 * 完善信息
 *
 * @author shijunxing
 * @date 2017/11/26
 */

public class CompleteInfoActivity extends BaseActivity<UserController> {

    @BindView(R.id.iv_login_close)
    ImageView mIvLoginClose;

    @BindView(R.id.et_complete_name)
    EditText mEtCompleteName;

    @BindView(R.id.v_line_complete_name)
    View mVLineCompleteName;

    @BindView(R.id.et_complete_pwd)
    EditText mEtCompletePwd;

    @BindView(R.id.iv_complete_clear_pw)
    ImageButton mIvCompleteClearPw;

    @BindView(R.id.v_line_complete_pwd)
    View mVLineCompletePwd;

    @BindView(R.id.tv_complete_done)
    TextView mTvCompleteDone;

    private boolean isNameValid;
    private boolean isPwdValid;

    private String mPhone;

    public static final String PHONE = "register_phone";

    @Override
    public void initVariables() {
        mPhone = getIntent().getStringExtra(PHONE);
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        mTvCompleteDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameValid && isPwdValid) {
                    register();
                }
            }
        });
        initInput();

    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_complete;
    }


    private void initInput() {
        mEtCompleteName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isNameValid = !"".equals(mEtCompleteName.getText().toString());
                checkPhoneAndPwd();
            }
        });

        mEtCompleteName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mVLineCompleteName.setBackgroundColor(getResources().getColor(hasFocus ? R.color.colorThemeGolden : R.color.colorThemeGray5));
            }
        });

        mEtCompletePwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mVLineCompletePwd.setBackgroundColor(getResources().getColor(hasFocus ? R.color.colorThemeGolden : R.color.colorThemeGray5));
            }
        });

        mEtCompletePwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                String pw = mEtCompletePwd.getText().toString();
                if (!"".equals(pw)) {
                    mIvCompleteClearPw.setVisibility(View.VISIBLE);
                    isPwdValid = true;
                } else {
                    mIvCompleteClearPw.setVisibility(View.GONE);
                    isPwdValid = false;
                }
                checkPhoneAndPwd();
            }
        });


        mIvCompleteClearPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtCompletePwd.setText("");
            }
        });
    }

    private void checkPhoneAndPwd() {
        if (isNameValid && isPwdValid) {
            mTvCompleteDone.setBackgroundResource(R.drawable.oval_btn_bg_golden);
        } else {
            mTvCompleteDone.setBackgroundResource(R.drawable.oval_btn_bg_gray);
        }
    }

    private void register() {
        getUiDelegate().showWaitDialog("注册中");
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.USER_REGISTER, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().hideWaitDialog();
                getUiDelegate().toast(event.getMsg());
                if (event.isSuccess()) {
                    EventBus.getDefault().post(new BaseEven(EvenBusTag.USER_REGISTER));
                    back();
                }
            }
        });
        controller.register(this,mPhone,mEtCompleteName.getText().toString(),mEtCompletePwd.getText().toString());
    }

}
