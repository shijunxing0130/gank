package com.gank.service.dao;



/**
 *
 * @author shijunxing
 * @date 2017/10/13
 */
public interface TokenDao {

     /**
     * 登录或注册时写入token
     */
    void saveOrUpdateToken(String token,String uid);


    /**
     * 退出时删除token
     * @param token
     */
    boolean deleteToken(String token);
    /**
     * 根据token获取电话号码
     * @param token
     * @return
     */
    boolean isTokenAvailable(String token);

}
