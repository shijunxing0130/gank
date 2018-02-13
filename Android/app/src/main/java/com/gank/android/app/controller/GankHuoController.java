package com.gank.android.app.controller;

import android.content.Context;

import cn.gank.androidlibs.httphelper.BaseResponse;
import cn.gank.androidlibs.httphelper.HttpFactory;
import cn.gank.androidlibs.httphelper.JsonCallback;
import cn.gank.androidlibs.httphelper.Result;

import com.gank.android.app.entity.DailyEntity;
import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.model.GankHuoModelImpl;
import com.gank.android.app.model.GankHuoModel;
import com.gank.android.app.net.UrlKit;
import com.gank.android.mvc.BaseController;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.NoticeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.gank.androidlibs.httphelper.ResultCallBack;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 干货Controller
 * @author shijunxing
 * @date 2017/9/7
 */

public class GankHuoController extends BaseController<GankHuoModelImpl, GankHuoModel> {

    public GankHuoController(String receiverIDCard) {
        super(receiverIDCard);
    }

    /** 干货历史
     * @param context 上下文
     */
    public void getHistory(Context context) {
        HttpFactory.getHelper().invokeGet(context, UrlKit.getUrl(UrlKit.API_HISTORY), new JsonCallback<BaseResponse<List<String>>>() {

            @Override
            public void onSuccess(BaseResponse<List<String>> response, Call call, Response response2) {
                if (response.getResults() != null) {
                    getModel().setHistoryList(response.getResults());
                }
                dispatchEvent(new NoticeEvent(RequestEventID.HISTORY_LIST,response));
            }

            @Override
            public void onError(Call call, Response response, Exception e) {

            }
        });
    }


    /** 干货搜索
     * @param context 上下文
     * @param keyword 关键词
     * @param category 类别
     * @param pageIndex 页码
     */
    public void getSearchList(Context context, String keyword, String category, int pageIndex) {

        String url = UrlKit.API_SEARCH.replace("{keyword}", keyword).replace("{category}", category).replace("{pageIndex}", pageIndex + "");

        HttpFactory.getHelper().invokeGet(context, UrlKit.getUrl(url),
                new JsonCallback<BaseResponse<List<GanHuoEntity>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<GanHuoEntity>> entity, Call call, Response response) {
                        if (entity != null && entity.getResults() != null) {
                            getModel().setSearchList(entity.getResults());
                        }
                        dispatchEvent(new NoticeEvent(RequestEventID.SEARCH_LIST,entity));
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dispatchEvent(new NoticeEvent(RequestEventID.SEARCH_LIST,-1,e.getMessage()));
                    }
                });
    }


    /** 提交干货
     * @param context 上下文
     * @param type 类别
     * @param url 地址
     * @param desc 描述
     * @param who 作者
     */
    public void addGankHuo(Context context, String type, String url, String desc, String who) {

        Map<String, String> params = new HashMap<>(16);
        params.put("type", type);
        params.put("url", url);
        params.put("desc", desc);
        params.put("who", who);

        HttpFactory.getHelper().invokePost(context, UrlKit.getUrl(UrlKit.API_ADD), params,
                new JsonCallback<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<String> response, Call call, Response response2) {
                        dispatchEvent(new NoticeEvent(RequestEventID.ADD,response));
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dispatchEvent(new NoticeEvent(RequestEventID.ADD,-1,e.getMessage()));
                    }
                });
    }

    /**
     * 每日干货
     * @param context 上下文
     * @param date 日期
     */
    public void getDailyList(final Context context, String date) {

        HttpFactory.getHelper().invokeGet(context, UrlKit.getUrl(UrlKit.API_TODAY + date), new JsonCallback<BaseResponse<DailyEntity>>() {
            @Override
            public void onSuccess(BaseResponse<DailyEntity> entity, Call call, Response response) {
                DailyEntity todayEntity = entity.getResults();
                if (todayEntity != null && todayEntity.getCategoryList() != null && todayEntity.getCategoryList().size() != 0) {
                    getModel().setDailyList(todayEntity.getCategoryList());
                    dispatchEvent(new NoticeEvent(RequestEventID.TODAY_LIST,entity));
                } else {
                    dispatchEvent(new NoticeEvent(RequestEventID.TODAY_LIST,-2,"未知错误"));
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                dispatchEvent(new NoticeEvent(RequestEventID.TODAY_LIST,-1,e.getMessage()));
            }
        });
    }

}
