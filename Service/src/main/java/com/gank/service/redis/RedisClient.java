package com.gank.service.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis 客户端封装
 * @author leo
 */
public class RedisClient {
    /**
     * 日志记录
     */
    private static final Log LOG = LogFactory.getLog(RedisClient.class);
    /**
     * redis 连接池
     */
    private JedisPool pool;

    public void setPool(JedisPool pool) {
        this.pool = pool;
    }

    /**
     * 获取jedis
     *
     * @return
     */
    public synchronized Jedis getResource() {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
        } catch (Exception e) {
            LOG.info("can't  get  the redis resource");
        }
        return jedis;
    }

    /**
     * 关闭连接
     *
     * @param jedis
     */
    public void disconnect(Jedis jedis) {
        jedis.disconnect();
    }

    /**
     * 将jedis 返还连接池
     *
     * @param jedis
     */
    public void returnResource(Jedis jedis) {
        if (null != jedis) {
            try {
                pool.returnResource(jedis);
            } catch (Exception e) {
                LOG.info("can't return jedis to jedisPool");
            }
        }
    }

    /**
     * 无法返还jedispool，释放jedis客户端对象，
     *
     * @param jedis
     */
    public void brokenResource(Jedis jedis) {
        if (jedis != null) {
            try {
                pool.returnBrokenResource(jedis);
            } catch (Exception e) {
                LOG.info("can't release jedis Object");
            }
        }
    }
}