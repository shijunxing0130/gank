package com.gank.android.app.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.ui.detail.DetailActivity;
import com.gank.android.app.ui.detail.WebActivity;

import butterknife.BindView;
import cn.gank.androidlibs.kit.Kits;

/**
 * 关于
 *
 * @author shijunxing
 * @date 2017/11/14
 */

public class AboutActivity extends BaseActivity {


    @BindView(R.id.tv_about_version)
    TextView mTvAboutVersion;

    @BindView(R.id.rl_about_author)
    RelativeLayout mRlAboutAuthor;

    @BindView(R.id.rl_about_github)
    RelativeLayout mRlAboutGithub;

    @BindView(R.id.rl_about_thank)
    RelativeLayout mRlAboutThank;

    @BindView(R.id.rl_about_libs)
    RelativeLayout mRlAboutLibs;

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar("关于",false);
        mTvAboutVersion.setText(Kits.Package.getVersionName(this));
        mRlAboutLibs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUiDelegate().startActivity(LibsActivity.class);
            }
        });
        mRlAboutThank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toDetail("致谢Gank","http://gank.io");
            }
        });
        mRlAboutAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDetail("关于作者","https://github.com/shijunxing0130");
            }
        });
        mRlAboutGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDetail("作者 Github","https://github.com/shijunxing0130");
            }
        });
    }

    private void toDetail(String t,String l){
        Bundle bundle = new Bundle();
        bundle.putString(DetailActivity.TITLE, t);
        bundle.putString(DetailActivity.LINK, l);
        bundle.putBoolean(DetailActivity.SHOWMORE, false);
        getUiDelegate().startActivity(bundle, DetailActivity.class);

    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about;
    }
}
