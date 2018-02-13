package cn.gank.androidlibs.httphelper;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.DeviceUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.cookie.store.PersistentCookieStore;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.PostRequest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import cn.gank.androidlibs.cache.SharedPref;
import cn.gank.androidlibs.kit.Kits;
import cn.gank.androidlibs.kit.Md5SaltTool;
import cn.gank.androidlibs.kit.SignUtils;
import cn.gank.androidlibs.log.XLog;
import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.OkGo.post;

/**
 * @author shijunxing
 * @date 2016/12/29
 */
public class OKHelper implements IHttp {

    @Override
    public void init(Context context) {

    }

    @Override
    public void init(Application context) {

        //必须调用初始化
        OkGo.init(context);

        //以下设置的所有参数是全局参数,同样的参数可以在请求的时候再设置一遍,那么对于该请求来讲,请求中的参数会覆盖全局参数
        //好处是全局参数统一,特定请求可以特别定制参数
        try {
            //以下都不是必须的，根据需要自行选择,一般来说只需要 debug,缓存相关,cookie相关的 就可以了
            OkGo.getInstance()

                    // 打开该调试开关,打印级别INFO,并不是异常,是为了显眼,不需要就不要加入该行
                    // 最后的true表示是否打印okgo的内部异常，一般打开方便调试错误
                    .debug("OkGo", Level.INFO, true)

                    //如果使用默认的 60秒,以下三行也不需要传
                    //全局的连接超时时间
                    .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)
                    //全局的读取超时时间
                    .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)
                    //全局的写入超时时间
                    .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)

                    //可以全局统一设置缓存模式,默认是不使用缓存,可以不传,具体其他模式看 github 介绍 https://github.com/jeasonlzy/
                    .setCacheMode(CacheMode.NO_CACHE)

                    //可以全局统一设置缓存时间,默认永不过期,具体使用方法看 github 介绍
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)

                    //可以全局统一设置超时重连次数,默认为三次,那么最差的情况会请求4次(一次原始请求,三次重连请求),不需要可以设置为0
                    .setRetryCount(3)

                    //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
                    //cookie持久化存储，如果cookie不过期，则一直有效
                    .setCookieStore(new PersistentCookieStore())

                    //可以设置https的证书,以下几种方案根据需要自己设置
                    .setCertificates();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invokeGet(Context context, String url, AbsCallback callback) {
        OkGo.get(url)
                .tag(context)
                .execute(callback);
    }

    @Override
    public void invokeGet(Context context, String url, Map<String, String> params, AbsCallback callback) {
        XLog.e("url", url);
        handleParams(context, params);
        OkGo.get(url)
                .tag(context)
                .params(params)
                .execute(callback);
    }


    @Override
    public void invokePost(Context context, String url, Map<String, String> params, AbsCallback callback) {

        PostRequest postRequest = OkGo.post(url).tag(context);
        if (!"http://gank.io/api/".equals(url)) {
            handleParams(context, params);
            addHeaders(postRequest, context);
        }
        postRequest.params(params);
        postRequest.execute(callback);
    }

    @Override
    public void invokePost(Context context, String url, String name, File file, AbsCallback callback) {

    }


    @Override
    public void invokePost(Context context, String url, Map<String, String> params, String fileName, String filePath, AbsCallback callback) {
        handleParams(context, params);
        PostRequest postRequest = OkGo.post(url).tag(context);
        postRequest.isMultipart(true);
        addHeaders(postRequest, context);
        postRequest.params(params);
        postRequest.params(fileName, new File(filePath));
        postRequest.execute(callback);
    }

    @Override
    public void cancelByTag(Object tag) {
        OkGo.getInstance().cancelTag(tag);
    }


    /**
     * 对参数进行签名
     *
     * @param context
     * @param params
     */
    private void handleParams(Context context, Map<String, String> params) {
        String token = SharedPref.getInstance(context).getString("token", "");
        String timestamp = String.valueOf(System.currentTimeMillis() + JsonCallback.deltaBetweenServerAndClientTime);
        params.put("token", token);
        params.put("timestamp", timestamp);
        String sign = null;
        try {
            sign = Md5SaltTool.getEncryptedPwd(SignUtils.createSign(params, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("sign", sign);
    }


    /**添加头部信息
     * @param request
     * @param context
     */
    private void addHeaders(BaseRequest request, Context context) {
        String oldAgent = request.getHeaders().get("User-Agent");

        String uid = "@" + SharedPref.getInstance(context).getString("phone", "");
        String version = "/Android" + Kits.Package.getVersionName(context);
        String device = "/" + DeviceUtils.getManufacturer() + "," + DeviceUtils.getModel() + "," + DeviceUtils.getSDKVersion();
        request.headers("User-Agent", oldAgent + uid + version + device);
    }

}
