package com.gank.android.app.ui.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gank.android.app.R;
import com.gank.android.app.entity.GanHuoEntity;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gank.androidlibs.image.config.SingleConfig;
import cn.gank.androidlibs.image.loader.ImageLoader;

/**
 * Created by shijunxing on 2017/11/25.
 */

public class TodayFragmentImageHolder {

    @BindView(R.id.tv_item_today_list_image_month)
    TextView tvMonth;

    @BindView(R.id.tv_item_today_list_image_day)
    TextView tvDay;

    @BindView(R.id.iv_item_today_list_image)
    ImageView ivImageView;

    @BindView(R.id.ll_item_today_list_date)
    View llDateBg;

    View mView;



    private OnListClickListener mListener;

    public TodayFragmentImageHolder(View itemView,OnListClickListener listener) {
        AutoUtils.autoSize(itemView);
        mView = itemView;
        mListener = listener;
        ButterKnife.bind(this, itemView);
    }


    public View getView() {
        return mView;
    }

    public void setData(final GanHuoEntity ganHuoEntity) {
        String date = ganHuoEntity.getDate();
        String dates[] = date.split("-");

        if (dates[1].substring(0, 1).equals("0")) {
            tvMonth.setText(dates[1].substring(1, 2) + "月");
        } else {
            tvMonth.setText(dates[1] + "月");
        }
        tvDay.setText(dates[2]);
        ImageLoader
                .with(mView.getContext())
                .placeHolder(R.drawable.holder_big_image)
                .error(R.drawable.holder_big_image)
                .url(ganHuoEntity.getUrl())
                .asBitmap(new SingleConfig.BitmapListener() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        if (ivImageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            ivImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = ivImageView.getLayoutParams();
                        int vw = ivImageView.getWidth() - ivImageView.getPaddingLeft() - ivImageView.getPaddingRight();
                        float scale = (float) vw / (float) bitmap.getWidth();
                        int vh = Math.round(bitmap.getHeight() * scale);
                        params.height = vh + ivImageView.getPaddingTop() + ivImageView.getPaddingBottom();
                        ivImageView.setLayoutParams(params);
                        ivImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onFail() {

                    }
                });

        llDateBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mListener.goToHistory();
            }
        });

        ivImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<GanHuoEntity> list = new ArrayList<>();
                list.add(ganHuoEntity);
                    mListener.goToImages(list, 0);
            }
        });

    }
    public interface OnListClickListener {
        void goToImages(List<GanHuoEntity> ganks, int position);
        void goToHistory();
    }
}
