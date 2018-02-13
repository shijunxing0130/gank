package com.gank.android.app.controller;

import android.content.Context;

import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.even.RequestEventID;
import com.gank.android.app.model.ClassifyLisModelImpl;
import com.gank.android.app.model.ClassifyListModel;
import com.gank.android.app.net.UrlKit;
import com.gank.android.mvc.BaseController;
import com.gank.android.mvc.events.DataEvent;
import com.gank.android.mvc.events.NoticeEvent;

import java.util.List;

import cn.gank.androidlibs.httphelper.BaseResponse;
import cn.gank.androidlibs.httphelper.HttpFactory;
import cn.gank.androidlibs.httphelper.JsonCallback;
import cn.gank.androidlibs.httphelper.Result;
import cn.gank.androidlibs.httphelper.ResultCallBack;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 干货分类Controller
 * Created  on 2017/9/7
 * @author shijunxing
 */

public class ClassifyLisController extends BaseController<ClassifyLisModelImpl,ClassifyListModel> {

    public ClassifyLisController(String receiverIDCard) {
        super(receiverIDCard);
    }

    /** 干货分类
     * @param context 上下文
     * @param tag 标签
     * @param page 页码
     */
    public void getClassifyList(Context context, String tag, int page) {
        HttpFactory.getHelper().invokeGet(context, UrlKit.getUrl(UrlKit.API_CLASSIFY + tag + "/" + "20" + "/" + page),
                new JsonCallback<BaseResponse<List<GanHuoEntity>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<GanHuoEntity>> entity, Call call, Response response) {
                        if (entity != null && entity.getResults() != null) {
                            getModel().setHuoEntityList(entity.getResults());
                        }
                        dispatchEvent(new NoticeEvent(RequestEventID.CLASSIFY_LIST,entity));
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        dispatchEvent(new NoticeEvent(RequestEventID.CLASSIFY_LIST,-1,e.getMessage()));
                    }
                });
    }

}
