package com.gank.service.dao;

import com.gank.service.bean.CollectBean;

import java.util.List;

/**
 * @author shijunxing
 */
public interface CollectDao {

    /**
     * 收藏
     * @param collectBean
     */
    CollectBean collect(CollectBean collectBean);

    /**
     * 取消收藏
     * @param uid
     * @param id
     */
    CollectBean unCollect(String uid,String id);

    /**
     * 是否收藏过
     * @param uid
     * @param id
     * @return
     */
    boolean isCollect(String uid,String id);

     /**
     * 获取收藏
     * @param uid
     * @return
     */
    List<CollectBean> getCollects(String uid, int page , int pageSize);

}
