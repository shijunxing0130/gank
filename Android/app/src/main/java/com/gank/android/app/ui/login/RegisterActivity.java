package com.gank.android.app.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.controller.UserController;
import com.gank.android.app.even.EvenBusTag;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.view.RongDivisionEditText;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.DataEventListener;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;
import com.jkb.vcedittext.VerificationAction;
import com.jkb.vcedittext.VerificationCodeEditText;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import cn.gank.androidlibs.event.IBus;
import cn.gank.androidlibs.httphelper.Result;
import cn.gank.androidlibs.kit.Kits;

/**
 * 注册
 * @author shijunxing
 * @date 2017/11/26
 */

public class RegisterActivity extends BaseActivity<UserController> {


    @BindView(R.id.iv_login_close)
    ImageView mIvLoginClose;

    @BindView(R.id.et_register_phone)
    RongDivisionEditText mEtRegisterPhone;

    @BindView(R.id.iv_register_clear_phone)
    ImageButton mIvRegisterClearPhone;

    @BindView(R.id.v_line_login_phone)
    View mVLineLoginPhone;

    @BindView(R.id.et_register_code)
    VerificationCodeEditText mEtRegisterCode;

    @BindView(R.id.tv_register_get_code)
    TextView mTvRegisterGetCode;

    @BindView(R.id.ll_register_into)
    LinearLayout mLlRegisterInto;

    private boolean isPhoneValid;
    private boolean isCodeValid;

    final MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar("");
        initInput();
        mTvRegisterGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPhoneValid) {
                    myCountDownTimer.start();
                    mTvRegisterGetCode.setTextColor(getResources().getColor(R.color.colorThemeGray));
                    getCode();
                }

            }
        });
        mLlRegisterInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPhoneValid && isCodeValid) {
                    verifyCode();
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe
    public void handlerEven(IBus.IEvent event) {
        if (event.getTag() == EvenBusTag.USER_REGISTER) {
            back();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myCountDownTimer.cancel();
        myCountDownTimer.onFinish();
    }
    private void initInput() {

        mEtRegisterPhone.setRongDivisionEditTextWatcher(new RongDivisionEditText.RongDivisionEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String phone = mEtRegisterPhone.getContent();
                if (!phone.equals("")) {
                    mIvRegisterClearPhone.setVisibility(View.VISIBLE);
                    if (phone.length() == 11) {
                        if (Kits.checkPhone(phone)) {
                            isPhoneValid = true;
                        } else {
                            isPhoneValid = false;
                            getUiDelegate().toast("手机号码格式有误");
                        }
                    } else {
                        isPhoneValid = false;
                    }
                } else {
                    mIvRegisterClearPhone.setVisibility(View.GONE);
                    isPhoneValid = false;
                }

                checkPhoneAndCode();
            }
        });

        mEtRegisterPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mVLineLoginPhone.setBackgroundColor(getResources().getColor(hasFocus ? R.color.colorThemeGolden : R.color.colorThemeGray5));
            }
        });


        mEtRegisterCode.setOnVerificationCodeChangedListener(new VerificationAction.OnVerificationCodeChangedListener() {
            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                if (mEtRegisterCode.getText().length() == 6) {
                    isCodeValid = true;
                }else {
                    isCodeValid = false;
                }
                checkPhoneAndCode();
            }

            @Override
            public void onInputCompleted(CharSequence s) {

            }
        });


        mIvRegisterClearPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtRegisterPhone.setText("");
            }
        });
    }


    private void checkPhoneAndCode() {
        if (isPhoneValid && isCodeValid) {
            mLlRegisterInto.setBackgroundResource(R.drawable.circle_golden_bg);
        } else {
            mLlRegisterInto.setBackgroundResource(R.drawable.circle_gray_bg);
        }
        mTvRegisterGetCode.setClickable(isPhoneValid);
        mTvRegisterGetCode.setTextColor(getResources().getColor(isPhoneValid?R.color.colorThemeBlack:R.color.colorThemeGray));

    }


    private void getCode(){
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
        controller.getVerificationCode(mEtRegisterPhone.getContent());
    }

     private void verifyCode(){
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.USER_VERIFY_CODE, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                if (event.isSuccess()) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CompleteInfoActivity.PHONE,mEtRegisterPhone.getContent());
                    getUiDelegate().startActivity(bundle,CompleteInfoActivity.class);
                } else {
                    getUiDelegate().toast(event.getMsg());
                }
            }

        });
        controller.verifyCode(mEtRegisterPhone.getContent(),mEtRegisterCode.getText().toString());
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
            mTvRegisterGetCode.setClickable(false);
            mTvRegisterGetCode.setText(l/1000+"/s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            mTvRegisterGetCode.setText("获取验证码");
            //设置可点击
            mTvRegisterGetCode.setClickable(true);
            mTvRegisterGetCode.setTextColor(getResources().getColor(R.color.colorThemeBlack));
        }
    }
}
