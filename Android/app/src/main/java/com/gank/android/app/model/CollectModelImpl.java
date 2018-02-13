package com.gank.android.app.model;

import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.mvc.BaseUIModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shijunxing
 * @date 2017/11/1
 */

public class CollectModelImpl extends BaseUIModel implements CollectModel {


    private Map<String, List<GanHuoEntity>> mTagsCollectMap = new HashMap<>();
    private List<String> mTagList = new ArrayList<>();

    public void setCollects(List<GanHuoEntity> ganHuoEntityList){
        mTagsCollectMap.clear();
        mTagList.clear();
        for (GanHuoEntity ganHuoEntity : ganHuoEntityList) {
            if (mTagsCollectMap.containsKey("All")) {
                mTagsCollectMap.get("All").add(ganHuoEntity);
            } else {
                mTagList.add("All");
                List<GanHuoEntity> list = new ArrayList<>();
                list.add(ganHuoEntity);
                mTagsCollectMap.put("All", list);
            }

            if (mTagsCollectMap.containsKey(ganHuoEntity.getType())) {
                mTagsCollectMap.get(ganHuoEntity.getType()).add(ganHuoEntity);
            } else {
                mTagList.add(ganHuoEntity.getType());
                List<GanHuoEntity> list = new ArrayList<>();
                list.add(ganHuoEntity);
                mTagsCollectMap.put(ganHuoEntity.getType(), list);
            }
        }
    }



    @Override
    public List<String> getCollectsTag() {
        return mTagList;
    }

    @Override
    public List<GanHuoEntity> getCollectsByTag(String tag) {
        return mTagsCollectMap.get(tag);
    }
}
