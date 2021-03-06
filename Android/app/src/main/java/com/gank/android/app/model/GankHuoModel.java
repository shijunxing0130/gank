package com.gank.android.app.model;

import com.gank.android.app.entity.GanHuoEntity;

import java.util.List;

/**
 * Created by shijunxing on 2017/9/7.
 */

public interface GankHuoModel {
    List<GanHuoEntity> getDailyList();
    List<GanHuoEntity> getSearchList();
    List<String> getHistoryList();
    GanHuoEntity getImageEntity();
}
