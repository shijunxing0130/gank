package com.gank.service.service;

import com.gank.service.bean.RestFulBean;
import com.gank.service.bean.UserBean;
import com.gank.service.config.Config;
import com.gank.service.dao.TokenDao;
import com.gank.service.dao.UserDao;
import com.gank.service.util.Md5SaltTool;
import com.gank.service.util.StringUtil;
import com.gank.service.util.TimeUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Properties;

/**
 * @author shijunxing
 * @date 2017/10/12
 */
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenDao tokenDao;

    public RestFulBean<UserBean> register(UserBean userBean) {
        UserBean user = userDao.getUser(userBean.getPhone());
        if (user != null) {
            return new RestFulBean<>(null, 1, "此号码已注册");
        } else {
            user = userDao.register(userBean);
            if (user != null) {
                saveOrUpdateToken(user);
                return new RestFulBean<>(user, 0, "注册成功");
            } else {
                return new RestFulBean<>(null, 2, "注册失败");
            }
        }

    }

    public RestFulBean<UserBean> login(String phone, String password) {
        UserBean user = userDao.login(phone);
        if (user != null) {
            if (password.equals(user.getPassword())) {
                saveOrUpdateToken(user);
                return new RestFulBean<>(user, 0, "登录成功");
            } else {
                return new RestFulBean<>(null, 1, "密码有误");
            }

        } else {
            return new RestFulBean<>(null, 2, "此账号不存在");
        }
    }


    public RestFulBean<UserBean> logout(String token) {
        if (tokenDao.deleteToken(token)) {
            return new RestFulBean<>(null, 0, "退出成功");
        } else {
            return new RestFulBean<>(null, 1, "退出失败");
        }
    }

    public RestFulBean<UserBean> userInfo(String phone) {
        UserBean userBean = userDao.getUser(phone);
        if (userBean != null) {
            return new RestFulBean<>(userBean, 0, "获取用户信息成功");
        }
        return new RestFulBean<>(null, 1, "获取用户信息失败");
    }


    public RestFulBean<UserBean> updatePwd(String phone, String password){
        UserBean user = userDao.getUser(phone);
        if (user != null) {
            user.setPassword(password);
            if (userDao.updateUser(user) != null) {
                return new RestFulBean<>(user, 0, "修改密码成功");
            }else {
                return new RestFulBean<>(null, 1, "修改密码失败");
            }
        }else {
            return new RestFulBean<>(null, 2, "修改密码失败");
        }
    }

    /**
     * 上传头像
     *
     * @param file
     * @param phone
     * @return
     * @throws Exception
     */
    public RestFulBean<UserBean> uploadHead(MultipartFile file, String phone) throws Exception {
        //过滤合法的文件类型
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String allowSuffixs = "gif,jpg,jpeg,bmp,png,ico";
        if (!allowSuffixs.contains(suffix)) {
            return new RestFulBean<>(null, 1, "图片格式不支持");
        }

        //获取网络地址、本地地址头部
        Properties config = new Properties();
        config.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
        String urlPath = config.getProperty("urlRoot");
        String localPath = config.getProperty("localRoot");

        //创建新目录
        String uri = File.separator + TimeUtils.getNowDateStr(File.separator);
        File dir = new File(localPath + uri);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //创建新文件
        String newFileName = StringUtil.getUniqueFileName();
        File f = new File(dir.getPath() + File.separator + newFileName + "." + suffix);

        //将输入流中的数据复制到新文件
        FileUtils.copyInputStreamToFile(file.getInputStream(), f);

        //保存头像地址到用户表
        UserBean userBean = userDao.getUser(phone);
        if (userBean != null) {
            userBean.setUserhead(urlPath + uri.replace("\\", "/") + "/" + newFileName + "." + suffix);
            UserBean updateBean = userDao.updateUser(userBean);
            if (updateBean != null) {
                return new RestFulBean<>(updateBean, 0, "头像上传成功");
            } else {
                return new RestFulBean<>(null, 2, "头像上传失败");
            }

        } else {
            return new RestFulBean<>(null, 3, "头像上传失败");
        }


    }


    private void saveOrUpdateToken(UserBean userBean) {
        String token = null;
        try {
            token = Md5SaltTool.getEncryptedPwd(String.valueOf(System.currentTimeMillis()) + Config.TOKEN_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userBean.setToken(token);
        tokenDao.saveOrUpdateToken(token, userBean.getPhone());
    }

}
