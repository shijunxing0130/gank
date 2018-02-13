package com.gank.android.app.ui.mine;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.ui.detail.DetailActivity;
import com.gank.android.app.ui.detail.WebActivity;
import com.gank.android.app.view.FastSmoothScrollLinearLayoutManager;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 开源库
 *
 * @author shijunxing
 * @date 2017/11/14
 */

public class LibsActivity extends BaseActivity {

    @BindView(R.id.rv_libs)
    RecyclerView mRvLibs;

    private List<Lib> mLibList;

    @Override
    public void initVariables() {
        initLibs();
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initToolBar("开源库", false);
        initList();
    }


    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_libs;
    }


    private void initLibs() {
        Resources res = getResources();
        String[] libs = res.getStringArray(R.array.libs);
        mLibList = new ArrayList<>();
        Lib libTitleGank = new Lib("干货集中营-Gank", "", true);
        Lib libLibGank = new Lib("Gank的 GitHub 仓库", "https://github.com/shijunxing0130/gank", false);
        Lib libTitleThird = new Lib("第三方", "", true);
        mLibList.add(libTitleGank);
        mLibList.add(libLibGank);
        mLibList.add(libTitleThird);
        for (String s : libs) {
            String[] name_link = s.split(",");
            Lib lib = new Lib(name_link[0], name_link[1], false);
            mLibList.add(lib);
        }
    }

    private void initList() {
        mRvLibs.setLayoutManager(new FastSmoothScrollLinearLayoutManager(this));
        mRvLibs.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    return new TitleVH(inflate(parent, R.layout.item_libs_title));
                }

                return new LibsVH(inflate(parent, R.layout.item_libs_lib));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (getItemViewType(position) == 0) {
                    ((TitleVH) holder).setData(mLibList.get(position));
                } else {
                    ((LibsVH) holder).setData(mLibList.get(position));
                }
            }

            @Override
            public int getItemCount() {
                return mLibList.size();
            }

            @Override
            public int getItemViewType(int position) {
                return mLibList.get(position).isTitle ? 0 : 1;
            }
        });
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(this).inflate(layoutRes, parent, false);
    }

    /**
     * 标题
     */
    public class TitleVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_lib_title)
        TextView tvTitle;

        public TitleVH(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Lib lib) {
            tvTitle.setText(lib.name);
        }
    }

    /**
     * lib
     */
    public class LibsVH extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_lib_name)
        TextView tvName;

        public LibsVH(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Lib lib) {
            tvName.setText(lib.name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(DetailActivity.TITLE, lib.name);
                    bundle.putString(DetailActivity.LINK, lib.link);
                    bundle.putBoolean(DetailActivity.SHOWMORE, true);
                    getUiDelegate().startActivity(bundle, DetailActivity.class);
                }
            });
        }
    }

    class Lib {
        String name;
        String link;
        boolean isTitle;

        public Lib(String name, String link, boolean isTitle) {
            this.name = name;
            this.link = link;
            this.isTitle = isTitle;
        }
    }
}
