package com.gank.android.app.ui.classify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.ui.base.BaseActivity;
import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.view.PinchImageView;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import cn.gank.androidlibs.httphelper.Convert;
import cn.gank.androidlibs.image.imagei.ImageDownLoadCallBack;
import cn.gank.androidlibs.image.loader.ImageLoader;
import cn.gank.androidlibs.image.utils.DownLoadImageService;


/**
 * 妹子图片
 *
 * @author shijunxing
 */
public class ImageActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager mPager;

    @BindView(R.id.tv_image_position)
    TextView mTvPosition;

    @BindView(R.id.iv_toolbar_save)
    ImageView mIvSave;

    @BindView(R.id.iv_toolbar_share)
    ImageView mIvShare;


    public static final String URLS = "url";
    public static final String POSITION = "position";

    private List<GanHuoEntity> mUrls;
    private int mPosition;

    private final LinkedList<PinchImageView> viewCache = new LinkedList<>();

    @Override
    public void initVariables() {
        mPosition = getIntent().getExtras().getInt(POSITION);
        String list = getIntent().getExtras().getString(URLS);
        if (list != null) {
            mUrls = Convert.fromJson(list, new TypeToken<ArrayList<GanHuoEntity>>() {
            }.getType());
        }
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {

        initToolBar("");
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mUrls.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                final PinchImageView piv;
                if (viewCache.size() > 0) {
                    piv = viewCache.remove();
                    piv.reset();
                } else {
                    piv = new PinchImageView(ImageActivity.this);
                }

                ImageLoader.with(ImageActivity.this)
                        .url(mUrls.get(position).getUrl())
                        .into(piv);

                container.addView(piv);
                return piv;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                PinchImageView piv = (PinchImageView) object;
                container.removeView(piv);
                viewCache.add(piv);
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                final PinchImageView piv = (PinchImageView) object;
                ImageLoader.with(ImageActivity.this)
                        .url(mUrls.get(position).getUrl())
                        .into(piv);

            }
        });

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvPosition.setText(position + 1 + "/" + mUrls.size());
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPager.setCurrentItem(mPosition);
        mIvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        mIvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }


    @Override
    public void loadData() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setTransparentForImageView(this, null);
    }

    private void share() {
        Intent intent1 = new Intent(Intent.ACTION_SEND);
        intent1.putExtra(Intent.EXTRA_TEXT, mUrls.get(mPosition).getUrl());
        intent1.setType("text/plain");
        startActivity(Intent.createChooser(intent1, "分享到"));
    }

    private void save() {
        ImageLoader.saveImageIntoGallery(new DownLoadImageService(
                this,
                mUrls.get(mPosition).getUrl(),
                true,
                mUrls.get(mPosition).getDate(),
                new ImageDownLoadCallBack() {
                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getUiDelegate().toast("保存成功");
                            }
                        });
                    }

                    @Override
                    public void onDownLoadFailed() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getUiDelegate().toast("保存失败");
                            }
                        });
                    }
                }));
    }

}