package cn.gank.androidlibs.httphelper;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.BaseRequest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import cn.gank.androidlibs.log.XLog;
import okhttp3.Response;

/**
 * ================================================
 *
 * ================================================
 * @author shijunxing
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    /**
     * 服务器时间和客户端时间的差值
     */
    public static long deltaBetweenServerAndClientTime;

    /**
     * 更新服务器时间和本地时间的差值
     */
    private void updateDeltaBetweenServerAndClientTime(Response response) {

        if (response != null) {
            final String strServerDate = response.header("Date");
            try {
                if ((strServerDate != null) && !strServerDate.equals("")) {
                    final SimpleDateFormat sdf = new SimpleDateFormat(
                            "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
                    TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));

                    Date serverDateUAT = sdf.parse(strServerDate);

                    deltaBetweenServerAndClientTime = serverDateUAT
                            .getTime()
                            + 8 * 60 * 60 * 1000
                            - System.currentTimeMillis();
                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现


    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertSuccess(Response response) throws Exception {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 如果你对这里的代码原理不清楚，可以看这里的详细原理说明：https://github.com/jeasonlzy/okhttp-OkGo/blob/master/README_JSONCALLBACK.md
        //以下代码是通过泛型解析实际参数,泛型必须传
        //这里为了方便理解，假如请求的代码按照上述注释文档中的请求来写，那么下面分别得到是
        Type genType = getClass().getGenericSuperclass();
        //从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        Type type = params[0];

        // 这里这么写的原因是，我们需要保证上面我解析到的type泛型，仍然还具有一层参数化的泛型，也就是两层泛型
        // 如果你不喜欢这么写，不喜欢传递两层泛型，那么以下两行代码不用写，并且javabean按照
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalStateException("没有填写泛型参数");
        }
        //如果确实还有泛型，那么我们需要取出真实的泛型，得到如下结果
        //此时，rawType的类型实际上是 class，但 Class 实现了 Type 接口，所以我们用 Type 接收没有问题
        Type rawType = ((ParameterizedType) type).getRawType();
        //这里获取最终内部泛型的类型
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

        //这里我们既然都已经拿到了泛型的真实类型，即对应的 class ，那么当然可以开始解析数据了，我们采用 Gson 解析
        //以下代码是根据泛型解析数据，返回对象，返回的对象自动以参数的形式传递到 onSuccess 中，可以直接使用
        String string = response.body().string();
        XLog.d("response_string",string);
        XLog.json("response_json",string);
        updateDeltaBetweenServerAndClientTime(response);
        if (typeArgument == Void.class) {
            //无数据类型,表示没有data数据的情况（以  new DialogCallback<LzyResponse<Void>>(this)  以这种形式传递的泛型)
            SimpleResponse simpleResponse = Convert.fromJson(string, SimpleResponse.class);
            response.close();
            //noinspection unchecked
            return (T) simpleResponse.toBaseResponse();
        } else if (rawType == BaseResponse.class) {
            //有数据类型，表示有data
            BaseResponse bsaeResponse = Convert.fromJson(string, type);
            response.close();
            return (T) bsaeResponse;

        } else {
            response.close();
            throw new IllegalStateException("基类错误无法解析!");
        }
    }
}