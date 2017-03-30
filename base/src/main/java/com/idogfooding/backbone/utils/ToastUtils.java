package com.idogfooding.backbone.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.idogfooding.backbone.BaseApplication;

/**
 * ToastUtils
 *
 * @author Charles
 */
public class ToastUtils {

    public static void show(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int resId, int duration) {
        Toast.makeText(BaseApplication.context(), resId, duration).show();
    }

    public static void show(String toastStr) {
        show(toastStr, Toast.LENGTH_SHORT);
    }

    public static void show(String toastStr, int duration) {
        Toast.makeText(BaseApplication.context(), toastStr, duration).show();
    }
}
