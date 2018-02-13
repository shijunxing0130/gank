package com.gank.android.app.entity;

/**
 * Created by shijunxing on 2017/10/13.
 */

public class GankCollect {


    /**
     * createdAt : 2017-10-11 17:44:31
     * gankHuoJson : {"ItemType":2,"_id":"59dcca81421aa94e0053bddf","createdAt":"2017-10-10T21:26:25.797Z","desc":"最近在给某某银行做项目的时，涉及到了数据埋点，性能监控等问题，那我们起先想到的有两种方案，方案之一就是借助第三方，比如友盟、Bugly等，由于项目是部署在银行的网络框架之内的，所以该方案不可行。","loaded":false,"publishedAt":"2017-10-11T12:40:42.545Z","type":"Android","url":"https://mp.weixin.qq.com/s?__biz\u003dMzIyMjQ0MTU0NA\u003d\u003d\u0026mid\u003d2247484445\u0026idx\u003d1\u0026sn\u003d8eef04a7932b58ef0012643db228fb32\u0026chksm\u003de82c3d3adf5bb42c88333160a88c7b05fb5f45798434afa956fe1f1a58c7713ef121c7ea0af4\u0026scene\u003d0\u0026key\u003d7460e137ddd94f92f668e812cfc0aef8fde2bdf7943c7409875cce12a3baed3526f31e4a707ed86896ee8ddbbf761bb2f09b2d7406c3b9016589495240d835d967a2141231c43d084635a7df11647fb0\u0026ascene\u003d0\u0026uin\u003dMjMzMzgwOTEwMQ%3D%3D\u0026devicetype\u003diMac+MacBookPro12%2C1+OSX+OSX+10.10.5+build(14F27)\u0026version\u003d11020201\u0026pass_ticket\u003d54ym37fDoXgDZm7nzjGt6KNDR9%2F9ZIU8%2Bo5kNcGEXqi8GKijls6et5TXcXxbERi%2F","used":true,"who":"Tamic (码小白)"}
     * objectId : dc0db8379d
     * updatedAt : 2017-10-11 17:44:31
     * userId : bb36aaad3e
     */

    private String createdAt;
    private String gankHuoJson;
    private String objectId;
    private String updatedAt;
    private String userId;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGankHuoJson() {
        return gankHuoJson;
    }

    public void setGankHuoJson(String gankHuoJson) {
        this.gankHuoJson = gankHuoJson;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
