package cn.gank.androidlibs.log;

import com.orhanobut.logger.Logger;

import cn.gank.androidlibs.XConf;

/**
 * Created by wanglei on 2016/11/29.
 */

public class XLog {

    public static boolean LOG = XConf.LOG;
    public static String TAG_ROOT = XConf.LOG_TAG;


    /**
     * Debug
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (LOG) {
            Logger.t(tag).d(msg);
        }
    }
    /**
     * Debug
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, Object msg) {
        if (LOG) {
            Logger.t(tag).d(msg);
        }
    }
    /**
     * Debug
     *
     * @param tag
     * @param msg
     */
    public static void json(String tag, String msg) {
        if (LOG) {
            Logger.t(tag).json(msg);
        }
    }
    /**
     * Information
     *
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (LOG) {
            Logger.t(tag).i(msg);
        }
    }
    /**
     * Verbose
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (LOG) {
            Logger.t(tag).v(msg);
        }
    }
    /**
     * Warning
     *
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (LOG) {
            Logger.t(tag).w(msg);
        }
    }
    /**
     * Error
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (LOG) {
            Logger.t(tag).e(msg);
        }
    }

}
