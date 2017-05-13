package com.idogfooding.backbone.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * IconFontUtils
 *
 * @author Charles
 */
public class IconFontUtils {

    public static void setIconFont(Context context, String fontName, TextView... views) {
        if (null == context || views.length == 0 || TextUtils.isEmpty(fontName))
            return;

        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), fontName);
        if (null == iconfont)
            return;

        for (TextView view : views) {
            view.setTypeface(iconfont);
        }
    }

}
