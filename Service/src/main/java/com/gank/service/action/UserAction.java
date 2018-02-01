package com.gank.service.action;

import com.gank.service.bean.RestFulBean;
import com.gank.service.bean.UserBean;
import com.gank.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ywl
 * @date 2017-10-2
 */
@Controller
@RequestMapping("/user")
public class UserAction {

    @Autowired
    private UserService userService;

    /**
     * 注册
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RestFulBean<UserBean> register(String phone,
                                          String name,
                                          String password,
                                          String version,
                                          String device ) throws Exception {
        UserBean userBean = new UserBean();
        userBean.setPhone(phone);
        userBean.setUsername(name);
        userBean.setPassword(password);
        userBean.setDevice(device);
        userBean.setVersion(version);
        return userService.register(userBean);
    }

    /**
     * 登录
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RestFulBean<UserBean> loginByPwd(String phone, String password) {
        return userService.login(phone, password);
    }


    /**
     * 修改密码
     */
    @ResponseBody
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public RestFulBean<UserBean> updatePwd(String phone, String password) {
        return userService.updatePwd(phone, password);
    }

    /**
     * 详情
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public RestFulBean<UserBean> userInfo(String phone) {
        return userService.userInfo(phone);
    }

    /**
     * 上传头像
     */
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public RestFulBean<UserBean> uploadHead(MultipartFile file,String phone) {
        try {
            return userService.uploadHead(file,phone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestFulBean<>(null, -1,"服务器异常");
    }
    /**
     * 退出
     */
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public RestFulBean<UserBean> logout(String token) {
        return userService.logout(token);
    }

}
