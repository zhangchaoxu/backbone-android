package com.idogfooding.backbone.img;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.idogfooding.backbone.utils.FileUtils;

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
            long size = getFolderSize(new File(context.getCacheDir(), DiskCache.Factory.DEFAULT_DISK_CACHE_DIR));
            return FileUtils.formatSize(size);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * get all file size in folder
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
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
        deleteFolderFile(imageExternalCacheDir, true);
    }

    private void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
