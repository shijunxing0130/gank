package com.gank.service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gank.service.bean.CollectBean;
import com.gank.service.bean.RestFulBean;
import com.gank.service.dao.CollectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author shijunxing
 */
@Transactional
public class CollectService {

    @Autowired
    private CollectDao collectDao;

    public RestFulBean<Boolean> collect(String uid,String json){
        CollectBean collectBean ;
        try {
            collectBean  = new ObjectMapper().readValue(json, CollectBean.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new RestFulBean<>(false, 1, "收藏失败");
        }
        if (collectBean != null) {
            collectBean.setUser_id(uid);
            if (collectDao.collect(collectBean) != null) {
               return new RestFulBean<>(true, 0, "收藏成功");
            }else {
                return new RestFulBean<>(false, 2, "收藏失败");
            }

        }else {
            return new RestFulBean<>(false, 3, "收藏失败");
        }
    }


    public RestFulBean<Boolean> unCollect(String uid,String id){
        if (collectDao.unCollect(uid, id) == null) {
            return new RestFulBean<>(true, 0, "取消收藏成功");
        }else {
            return new RestFulBean<>(false, 1, "取消收藏失败");
        }
    }

    public RestFulBean<Boolean> isCollect(String uid,String id){
        if (collectDao.isCollect(uid, id)) {
            return new RestFulBean<>(true, 0, "已收藏");
        }else {
            return new RestFulBean<>(false, 1, "未收藏");
        }
    }
    public RestFulBean<List<CollectBean>> getCollects(String uid,  int page , int pageSize){

        List<CollectBean> collectList = collectDao.getCollects(uid, page,pageSize);
        if (collectList != null) {
            return new RestFulBean<>(collectList, 0, "获取成功");
        }else {
            return new RestFulBean<>(null, 1, "获取失败");
        }
    }
}
