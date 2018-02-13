package com.gank.android.app.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.gank.android.app.R;
import com.gank.android.app.view.when_page.PageFrameLayout;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindViews;
import cn.gank.androidlibs.kit.Kits;


/**
 * 欢迎页
 *
 * @author shijunxing
 * @date 2018/1/24
 */

public class WelcomeActivity extends FragmentActivity {

    private ViewPager mViewPager;

    private View mBtnGo;

    private ViewPaperAdapter vpAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initViews();
    }

    public void initViews() {
        StatusBarUtil.setTransparentForImageView(this, null);
        initPage();
    }


    private void initPage() {
        //实例化各个界面的布局对象
        View view1 = View.inflate(this, R.layout.page_tab1, null);
        View view2 = View.inflate(this, R.layout.page_tab2, null);
        View view3 = View.inflate(this, R.layout.page_tab3, null);
        View view4 = View.inflate(this, R.layout.page_tab4, null);
        View view5 = View.inflate(this, R.layout.page_tab5, null);


        ArrayList<View> views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        views.add(view4);
        views.add(view5);

        mViewPager = (ViewPager)findViewById(R.id.vp_guide);
        mBtnGo = view5.findViewById(R.id.id_ok);

        vpAdapter = new ViewPaperAdapter(views);

        mViewPager.setOffscreenPageLimit(views.size());
        mViewPager.setAdapter(vpAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position + 1 == mViewPager.getAdapter().getCount()) {
                    if (mBtnGo.getVisibility() != View.VISIBLE) {
                        mBtnGo.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mBtnGo.getVisibility() != View.GONE) {
                        mBtnGo.setVisibility(View.GONE);
                    }
                }
            }
        });
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
            }
        });


    }

    class ViewPaperAdapter extends PagerAdapter {
        private ArrayList<View> views;

        public ViewPaperAdapter(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            if (views != null) {
                return views.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position), 0);
            return views.get(position);
        }

    }
}
