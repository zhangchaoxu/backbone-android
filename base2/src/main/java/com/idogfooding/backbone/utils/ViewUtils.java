package com.idogfooding.backbone.utils;

import android.os.Build;
import android.support.annotation.IdRes;
import android.view.View;

import java.util.UUID;

/**
 * ViewUtils
 *
 * @author Charles
 */
public class ViewUtils {

    /**
     * 生成view id
     *
     * @return 生产的viewid
     */
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId();
        } else {
            return UUID.randomUUID().hashCode();
        }
    }

    public static <T> T getTagValue(View view, T defaultValue) {
        return getTagValue(view, -1, defaultValue);
    }

    /**
     * 获取view tag的值
     *
     * @param view
     * @param key
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T getTagValue(View view, @IdRes int key, T defaultValue) {
        try {
            Object object = (-1 == key) ? view.getTag() : view.getTag(key);
            return null == object ? defaultValue : (T) object;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getTagValue(View view, int defaultValue) {
        return getTagValue(view, -1, defaultValue);
    }

    /**
     * 获取view tag的值
     *
     * @param view
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getTagValue(View view, @IdRes int key, int defaultValue) {
        try {
            Object object = (-1 == key) ? view.getTag() : view.getTag(key);
            if (null == object) {
                return defaultValue;
            } else {
                if (object instanceof String) {
                    return NumberUtils.parseToInt((String) object, defaultValue);
                } else {
                    return (int) object;
                }
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

}
