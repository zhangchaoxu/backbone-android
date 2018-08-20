package com.idogfooding.backbone.network;

import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.db.CacheManager;

/**
 * OkGo Cache Utils
 *
 * @author Charles
 */
@SuppressWarnings("unchecked")
public class OkGoCacheUtils {

    /**
     * 通过key判断是否存在缓存数据
     * @param key
     * @param <T>
     * @return
     */
    public static <T> boolean hasHttpResultCache(String key) {
        try {
            CacheEntity<HttpResult<T>> cacheEntity = (CacheEntity<HttpResult<T>>) CacheManager.getInstance().get(key);
            return cacheEntity != null && cacheEntity.getData() != null && cacheEntity.getData().getData() != null;
        } catch (ClassCastException e) {
            return false;
        }
    }

    /**
     * 通过key获取缓存数据
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getHttpResultCache(String key) {
        try {
            CacheEntity<HttpResult<T>> cacheEntity = (CacheEntity<HttpResult<T>>) CacheManager.getInstance().get(key);
            if (cacheEntity == null || cacheEntity.getData() == null || cacheEntity.getData().getData() == null)
                return null;
            else
                return cacheEntity.getData().getData();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过key判断是否存在缓存数据
     * @param key
     * @param <T>
     * @return
     */
    public static <T> boolean hasCache(String key) {
        try {
            CacheEntity<T> cacheEntity = (CacheEntity<T>) CacheManager.getInstance().get(key);
            return cacheEntity != null && cacheEntity.getData() != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 通过key获取缓存数据
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getCache(String key) {
        try {
            CacheEntity<T> cacheEntity = (CacheEntity<T>) CacheManager.getInstance().get(key);
            if (cacheEntity == null || cacheEntity.getData() == null)
                return null;
            else
                return cacheEntity.getData();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过key删除缓存数据
     * @param key
     * @return
     */
    public static boolean removeCache(String key) {
        try {
            return CacheManager.getInstance().remove(key);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除所有数据
     * @return
     */
    public static boolean clear() {
        try {
            return CacheManager.getInstance().clear();
        } catch (Exception e) {
            return false;
        }
    }
}