package com.gank.service.config;

/**
 * @author shijunxing
 */
public class Config {

    /**
     * 请求过期时间
     */
    public static final long  REQUEST_TIME_LIMIT = 60*2;
    /**
     * token过期时间
     */
    public static final long  TOKEN_LIMIT = 60*60*24*60;

    /**
     * 签名key,与APP端的key一样
     */
    public static final String SIGN_KEY = "abc";

    /**
     * token key，自己定义
     */
    public static final String TOKEN_KEY = "02154778ke783dad34";
}
