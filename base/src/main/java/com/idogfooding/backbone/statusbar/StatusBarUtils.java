package com.idogfooding.backbone.statusbar;

import android.content.Context;

/**
 * StatusBarUtils
 *
 * @author Charles
 */
public class StatusBarUtils {

    /**
     * get height of status bar
     *
     * see {http://www.jianshu.com/p/a44c119d6ef7}
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

}
