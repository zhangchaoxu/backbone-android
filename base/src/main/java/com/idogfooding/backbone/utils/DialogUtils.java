package com.idogfooding.backbone.utils;

import android.app.Dialog;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * DialogUtils
 *
 * @author Charles
 */
public class DialogUtils {

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * Simple dialog builder with default buttons.
     */
    public static MaterialDialog.Builder getSimpleDialogBuilder(Context context, String text) {
        return new MaterialDialog.Builder(context)
                .content(text)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel);
    }

    public static MaterialDialog.Builder getSimpleDialogBuilder(Context context) {
        return new MaterialDialog.Builder(context)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel);
    }

}
