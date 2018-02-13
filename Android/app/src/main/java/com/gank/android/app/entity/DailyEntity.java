package com.gank.android.app.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by shijunxing on 2017/9/7.
 */

public class DailyEntity {



    private List<GanHuoEntity> Android;
    private List<GanHuoEntity> iOS;
    private List<GanHuoEntity> App;
    private List<GanHuoEntity> 前端;
    private List<GanHuoEntity> 休息视频;
    private List<GanHuoEntity> 拓展资源;
    private List<GanHuoEntity> 瞎推荐;
    private List<GanHuoEntity> 福利;

    public List<GanHuoEntity> getAndroid() {
        return Android;
    }

    public void setAndroid(List<GanHuoEntity> Android) {
        this.Android = Android;
    }

    public List<GanHuoEntity> getIOS() {
        return iOS;
    }

    public void setIOS(List<GanHuoEntity> iOS) {
        this.iOS = iOS;
    }

    public List<GanHuoEntity> getApp() {
        return App;
    }

    public void setApp(List<GanHuoEntity> app) {
        App = app;
    }

    public List<GanHuoEntity> getH5() {
        return 前端;
    }

    public void setH5(List<GanHuoEntity> 前端) {
        this.前端 = 前端;
    }

    public List<GanHuoEntity> getRestVedio() {
        return 休息视频;
    }

    public void setRestVedio(List<GanHuoEntity> 休息视频) {
        this.休息视频 = 休息视频;
    }

    public List<GanHuoEntity> getExpandResurce() {
        return 拓展资源;
    }

    public void setExpandResurce(List<GanHuoEntity> 拓展资源) {
        this.拓展资源 = 拓展资源;
    }

    public List<GanHuoEntity> getFiddleRecommend() {
        return 瞎推荐;
    }

    public void setFiddleRecommend(List<GanHuoEntity> 瞎推荐) {
        this.瞎推荐 = 瞎推荐;
    }

    public List<GanHuoEntity> getelfare() {
        return 福利;
    }

    public void setWelfare(List<GanHuoEntity> 福利) {
        this.福利 = 福利;
    }


    public List<List<GanHuoEntity>> getCategoryList(){
        List<List<GanHuoEntity>> lists = new ArrayList<>();
        lists.add(getAndroid());
        lists.add(getIOS());
        lists.add(getH5());
        lists.add(getRestVedio());
        lists.add(getFiddleRecommend());
        lists.add(getelfare());
        lists.add(getExpandResurce());

        return lists;
    }
}
