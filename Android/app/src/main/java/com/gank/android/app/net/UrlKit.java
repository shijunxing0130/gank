package com.gank.android.app.net;

/**
 *
 * @author shijunxing
 * @date 2016/12/9
 */

public class UrlKit {

    /**
     * gank 用户api接口地址
     */
    public static final String GANK_USER_BASE_URL = "http://192.168.0.111:8080/";
//    public static final String GANK_USER_BASE_URL = "https://www.alisjx.top/gank/";

    /**
     * gank 干货api接口地址
     */
    public static final String GANK_API_BASE_URL = "http://gank.io/api/";



    /*******************************************用户*************************************************/

    public static final String API_REGISTER = "user/register";
    public static final String API_LOGIN    = "user/login";
    public static final String API_LOGOUT   = "user/logout";
    public static final String API_INFO     = "user/info";
    public static final String API_UPLOAD   = "user/upload";
    public static final String API_UPDATE_PWD   = "user/updatePwd";


    /*********************************************收藏*************************************************/

    public static final String API_COLLECT    = "collect/collect";
    public static final String API_IS_COLLECT = "collect/isCollect";
    public static final String API_UN_COLLECT = "collect/unCollect";
    public static final String API_COLLECTS   = "collect/getCollects";

    /*********************************************干货*************************************************/

    /**
     * 每日数据： http://gank.io/api/day/年/月/日
     */
    public static final String API_TODAY = "day/";
    /**
     * 分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
     */
    public static final String API_CLASSIFY = "data/";
    /**
     * 获取发过干货日期接口:
     */
    public static final String API_HISTORY = "day/history ";

    /**
     * http://gank.io/api/search/query/listview/category/Android/count/10/page/1
     注：
     category 后面可接受参数 all | Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App
     count 最大 50
     */
    public static final String API_SEARCH = "search/query/{keyword}/category/{category}/count/20/page/{pageIndex}";

    /**
     * 提交干货到审核区post
     */
    public static final String API_ADD = "add2gank";

    public static String getUrl(String action) {
        return new StringBuilder(GANK_API_BASE_URL).append(action).toString();
    }

    public static String getUserUrl(String action) {
        return new StringBuilder(GANK_USER_BASE_URL).append(action).toString();
    }
}
