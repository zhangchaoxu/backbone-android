package com.idogfooding.backbone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.CrashUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.request.target.ViewTarget;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.util.TextInfo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

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

        // init utils
        Utils.init(this);
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
        return this.getSPInstance("app");
    }

    protected SPUtils getSPInstance(String spName) {
        if (_spInstance == null) {
            _spInstance = SPUtils.getInstance(spName);
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

    /**
     * 初始化网络日志
     */
    protected void initOkHttp() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .build();
        OkGo.getInstance()
                .init(this)
                .setOkHttpClient(okHttpClient);
    }

    protected void initDialog() {
        this.initDialog(18, 16);
    }
    /**
     * 设置对话框
     */
    protected void initDialog(int titleSize, int btnSize) {
        // init dialog
        DialogSettings.DEBUGMODE = false;
        DialogSettings.isUseBlur = false;
        DialogSettings.cancelableTipDialog = false;
        DialogSettings.cancelable = false;
        DialogSettings.style = DialogSettings.STYLE.STYLE_KONGZUE;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
        DialogSettings.tipTheme = DialogSettings.THEME.LIGHT;
        // 对话框标题文字样式
        DialogSettings.titleTextInfo = new TextInfo().setFontColor(color(R.color.black1)).setFontSize(titleSize);
        // 对话框内容文字样式
        DialogSettings.contentTextInfo = new TextInfo().setFontColor(color(R.color.black1)).setFontSize(btnSize);
        //全局默认按钮文字样式
        DialogSettings.buttonTextInfo = new TextInfo().setFontColor(color(R.color.black1)).setFontSize(btnSize);
        // 全局焦点按钮文字样式（一般指确定按钮）
        DialogSettings.buttonPositiveTextInfo = new TextInfo().setFontColor(color(R.color.black1)).setFontSize(btnSize);
    }

    /**
     * 获取颜色
     */
    @ColorInt
    protected int color(@ColorRes int res) {
        return getResources().getColor(res);
    }

    /**
     * 初始化日志
     */
    protected void initLog() {
        LogUtils.getConfig()
                .setLogSwitch(BuildConfig.DEBUG)
                .setConsoleSwitch(BuildConfig.DEBUG)
                .setLogHeadSwitch(true)
                .setLog2FileSwitch(true)
                .setFilePrefix("app")
                .setBorderSwitch(true)
                .setSingleTagSwitch(true)
                .setConsoleFilter(LogUtils.V)
                .setFileFilter(LogUtils.D)
                .setStackDeep(1)
                .setStackOffset(0)
                .setSaveDays(30);
    }

    /**
     * 初始化crash
     */
    @SuppressLint("MissingPermission")
    protected void initCrash(boolean reload) {
        CrashUtils.init((crashInfo, e) -> {
            LogUtils.e(crashInfo);
            if (reload) {
                // 重启app
                AppUtils.relaunchApp();
            }
        });
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
