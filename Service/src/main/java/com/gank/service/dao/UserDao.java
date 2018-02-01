package com.gank.service.dao;


import com.gank.service.bean.UserBean;

/**
 * Created by ywl5320 on 2017/10/12.
 */
public interface UserDao {

    /**
     * 更新用户
     * @param userBean
     */
    UserBean updateUser(UserBean userBean);/**
     * 注册
     * @param userBean
     */
    UserBean register(UserBean userBean);

    /**
     * 登陆
     * @return
     */
    UserBean login(String phone);

    /**
     * 登出
     * @return
     */
    boolean logout(String phone);

    /**
     * 根据名字获取用户信息
     * @param phone
     * @return
     */
    UserBean getUser(String phone);

}
