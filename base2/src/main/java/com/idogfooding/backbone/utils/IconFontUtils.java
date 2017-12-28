package com.idogfooding.backbone.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * IconFontUtils
 * 在app中往往有需要图标和文字一起显示的时候，而为了偷懒不想重新修改布局，引入图片，就可以使用iconfont。
 *
 * 使用方法：
 * 1. 在http://iconfont.cn/等网站找到对于的图标，下载
 * 2. 将下载得到的iconfont.ttf放入assets文件夹中
 * 3. 在需要使用TextView的地方调用IconFontUtils(context, "iconfont.ttf", textview1, textview2);
 * 4. 字体使用需要用uniocode，如 当前位置 &#xe605; 宁波
 *
 * @author Charles
 */
public class IconFontUtils {

    /**
     * set icon font
     * @param context
     * @param fontName
     * @param views
     */
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
