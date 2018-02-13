package com.gank.android.app.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.controller.UserController;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.view.RongDivisionEditText;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;

import butterknife.BindView;
import cn.gank.androidlibs.kit.Kits;

/**
 * 登录
 * @author shijunxing
 * @date 2017/11/26
 */

public class LoginActivity extends BaseActivity<UserController> {


    @BindView(R.id.tv_login_register)
    TextView mTvLoginRegister;

    @BindView(R.id.et_login_phone)
    RongDivisionEditText mEtLoginPhone;

    @BindView(R.id.v_line_login_phone)
    View mVLineLoginPhone;

    @BindView(R.id.et_login_pwd)
    EditText mEtLoginPwd;

    @BindView(R.id.v_line_login_pwd)
    View mVLineLoginPwd;

    @BindView(R.id.tv_login_forget_pwd)
    TextView mTvLoginForgetPwd;

    @BindView(R.id.ll_login_into)
    LinearLayout mLlLoginInto;


    @BindView(R.id.cb_login_change_pw)
    CheckBox mCbChangePw;

    @BindView(R.id.iv_login_clear_phone)
    ImageButton mIvClearPhone;

    @BindView(R.id.iv_login_clear_pw)
    ImageButton mIvClearPw;

    private boolean isPhoneValid;
    private boolean isPwdValid;

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar("");
        initInput();
        mTvLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUiDelegate().startActivity(RegisterActivity.class);
            }
        });
        mTvLoginForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPhoneValid) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ForgetPwdActivity.PHONE,mEtLoginPhone.getText().toString());
                    getUiDelegate().startActivity(bundle,ForgetPwdActivity.class);
                }else {
                    getUiDelegate().toast("请填写正确的手机号");
                }
            }
        });
        mLlLoginInto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPhoneValid && isPwdValid) {
                    login(mEtLoginPhone.getContent(),mEtLoginPwd.getText().toString());
                }
            }
        });
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }



    private void initInput(){
        mEtLoginPhone.setRongDivisionEditTextWatcher(new RongDivisionEditText.RongDivisionEditTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String phone = mEtLoginPhone.getContent();
                if (!phone.equals("")) {
                    mIvClearPhone.setVisibility(View.VISIBLE);
                    if (phone.length() == 11 ){
                        if (Kits.checkPhone(phone)) {
                            isPhoneValid = true;
                        }else {
                            isPhoneValid = false;
                            getUiDelegate().toast("手机号码格式有误");
                        }
                    }else {
                        isPhoneValid = false;
                    }
                } else {
                    mIvClearPhone.setVisibility(View.GONE);
                    isPhoneValid = false;
                }

                checkPhoneAndPwd();
            }
        });

        mEtLoginPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mVLineLoginPhone.setBackgroundColor(getResources().getColor(hasFocus?R.color.colorThemeGolden:R.color.colorThemeGray5));
            }
        });

        mEtLoginPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mVLineLoginPwd.setBackgroundColor(getResources().getColor(hasFocus?R.color.colorThemeGolden:R.color.colorThemeGray5));
            }
        });

        mEtLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                String pw = mEtLoginPwd.getText().toString();
                if (!pw.equals("")) {
                    mIvClearPw.setVisibility(View.VISIBLE);
                    isPwdValid = true;
                } else {
                    mIvClearPw.setVisibility(View.GONE);
                    isPwdValid = false;
                }

                checkPhoneAndPwd();
        }});

        mCbChangePw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mEtLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mEtLoginPwd.setSelection(mEtLoginPwd.getText().length());
                } else {
                    mEtLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mEtLoginPwd.setSelection(mEtLoginPwd.getText().length());
                }
            }
        });
        mIvClearPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtLoginPwd.setText("");
            }
        });
        mIvClearPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtLoginPhone.setText("");
            }
        });
    }


    private boolean checkPhoneAndPwd(){
        if (isPhoneValid && isPwdValid){
            mLlLoginInto.setBackgroundResource(R.drawable.circle_golden_bg);
        }else {
            mLlLoginInto.setBackgroundResource(R.drawable.circle_gray_bg);
        }

        return isPhoneValid && isPwdValid;
    }

    private void login(String name, String pwd){
        getUiDelegate().showWaitDialog("登录中");
        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.USER_LOGIN, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().hideWaitDialog();
                getUiDelegate().toast(event.getMsg());
                if (event.isSuccess()) {
                    back();
                }
            }

        });
        controller.login(this,name,pwd);
    }
}
