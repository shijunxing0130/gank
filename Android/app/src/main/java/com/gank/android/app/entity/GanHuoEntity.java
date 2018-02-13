package com.gank.android.app.entity;


import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by shijunxing on 2017/9/7.
 */

public class GanHuoEntity {

    public static final int WELFARE = 3;
    public static final int CONTENT = 2;
    public static final int TITLE = 1;
    public static final int IMAGE = 0;

    /**
     * _id : 56cc6d23421aa95caa707a69
     * createdAt : 2015-08-06T07:15:52.65Z
     * desc : 类似Link Bubble的悬浮式操作设计
     * publishedAt : 2015-08-07T03:57:48.45Z
     * type : Android
     * url : https://github.com/recruit-lifestyle/FloatingView
     * used : true
     * who : mthli
     */

    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String type;
    private String url;
    @Expose(serialize = true, deserialize = false)
    private boolean used;
    @Expose(serialize = true, deserialize = false)
    private boolean loaded;
    @Expose(serialize = true, deserialize = false)
    private List<String> images;

    private String who;
    @Expose(serialize = true, deserialize = false)
    private int itemType = CONTENT;

    public String getDate() {
        int end = publishedAt.indexOf("T");
        return publishedAt.substring(0, end);
    }
    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setItemType(int type) {
        itemType = type;
    }

    public int getItemType() {
        if (getType().equals("福利") && itemType != IMAGE){
            setItemType(WELFARE);
        }
        return itemType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
