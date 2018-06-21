package com.idogfooding.backbone.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 浏览器工具
 *
 * @author Charles
 */
public class BrowserUtils {

    /**
     * 系统自带浏览器打开链接
     */
    public static void openUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(intent);
    }

    /**
     * 系统自带浏览器打开链接
     */
    public static void openUrl(android.app.Fragment fragment, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        fragment.startActivity(intent);
    }

    /**
     * 系统自带浏览器打开链接
     */
    public static void openUrl(android.support.v4.app.Fragment fragment, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        fragment.startActivity(intent);
    }

    /**
     * 系统自带浏览器打开链接
     */
    public static void openUrl(Activity activity, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

    public static boolean isApkLink(String url) {
        return url.toLowerCase().endsWith(".apk");
    }

}
