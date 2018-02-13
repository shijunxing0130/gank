package com.gank.android.app.ui.classify;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gank.android.app.R;
import com.gank.android.app.ui.base.BaseFragment;
import com.gank.android.app.entity.IconEntity;
import com.gank.android.app.ui.adapter.ClassifyListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 *
 * @author shijunxing
 * @date 2017/9/2
 */

public class ClassifyFragment extends BaseFragment implements ClassifyListAdapter.OnListClickListener{

    @BindView(R.id.rv_classify)
    RecyclerView mRecyclerView;

    @BindView(R.id.iv_toolbar_search)
    View mViewSearch;

    @Override
    public void initVariables() {

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        initClassify();
        mViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(SearchActivity.TAG,"all");
                bundle.putString(SearchActivity.HINT,"搜索你想找的干货吧");
                getUiDelegate().startActivity(bundle,SearchActivity.class);
            }
        });
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_classify;
    }


    private void initClassify(){

        List<IconEntity> list = new ArrayList<>();
        list.add(new IconEntity(R.drawable.category_all,"all"));
        list.add(new IconEntity(R.drawable.category_android,"Android"));
        list.add(new IconEntity(R.drawable.category_ios,"iOS"));
        list.add(new IconEntity(R.drawable.category_app,"App"));
        list.add(new IconEntity(R.drawable.category_hfive,"前端"));
        list.add(new IconEntity(R.drawable.category_vedio,"休息视频"));
        list.add(new IconEntity(R.drawable.category_common,"瞎推荐"));
        list.add(new IconEntity(R.drawable.category_resurce,"拓展资源"));
        list.add(new IconEntity(R.drawable.category_welfare,"福利"));

        ClassifyListAdapter adapter = new ClassifyListAdapter(this);
        adapter.setList(list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        mRecyclerView.setAdapter(adapter);
    }


    @Override
    public void toClassifyList(String tag) {
        Bundle bundle = new Bundle();
        bundle.putString(ClassifyListActivity.TAG,tag);
        getUiDelegate().startActivity(bundle,ClassifyListActivity.class);
    }
}
