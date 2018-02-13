package com.gank.android.app.model;

import android.content.Context;

import com.gank.android.app.entity.GanHuoEntity;
import com.gank.android.app.net.UrlKit;
import com.gank.android.mvc.BaseUIModel;

import java.util.List;

import cn.gank.androidlibs.httphelper.BaseResponse;
import cn.gank.androidlibs.httphelper.HttpFactory;
import cn.gank.androidlibs.httphelper.JsonCallback;
import cn.gank.androidlibs.httphelper.Result;
import cn.gank.androidlibs.httphelper.ResultCallBack;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by shijunxing on 2017/9/7.
 */

public class ClassifyLisModelImpl extends BaseUIModel implements ClassifyListModel {

    private List<GanHuoEntity> mHuoEntityList;
    private static final String mPageSize = "20";

    public void getClassifyLis(Context context, String tag, int page, final ResultCallBack callBack) {
        HttpFactory.getHelper().invokeGet(context, UrlKit.getUrl(UrlKit.API_CLASSIFY + tag + "/" + mPageSize + "/" + page),
                new JsonCallback<BaseResponse<List<GanHuoEntity>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<GanHuoEntity>> entity, Call call, Response response) {
                        if (entity != null && entity.getResults() != null) {
                            mHuoEntityList = entity.getResults();
                            callBack.onResult(new Result(true, ""));
                        } else {
                            callBack.onResult(new Result(false, "数据为空"));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        callBack.onResult(new Result(false, e.getMessage()));
                    }
                });
    }

    @Override
    public List<GanHuoEntity> getClassifyLis() {
        return mHuoEntityList;
    }

    public void setHuoEntityList(List<GanHuoEntity> huoEntityList) {
        mHuoEntityList = huoEntityList;
    }
}
