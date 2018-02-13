package cn.gank.androidlibs;

/**
 * Created by wanglei on 2016/12/4.
 */

public class XConf {
    // #debug

    public static final boolean IS_DEBUG = true;

    // #log
    public static final boolean LOG = IS_DEBUG;
    public static final String LOG_TAG = "gank";

    // #cache
    public static final String CACHE_SP_NAME = "config";
    public static final String CACHE_DISK_DIR = "cache";

    /**
     * 参数签名key，要与服务器的一致
     */
    public static final String SIGN_KEY = "sss";


}
