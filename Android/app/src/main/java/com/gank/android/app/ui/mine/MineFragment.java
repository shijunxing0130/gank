package com.gank.android.app.ui.mine;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.aouth.User;
import com.gank.android.app.controller.UserController;
import com.gank.android.app.even.EvenBusTag;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.ui.base.BaseFragment;
import com.gank.android.app.ui.detail.DetailActivity;
import com.gank.android.app.ui.detail.WebActivity;
import com.gank.android.app.ui.dialog.SelectImageDialog;
import com.gank.android.app.ui.login.LoginActivity;
import com.gank.android.mvc.events.BaseEvent;
import com.gank.android.mvc.events.EventDispatcherUtils;
import com.gank.android.mvc.events.NoticeEventListener;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import cn.gank.androidlibs.event.IBus;
import cn.gank.androidlibs.image.loader.ImageLoader;
import cn.gank.androidlibs.kit.Kits;
import cn.gank.androidlibs.log.XLog;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * @author shijunxing
 * @date 2017/9/2
 */

public class MineFragment extends BaseFragment<UserController> {

    @BindView(R.id.tv_mine_name)
    TextView mTvMineName;

    @BindView(R.id.iv_toolbar_new)
    ImageView mIvAdd;

    @BindView(R.id.iv_mine_head)
    ImageView mIvMineHead;

    @BindView(R.id.rl_mine_info)
    RelativeLayout mRlMineInfo;

    @BindView(R.id.rl_mine_notify)
    RelativeLayout mRlMineNotify;

    @BindView(R.id.rl_mine_thank)
    RelativeLayout mRlMineThank;

    @BindView(R.id.rl_mine_about)
    RelativeLayout mRlMineAbout;

    @BindView(R.id.rl_mine_update)
    RelativeLayout mRlMineUpdate;

    @BindView(R.id.rl_mine_grade)
    RelativeLayout mRlMineGrade;

    @BindView(R.id.tv_mine_logout)
    TextView mTvMineLogout;

    @BindView(R.id.tv_rl_update)
    TextView vUpdate;

    @BindView(R.id.tv_mine_version)
    TextView tvVersion;


    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {

        OverScrollDecoratorHelper.setUpOverScroll((ScrollView) view.findViewById(R.id.sv_mine));
        tvVersion.setText("version " + Kits.Package.getVersionName(getContext()));
        mRlMineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!User.isLogin()) {
                    getUiDelegate().startActivity(LoginActivity.class);
                } else {
                    getUiDelegate().startActivity(CollectListActivity.class);
                }

            }
        });

        mTvMineLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    getUiDelegate().showCommonDialog("确定要退出账号么", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            logout();
                        }
                    });
                }


            }
        });

        mIvMineHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (User.isLogin()) {
                    getImage();
                } else {
                    getUiDelegate().startActivity(LoginActivity.class);
                }

            }
        });

        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUiDelegate().startActivity(CommitActivity.class);
            }
        });

        mRlMineNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUiDelegate().startActivity(PushActivity.class);
            }
        });
        mRlMineThank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(WebActivity.TITLE, "感谢编辑们");
                bundle.putString(WebActivity.LINK, "http://gank.io/backbone");
                getUiDelegate().startActivity(bundle, WebActivity.class);
            }
        });

        mRlMineAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUiDelegate().startActivity(AboutActivity.class);
            }
        });
        mRlMineUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Beta.checkUpgrade();
            }
        });
        mRlMineGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUiDelegate().showCommonDialog("去Github Start 一下吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putString(DetailActivity.TITLE, "gank android");
                        bundle.putString(DetailActivity.LINK, "https://github.com/shijunxing0130/gank");
                        bundle.putBoolean(DetailActivity.SHOWMORE, true);
                        getUiDelegate().startActivity(bundle, DetailActivity.class);
                    }
                });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        XLog.e("head", User.getHead());
    }

    @Override
    public void loadData() {
        initUserInfo();
        checkUpdate();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe
    public void handlerEven(IBus.IEvent event) {
        if (event.getTag() == EvenBusTag.USER_INFO) {
            initUserInfo();
        }
    }


    private void logout() {

        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.USER_LOGOUT, new NoticeEventListener() {
            @Override
            public void onEvent(BaseEvent event) {
                getUiDelegate().toast(event.getMsg());
                initUserInfo();
            }


        });
        controller.logout(context);
    }


    private void checkUpdate() {
        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();
        if (upgradeInfo == null) {
            vUpdate.setVisibility(View.GONE);
        } else {
            vUpdate.setVisibility(View.VISIBLE);
        }
    }


    private void initUserInfo() {
        if (User.isLogin()) {
            mTvMineName.setText(User.getName());
            mTvMineLogout.setVisibility(View.VISIBLE);
            mIvAdd.setVisibility(View.VISIBLE);
            ImageLoader
                    .with(getContext())
                    .placeHolder(R.drawable.holder_small_image)
                    .error(R.drawable.holder_small_image)
                    .url(User.getHead())
                    .asCircle()
                    .into(mIvMineHead);
        } else {
            mTvMineName.setText("登录后可以收藏和提交干货");
            mTvMineLogout.setVisibility(View.GONE);
            mIvAdd.setVisibility(View.GONE);
            mIvMineHead.setImageResource(R.drawable.holder_small_image);
        }
    }

    private void getImage() {

        EventDispatcherUtils.addListener(getReceiverIDCard(), RequestEventID.USER_CHANGE_HEAD, new NoticeEventListener() {

            @Override
            public void onEvent(BaseEvent event) {
                if (event.isSuccess()) {
                    initUserInfo();
                } else {
                    getUiDelegate().toast(event.getMsg());
                }
            }
        });

        SelectImageDialog selectImageDialog = new SelectImageDialog();
        selectImageDialog.setListener(new SelectImageDialog.OnDialogSelectListener() {
            @Override
            public void onDialogSelect(int type) {
                controller.getPhoto(getContext(), type);
            }
        });
        selectImageDialog.show(getFragmentManager());
    }


}
