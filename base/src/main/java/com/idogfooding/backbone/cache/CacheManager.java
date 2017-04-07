package com.idogfooding.backbone.cache;

import android.content.Context;
import android.content.pm.PackageManager;

import com.vincentbrison.openlibraries.android.dualcache.Builder;
import com.vincentbrison.openlibraries.android.dualcache.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.JsonSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * CacheManager
 *
 * @author Charles
 */
public class CacheManager {

    private static final int RAM_MAX_SIZE = 1024 * 1024 * 100; // 100 MB
    private static final int DISK_MAX_SIZE = 1024 * 1024 * 500; // 500 MB

    private static CacheManager sInst;

    private final Context mContext;
    private final int mAppVersionCode;
    private final HashMap<Class<?>, DualCache<?>> mCacheMap = new HashMap<>();

    private CacheManager(Context context) {
        mContext = context.getApplicationContext();
        int versionCode;
        try {
            versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = 1;
        }
        mAppVersionCode = versionCode;
    }

    public static synchronized CacheManager getInstance(Context context) {
        if (sInst == null) {
            sInst = new CacheManager(context);
        }
        return sInst;
    }

    public <T> DualCache<T> getCache(Class<T> clz) {
        synchronized (mCacheMap) {
            if (mCacheMap.containsKey(clz)) {
                return (DualCache<T>) mCacheMap.get(clz);
            }

            DualCache cache = new Builder<T>(clz.getName(), mAppVersionCode)
                    .enableLog()
                    .useReferenceInRam(RAM_MAX_SIZE, object -> {
                        try {
                            return sizeOfObject(object);
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .useSerializerInDisk(DISK_MAX_SIZE, true, new JsonSerializer<>(clz), mContext)
                    .build();

            mCacheMap.put(clz, cache);
            return cache;
        }
    }

    /**
     * Function that get the size of an object.
     *
     * @param object
     * @return Size in bytes of the object or -1 if the object is null.
     * @throws IOException
     */
    private int sizeOfObject(Object object) throws IOException {
        if (object == null)
            return -1;

        // Special output stream use to write the content
        // of an output stream to an internal byte array.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Output stream that can write object
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        // Write object and close the output stream
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();

        // Get the byte array
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return byteArray == null ? 0 : byteArray.length;
    }

}
