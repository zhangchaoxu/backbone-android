package com.idogfooding.backbone.utils;

import android.content.Context;

/**
 * ViewUtils
 *
 * @author Charles
 */
public class ViewUtils {

    /**
     * dp2px
     */
    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * p2תdp
     */
    public static int px2dip(Context ctx, float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
