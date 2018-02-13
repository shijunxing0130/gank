package cn.gank.androidlibs.httphelper;

/**
 * Created by Administrator on 2016/12/29.
 */
public class HttpFactory {

    private static IHttp helper;

    public static IHttp getHelper() {
        if (helper == null) {
            synchronized (HttpFactory.class) {
                if (helper == null) {
                    helper = new OKHelper();
                }
            }
        }
        return helper;
    }
}
