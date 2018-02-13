package com.gank.android.app.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.controller.UserController;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.view.RongDivisionEditText;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;
import com.jkb.vcedittext.VerificationAction;
import com.jkb.vcedittext.VerificationCodeEditText;

import butterknife.BindView;
import cn.gank.androidlibs.kit.Kits;

/**
 * 忘记密码
 *
 * @author shijunxing
 * @date 2017/11/26
 */

public class ForgetPwdActivity extends BaseActivity<UserController> {

    @BindView(R.id.iv_forget_pwd_close)
    ImageView mIvForgetPwdClose;

    @BindView(R.id.tv_forget_tip)
    TextView mTvForgetTip;

    @BindView(R.id.et_forget_pwd)
    EditText mEtForgetPwd;

    @BindView(R.id.iv_forget_clear_pwd)
    ImageButton mIvForgetClearPwd;

    @BindView(R.id.v_line_forget_pwd)
    View mVLineForgetPwd;

    @BindView(R.id.et_forget_code)
    VerificationCodeEditText mEtForgetCode;

    @BindView(R.id.tv_forget_get_code)
    TextView mTvForgetGetCode;

    @BindView(R.id.tv_forget_done)
    TextView mTvForgetDone;

    private boolean isPwdValid;
    private boolean isCodeValid;

    private String mPhone;

    public static final String PHONE = "forget_phone";


    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000, 1000);

    @Override
    public void initVariables() {
        mPhone = getIntent().getStringExtra(PHONE);
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar("");
        mTvForgetGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCountDownTimer.start();
                mTvForgetGetCode.setTextColor(getResources().getColor(R.color.colorThemeGray));
                getCode();
            }
        });
        mTvForgetDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPwdValid && isCodeValid) {
                    verifyCode();
                }
            }
        });
        mTvForgetTip.setText("验证码将发送至" + mPhone + "填写验证码并输入新的密码替代原来的密码");

        initInput();
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myCountDownTimer.cancel();
        myCountDownTimer.onFinish();
    }

    private void initInput() {
        mEtForgetPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                String pw = mEtForgetPwd.getText().toString();
                if (!pw.equals("")) {
                    mIvForgetClearPwd.setVisibility(View.VISIBLE);
                    isPwdValid = true;
                } else {
                    mIvForgetClearPwd.setVisibility(View.GONE);
                    isPwdValid = false;
                }

                checkPwdAndCode();
            }
        });

        mEtForgetPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mVLineForgetPwd.setBackgroundColor(getResources().getColor(hasFocus ? R.color.colorThemeGolden : R.color.colorThemeGray5));
            }
        });


        mEtForgetCode.setOnVerificationCodeChangedListener(new VerificationAction.OnVerificationCodeChangedListener() {
            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                if (mEtForgetCode.getText().length() == 6) {
                    isCodeValid = true;
                } else {
                    isCodeValid = false;
                }
                checkPwdAndCode();
            }

            @Override
            public void onInputCompleted(CharSequence s) {

            }
        });


        mIvForgetClearPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtForgetPwd.setText("");
            }
        });
    }

    private void updatePwd() {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.USER_UPDATE_PWD, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().hideWaitDialog();
                getUiDelegate().toast(event.getMsg());
                if (event.isSuccess()) {
                    finish();
                }
            }
        });
        controller.updatePwd(this,mPhone.replace(" ", ""),mEtForgetPwd.getText().toString());
    }

    private void getCode() {
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.USER_CODE, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().toast(event.getMsg());
                if (event.getStatus() != 0) {
                    myCountDownTimer.cancel();
                    myCountDownTimer.onFinish();
                }
            }
        });
        controller.getVerificationCode(mPhone.replace(" ", ""));
    }

    private void verifyCode(){
        getUiDelegate().showWaitDialog("密码修改中");
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.USER_VERIFY_CODE, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                if (event.isSuccess()) {
                    updatePwd();
                } else {
                    getUiDelegate().hideWaitDialog();
                    getUiDelegate().toast(event.getMsg());
                }
            }

        });
        controller.verifyCode(mPhone.replace(" ", ""),mEtForgetCode.getText().toString());
    }
    private void checkPwdAndCode() {
        if (isPwdValid && isCodeValid) {
            mTvForgetDone.setBackgroundResource(R.drawable.oval_btn_bg_golden);
        } else {
            mTvForgetDone.setBackgroundResource(R.drawable.oval_btn_bg_gray);
        }
        mTvForgetGetCode.setClickable(isPwdValid);
        mTvForgetGetCode.setTextColor(getResources().getColor(isPwdValid ? R.color.colorThemeBlack : R.color.colorThemeGray));
    }

    //复写倒计时
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            mTvForgetGetCode.setClickable(false);
            mTvForgetGetCode.setText(l / 1000 + "/s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            mTvForgetGetCode.setText("获取验证码");
            //设置可点击
            mTvForgetGetCode.setClickable(true);
            mTvForgetGetCode.setTextColor(getResources().getColor(R.color.colorThemeBlack));
        }
    }
}
