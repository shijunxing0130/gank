package com.gank.android.app.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.view.FastSmoothScrollLinearLayoutManager;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 版本更新
 * @author shijunxing
 * @date 2018/1/17
 */

public class VersionUpdateActivity extends BaseActivity {


    @BindView(R.id.rv_version)
    RecyclerView mRvVersion;

    private List<Version> mVersions;



    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar("版本更新", false);
    }

    @Override
    public void loadData() {
        mVersions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mVersions.add(new Version("1.0."+i,"2018年"+i+"月30号更新","版本更新"+1));
        }
        initList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_version;
    }
    private void initList() {
        mRvVersion.setLayoutManager(new FastSmoothScrollLinearLayoutManager(this));
        mRvVersion.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new VersionVH(inflate(parent, R.layout.item_version_list));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((VersionVH) holder).setData(mVersions.get(position));
            }

            @Override
            public int getItemCount() {
                return mVersions.size();
            }

        });
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(this).inflate(layoutRes, parent, false);
    }

    /**
     * version
     */
    public class VersionVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_version_name)
        TextView tvName;

        @BindView(R.id.tv_item_version_date)
        TextView tvDate;

        @BindView(R.id.tv_item_version_msg)
        TextView tvMsg;

        public VersionVH(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Version version) {
            tvName.setText(version.name);
            tvDate.setText(version.date);
            tvMsg.setText(version.msg);
        }
    }

    class Version {
        String name;
        String date;
        String msg;

        public Version(String name, String date, String msg) {
            this.name = name;
            this.date = date;
            this.msg = msg;
        }
    }
}
