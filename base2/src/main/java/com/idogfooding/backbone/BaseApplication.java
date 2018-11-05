package com.idogfooding.backbone;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.request.target.ViewTarget;

import java.util.List;

/**
 * Base Application
 *
 * @author Charles
 */
public class BaseApplication extends Application {

    private static final String TAG = "BaseApplication";
    private static BaseApplication sInstance;

    static Context _context;
    static Resources _resource;
    static SPUtils _spInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        // register activity lifecycle callback
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);

        _context = getApplicationContext();
        _resource = _context.getResources();

        // fix glide issue "You must not call setTag() on a view Glide is targeting"
        // https://github.com/bumptech/glide/issues/370
        // http://stackoverflow.com/questions/34833627/error-you-must-not-call-settag-on-a-view-glide-is-targeting-when-use-glide/35096552
        ViewTarget.setTagId(R.id.glide_tag);
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

    public static BaseApplication getInstance() {
        return sInstance;
    }

    /**
     * get SP Utils instance
     */
    protected SPUtils getSPInstance() {
        if (_spInstance == null) {
            _spInstance = SPUtils.getInstance();
        }
        return _spInstance;
    }

    /**
     * is app process in running
     */
    protected boolean isProcessInRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (null == activityManager)
            return false;

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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // auto gc when the memory is low
        System.gc();
    }

    private ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(TAG, "onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "onActivityStarted() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG, "onActivityResumed() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG, "onActivityPaused() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "onActivityStopped() called with: activity = [" + activity + "]");
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(TAG, "onActivitySaveInstanceState() called with: activity = [" + activity + "], outState = [" + outState + "]");
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "onActivityDestroyed() called with: activity = [" + activity + "]");
        }
    };

}
