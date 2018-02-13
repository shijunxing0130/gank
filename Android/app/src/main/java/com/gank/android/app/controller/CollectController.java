package com.gank.android.app.controller;

import android.content.Context;

import com.gank.android.app.aouth.User;
import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.model.CollectModel;
import com.gank.android.app.model.CollectModelImpl;
import com.gank.android.app.net.UrlKit;
import com.gank.android.mvc.BaseController;
import com.gank.android.mvc.events.NoticeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gank.androidlibs.httphelper.BaseResponse;
import cn.gank.androidlibs.httphelper.HttpFactory;
import cn.gank.androidlibs.httphelper.JsonCallback;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 收藏
 * Created by shijunxing on 2017/9/7
 * @author shijunxing
 */

public class CollectController extends BaseController<CollectModelImpl, CollectModel> {

    public CollectController(String receiverIDCard) {
        super(receiverIDCard);
    }

    /**
     * 收藏
     * @param context
     * @param ganHuoEntity 干货实体json
     */
    public void collect(Context context, String ganHuoEntity) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", User.getPhone());
        params.put("json", ganHuoEntity);

        doCollect(context, UrlKit.getUserUrl(UrlKit.API_COLLECT),RequestEventID.COLLECT, params);
    }

    /**
     * 取消收藏
     * @param context
     * @param gankID 干货id
     */
    public void unCollect(Context context, String gankID) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", User.getPhone());
        params.put("id", gankID);

       doCollect(context, UrlKit.getUserUrl(UrlKit.API_UN_COLLECT),RequestEventID.UN_COLLECT, params);
    }

    /**
     * 判断是否收藏过
     * @param context
     * @param gankID 干货id
     */
    public void isCollect(Context context, String gankID) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", User.getPhone());
        params.put("id", gankID);

        doCollect(context, UrlKit.getUserUrl(UrlKit.API_IS_COLLECT), RequestEventID.IS_COLLECT,params);
    }


    private void doCollect(Context context, String url, final String requestEvenID, Map<String, String> params) {
        HttpFactory.getHelper().invokePost(context, url, params, new JsonCallback<BaseResponse<Boolean>>() {

            @Override
            public void onSuccess(BaseResponse<Boolean> response, Call call, Response response2) {
                if (response != null) {
                    dispatchEvent(new NoticeEvent(requestEvenID,response));
                } else {
                    dispatchEvent(new NoticeEvent(requestEvenID,-2,"未知错误"));
                }
            }
            @Override
            public void onError(Call call, Response response, Exception e) {
                dispatchEvent(new NoticeEvent(requestEvenID,-1,e.getMessage()));
            }
        });
    }


    /**
     * 收藏列表
     * @param context
     * @param page 当前页
     * @param pageSize 每页数据个数
     */
    public void getCollects(Context context,int page, int pageSize) {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", User.getPhone());
        params.put("page", page + "");
        params.put("pageSize", pageSize + "");
        HttpFactory.getHelper().invokePost(context, UrlKit.getUserUrl(UrlKit.API_COLLECTS), params, new JsonCallback<BaseResponse<List<GanHuoEntity>>>() {

            @Override
            public void onSuccess(BaseResponse<List<GanHuoEntity>> response, Call call, Response response2) {
                if (response != null) {
                    if (response.getResults() != null && response.getResults().size() != 0) {
                        getModel().setCollects(response.getResults());
                    }
                    dispatchEvent(new NoticeEvent(RequestEventID.GET_COLLECTS,response));

                } else {
                    dispatchEvent(new NoticeEvent(RequestEventID.GET_COLLECTS,-2,"未知错误"));
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                dispatchEvent(new NoticeEvent(RequestEventID.GET_COLLECTS,-1,e.getMessage()));
            }
        });
    }
}
