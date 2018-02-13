package com.gank.android.app.model;

import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.mvc.BaseUIModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 干货model
 * @author shijunxing
 * @date 2017/9/7
 */

public class GankHuoModelImpl extends BaseUIModel implements GankHuoModel {



    private List<GanHuoEntity> mDailyList;
    private GanHuoEntity mImageEntity;
    private List<GanHuoEntity> mSearchList;
    private List<String> mHistoryList;


    public void setDailyList(List<List<GanHuoEntity>> todayEntity) {
        if (mDailyList == null) {
            mDailyList = new ArrayList<>();
        }else {
            mDailyList.clear();
        }

        for (List<GanHuoEntity> ganHuoEntityList : todayEntity) {
            if (ganHuoEntityList != null && ganHuoEntityList.size() != 0) {
                if (ganHuoEntityList.get(0).getType().equals("福利")) {
                    mImageEntity = ganHuoEntityList.get(0);
                    mImageEntity.setItemType(GanHuoEntity.IMAGE);
                } else {
                    GanHuoEntity title = new GanHuoEntity();
                    title.setType(ganHuoEntityList.get(0).getType());
                    title.setItemType(GanHuoEntity.TITLE);
                    mDailyList.add(title);
                    mDailyList.addAll(ganHuoEntityList);
                }

            }
        }

    }


    public void setSearchList(List<GanHuoEntity> searchList) {
        mSearchList = searchList;
    }

    public void setHistoryList(List<String> historyList) {
        if(mHistoryList == null) {
            mHistoryList = new ArrayList<>();
        }else {
            mHistoryList.clear();
        }
        for (String s : historyList) {
            mHistoryList.add(s.replace("-", "/"));
        }
    }


    @Override
    public List<GanHuoEntity> getDailyList() {
        return mDailyList;
    }

    @Override
    public List<GanHuoEntity> getSearchList() {
        return mSearchList;
    }

    @Override
    public List<String> getHistoryList() {
        return mHistoryList;
    }

    @Override
    public GanHuoEntity getImageEntity() {
        return mImageEntity;
    }

}
