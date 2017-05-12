package com.idogfooding.backbone.statusbar;

import android.content.Context;

/**
 * StatusBarUtils
 *
 * @author Charles
 */
public class StatusBarUtils {

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

}
