package com.mmall.common;

import com.mmall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author 唐孟廷
 * @desc redis常量池
 * @date 2020/5/12 - 15:24
 */
public class RedisPool {
    /**
     * jedis连接池
     */
    private static JedisPool pool;

    /**
     * 最大连接数
     */
    private static Integer maxTotal;

    /**
     * 最大空闲连接数
     */
    private static Integer maxIdle;

    /**
     * 最小空闲连接数
     */
    private static Integer minIdle;

    /**
     * 在获取一个jedis实例的时候,是否需要进行验证操作.
     * 如果赋值为true, 则得到的jedis实例是一定可以使用的
     */
    private static Boolean testOnBorrow;

    /**
     * 在归还一个jedis实例的时候,是否需要进行验证操作.
     * 如果赋值为true, 则归还的jedis实例是一定可以使用的
     */
    private static Boolean testOnReturn;


    /**
     * redis服务器ip地址
     */
    private static String ip;

    /**
     * redis服务器端口号
     */
    private static int port;

    static {
        maxTotal = PropertiesUtil.getProperty("redis.max.total", 20);
        maxIdle = PropertiesUtil.getProperty("redis.max.idle", 10);
        minIdle = PropertiesUtil.getProperty("redis.min.idle", 2);
        testOnBorrow = PropertiesUtil.getProperty("redis.test.borrow", true);
        testOnReturn = PropertiesUtil.getProperty("redis.test.return", false);
        ip = PropertiesUtil.getProperty("redis.ip");

        // 如果没有配置端口,直接抛出异常
        port = PropertiesUtil.getProperty("redis.port", -999);

        // 效验参数值是否合法
        if (!(maxTotal >= maxIdle && maxIdle >= minIdle && port > 0)) {
            throw new IllegalArgumentException("初始化RedisPool失败,传入参数异常...");
        }

        // 初始化连接池
        initPool();
    }


    /**
     * 初始化连接池
     */
    public static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        // 连接耗尽时,是否阻塞.
        //  true   阻塞直到超时.
        //  false  直接抛出异常
        config.setBlockWhenExhausted(true);

        // 默认超时时间
        int timeout = 1000 * 2;

        pool = new JedisPool(config, ip, port, timeout);
    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    /**
     * 将jedis返回连接池
     */
    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);
    }

    /**
     * 将jedis放回来连接池,并检查连接是否可用
     * 这里我没有判断是否可用是因为我调用的returnBrokenResource这个
     * 方法已经有判断连接是否可用的逻辑了
     */
    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        jedis.set("mini", "12");

        RedisPool.returnResource(jedis);
    }

}
