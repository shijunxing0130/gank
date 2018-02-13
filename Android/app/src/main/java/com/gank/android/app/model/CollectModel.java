package com.gank.android.app.model;

import com.gank.android.app.entity.GanHuoEntity;

import java.util.List;

/**
 * Created by shijunxing on 2017/11/1.
 */

public interface CollectModel {

    List<String> getCollectsTag();
    List<GanHuoEntity> getCollectsByTag(String tag);
}
