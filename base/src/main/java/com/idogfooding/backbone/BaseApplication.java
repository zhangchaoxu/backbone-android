package com.idogfooding.backbone;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Looper;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.facebook.stetho.Stetho;

import java.util.List;

/**
 * Base Application
 *
 * @author Charles
 */
public class BaseApplication extends Application {

    static Context _context;
    static Resources _resource;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
        _resource = _context.getResources();

        // fix glide issue "You must not call setTag() on a view Glide is targeting"
        // https://github.com/bumptech/glide/issues/370
        // http://stackoverflow.com/questions/34833627/error-you-must-not-call-settag-on-a-view-glide-is-targeting-when-use-glide/35096552
        ViewTarget.setTagId(R.id.glide_tag);

        // init bugly
        // CrashReport.initCrashReport(getApplicationContext());

        // init stetho
        if (isDebug()) {
            Stetho.initializeWithDefaults(this);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // add multidex support
        MultiDex.install(this);
    }

    public static synchronized Context context() {
        return _context;
    }

    public static synchronized Resources resource() {
        return _resource;
    }

    protected boolean isProcessInRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : appList) {
            if (info.pid == android.os.Process.myPid()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isDebug() {
        return true;
    }

    /**
     * clear glide cache
     */
    public boolean clearGlideCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(() -> Glide.get(_context).clearDiskCache());
            } else {
                Glide.get(_context).clearDiskCache();
            }
            Glide.get(this).clearMemory();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // auto gc when the memory is low
        System.gc();
    }

}
