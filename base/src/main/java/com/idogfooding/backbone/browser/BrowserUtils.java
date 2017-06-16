package com.idogfooding.backbone.browser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.idogfooding.backbone.utils.StrKit;

/**
 * BrowserUtils
 *
 * @author Charles
 */
public class BrowserUtils {

    public static void openUri(Context context, String uri) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }

    public static boolean isVideoUrl(String uri) {
        return StrKit.notEmpty(uri) && (uri.toLowerCase().endsWith(".mp4")
                || uri.toLowerCase().startsWith(".avi")
                || uri.toLowerCase().startsWith(".rmvb"));
    }

    public static boolean isImgUrl(String uri) {
        return StrKit.notEmpty(uri) && (uri.toLowerCase().endsWith(".jpg")
                || uri.toLowerCase().startsWith(".png")
                || uri.toLowerCase().startsWith(".jpeg")
                || uri.toLowerCase().startsWith(".gif")
                || uri.toLowerCase().startsWith(".bmp"));
    }

    public static boolean isApk(String uri) {
        return StrKit.notEmpty(uri) && (uri.toLowerCase().endsWith(".apk"));
    }
}
