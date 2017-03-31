package com.idogfooding.backbone;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.bumptech.glide.request.target.ViewTarget;
import com.tencent.bugly.crashreport.CrashReport;

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
        CrashReport.initCrashReport(getApplicationContext(), CfgConst.BUGLY_APPID, false);
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // auto gc when the memory is low
        System.gc();
    }

}
