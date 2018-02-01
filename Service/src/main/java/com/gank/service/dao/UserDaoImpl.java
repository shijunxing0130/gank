package com.gank.service.dao;

import com.gank.service.bean.UserBean;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

/**
 *
 * @author shijunxing
 * @date 2017/10/12
 */
public class UserDaoImpl extends HibernateDaoSupport implements UserDao {

    @Override
    public UserBean login(String phone) {
        List<UserBean> users = (List<UserBean>) this.getHibernateTemplate().find(  "from UserBean where phone=?", phone);
        if(users != null && users.size() > 0)
        {
            return users.get(0);
        }
        return null;
    }

    @Override
    public boolean logout(String phone) {
        return false;
    }

    @Override
    public UserBean updateUser(UserBean userBean) {
        this.getHibernateTemplate().update(userBean);
        return getUser(userBean.getPhone());
    }

    @Override
    public UserBean register(UserBean userBean) {
        this.getHibernateTemplate().save(userBean);
        return getUser(userBean.getPhone());
    }

    @Override
    public UserBean getUser(String phone) {
        List<UserBean> users = (List<UserBean>) this.getHibernateTemplate().find("from UserBean where phone=?", phone);
        if(users != null && users.size() > 0)
        {
            return users.get(0);
        }
        return null;
    }
}
