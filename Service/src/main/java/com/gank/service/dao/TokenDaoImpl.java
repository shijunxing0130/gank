package com.gank.service.dao;

import com.gank.service.config.Config;
import com.gank.service.redis.RedisCacheStorage;
import com.gank.service.redis.RedisCacheStorageImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

/**
 * @author shijunxing
 * @date 2017/10/13
 */
public class TokenDaoImpl extends HibernateDaoSupport implements TokenDao {
    public static final Log LOG = LogFactory.getLog(TokenDaoImpl.class);
    private RedisCacheStorage<String, String> storageCache;

    public void setStorageCache(RedisCacheStorage<String, String> storageCache) {
        this.storageCache = storageCache;
    }

    @Override
    public void saveOrUpdateToken(String token, String uid) {
        if (!token.isEmpty() && !uid.isEmpty()) {
            storageCache.set(token, uid, (int) Config.TOKEN_LIMIT);
        }
    }

    @Override
    public boolean deleteToken(String token) {
        return storageCache.remove(token);
    }

    @Override
    public boolean isTokenAvailable(String token) {
        LOG.info(token);
        System.out.println("token:" + token);
        if (storageCache.get(token) != null) {
            System.out.println("redis-id:" + storageCache.get(token));
        }
        return !token.isEmpty() && storageCache.get(token) != null && !"".equals(storageCache.get(token));
    }
}
