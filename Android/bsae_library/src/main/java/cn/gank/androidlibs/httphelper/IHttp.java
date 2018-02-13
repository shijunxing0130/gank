package cn.gank.androidlibs.httphelper;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.callback.AbsCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/29.
 */
public interface IHttp {

    void init(Context context);
    void init(Application context);
    void invokeGet(Context context,String url,  AbsCallback callback);
    void invokeGet(Context context,String url,Map<String, String> params,  AbsCallback callback);
    void invokePost(Context context, String url,Map<String, String> params, String fileName, String filePath, AbsCallback callback);
    void invokePost(Context context, String url, Map<String,String> params, AbsCallback callback);
    void invokePost(Context context, String url, String name, File file, AbsCallback callback);
    void cancelByTag(Object tag);
}
