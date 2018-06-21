package com.idogfooding.backbone.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.blankj.utilcode.util.ToastUtils;

/**
 * 剪切板工具
 *
 * @author Charles
 */
public class ClipboardUtils {

    /**
     * 复制到剪切板
     */
    public static void copyTextToClipboard(Context context, String label, String text) {
        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText(label, text);
        if (mClipboardManager == null) {
            ToastUtils.showLong("找不到剪切板");
        } else {
            mClipboardManager.setPrimaryClip(mClipData);
        }
    }

    /**
     * * todo
     * 获取剪切板内容
     *
     * @param context
     */
    public static void getPrimaryClip(Context context) {
        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (mClipboardManager == null) {
            ToastUtils.showLong("找不到剪切板");
        } else {
            ClipData clipData = mClipboardManager.getPrimaryClip();
        }

    }
}
