package com.idogfooding.backbone.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.idogfooding.backbone.R;

/**
 * 拨打电话工具
 *
 * @author Charles
 */
public class CallUtils {

    /**
     * 拨打电话
     */
    public static void call(Context context, String phone) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.tips)
                .setMessage("确认拨打电话:" + phone + "?")
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                })
                .show();
    }

}
