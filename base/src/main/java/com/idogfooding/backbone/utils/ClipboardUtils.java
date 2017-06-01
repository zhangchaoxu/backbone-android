package com.idogfooding.backbone.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.idogfooding.backbone.R;

/**
 * ClipboardUtils
 *
 * @author Charles
 */
public class ClipboardUtils {

    public static void copyTextToClipboard(Context context, String label, String text, boolean toast) {
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(label, text));
        if (toast) {
            ToastUtils.show(R.string.copy_to_clipboard_success);
        }
    }

    public static void copyTextToClipboard(Context context, String label, String text) {
        copyTextToClipboard(context, label, text, true);
    }

}
