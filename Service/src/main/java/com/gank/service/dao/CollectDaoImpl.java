package com.gank.service.dao;

import com.gank.service.bean.CollectBean;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import java.util.List;

/**
 * @author shijunxing
 */
public class CollectDaoImpl extends HibernateDaoSupport implements CollectDao {

    @Override
    public CollectBean collect(CollectBean collectBean) {
        String uid = collectBean.getUser_id();
        String id = collectBean.get_id();
        this.getHibernateTemplate().save(collectBean);
        return getCollect(uid, id);
    }

    @Override
    public CollectBean unCollect(String uid, String id) {
        CollectBean collectBean = getCollect(uid, id);
        if (collectBean != null) {
            getHibernateTemplate().delete(collectBean);
            return getCollect(uid, id);
        }
        return null;
    }

    @Override
    public boolean isCollect(String uid, String id) {
        return getCollect(uid, id) != null;
    }

    @Override
    public List<CollectBean> getCollects(final String uid, final int page, final int pageSize) {

        final String hql = "from CollectBean where user_id=" + uid;
        List<CollectBean> collectList = getHibernateTemplate().execute(session -> {
            Query query = session.createQuery(hql);
            int begin = (page - 1) * pageSize;
            query.setFirstResult(begin);
            query.setMaxResults(pageSize);

            return query.list();
        });
        return collectList;

    }

    private CollectBean getCollect(String uid, String id) {
        List<CollectBean> collectBeans = (List<CollectBean>) this.getHibernateTemplate().find("from CollectBean where user_id=? and _id=?", uid, id);
        if (collectBeans != null && collectBeans.size() > 0) {
            return collectBeans.get(0);
        }
        return null;
    }
}
