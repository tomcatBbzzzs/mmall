package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author 唐孟廷
 * @desc RedisPool工具类, 简化redis操作
 * @date 2020/5/12 - 16:28
 */
@Slf4j
public class RedisPoolUtil {

    /**
     * 设置指定的key的值为value
     *
     * @param key   键
     * @param value 值
     * @return 返回修改后的状态,成功为OK
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("RedisPoolUtil.set(k,v)异常: set key:{} value:{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 删除redis中的key
     *
     * @param key 键
     * @return 删除redis中的key的状态,成功返回1,失败返回0
     */
    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("RedisPoolUtil.get(k)异常: get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 获取指定的key在redis中的值
     *
     * @param key 键
     * @return 返回redis中的key的值
     */
    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("RedisPoolUtil.get(k)异常: get key:{} error", key, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }


    /**
     * 创建key并指定它的生存时间
     *
     * @param key    键
     * @param value  值
     * @param exTime 有效时间,单位是s
     * @return 返回修改状态,成功返回OK
     */
    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("RedisPoolUtil.setEx(key,value,time)异常: set key:{} value:{} exTime:{} error", key, value, exTime, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    /**
     * 设置key的生成时间
     *
     * @param key    指定键
     * @param exTime 生存时间,单位是s
     * @return 返回操作是否生效, 生效返回1, 不生效返回0
     */
    public static Long expire(String key, int exTime) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("RedisPoolUtil.expire(key, time)异常: set key:{}  exTime:{} error", key, exTime, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        // OK
        System.out.println(set("a", "res_a"));

        // 1
        System.out.println(expire("a", 100));

        // res_a
        System.out.println(get("a"));

        // OK
        System.out.println(setEx("a", "red_b", 100));

        // 1
        System.out.println(del("a"));
    }
}
