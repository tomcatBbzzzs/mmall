package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author 唐孟廷
 * @desc 单机缓存工具类
 * @date 2020/5/3 - 9:58
 */
public class TokenCache {
    private static Logger log = LoggerFactory.getLogger(TokenCache.class);

    /**
     * 默认的NULL表示
     */
    private static final String NULL = "null";

    /**
     * token前缀
     */
    private static final String TOKEN_PREFIX = "token_";

    // LRU算法
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        /**
         * 默认的数据加载实现, 当调用get取值的时候, 如果key没有对应的值, 则使用这个方法进行加载
         * @param key
         * @return
         * @throws Exception
         */
        @Override
        public String load(String key) throws Exception {
            return NULL;
        }
    });

    /**
     * 添加缓存
     *
     * @param key   键
     * @param value 值
     */
    public static void setKey(String key, String value) {
        localCache.put(TOKEN_PREFIX + key, value);
    }

    /**
     * 根据键获取缓存
     *
     * @param key 键
     * @return 返回对应的值
     */
    public static String getKey(String key) {
        String value = null;
        try {
            value = localCache.get(TOKEN_PREFIX + key);
            if (NULL.equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            log.error("本地缓存获取key异常", e);
        }
        return null;
    }
}
