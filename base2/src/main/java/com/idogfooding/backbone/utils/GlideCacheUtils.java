package com.idogfooding.backbone.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;

import java.io.File;

/**
 * GlideCacheUtils
 *
 * @author Charles
 */
public class GlideCacheUtils {

    private static GlideCacheUtils instance;

    public static GlideCacheUtils getInstance() {
        if (instance == null) {
            instance = new GlideCacheUtils();
        }
        return instance;
    }

    /**
     * get cache size
     */
    public String getCacheSize(Context context) {
        try {
            return FileUtils.getDirSize(new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * clear cache in disk
     */
    public void clearDiskCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(() -> {
                    Glide.get(context).clearDiskCache();
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clear cache in memory
     */
    public void clearMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clear all cache
     */
    public void clearAllCache(Context context) {
        clearDiskCache(context);
        clearMemoryCache(context);
        String imageExternalCacheDir = context.getExternalCacheDir() + "/" + DiskCache.Factory.DEFAULT_DISK_CACHE_DIR;
        FileUtils.deleteDir(imageExternalCacheDir);
    }


}
