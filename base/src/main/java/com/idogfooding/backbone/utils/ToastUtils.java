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

    private static String oldMsg;
    private static Toast toast = null;
    /**
     * first show time
     */
    private static long firstToastTime = 0;
    /**
     * second show time
     */
    private static long secondToastTime = 0;

    public static void show(@StringRes int resId) {
        show(resId, Toast.LENGTH_SHORT);
    }

    public static void show(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void show(@StringRes int resId, int duration) {
        show(BaseApplication.context().getString(resId), duration);
    }

    public static void show(String msg, int duration) {
        if (StrKit.isEmpty(msg))
            return;

        if (toast == null) {
            toast = Toast.makeText(BaseApplication.context(), msg, duration);
            toast.show();
            firstToastTime = System.currentTimeMillis();
        } else {
            secondToastTime = System.currentTimeMillis();
            if (msg.equals(oldMsg)) {
                if (secondToastTime - firstToastTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = msg;
                toast.setText(msg);
                toast.show();
            }
        }
        firstToastTime = secondToastTime;
    }
}
