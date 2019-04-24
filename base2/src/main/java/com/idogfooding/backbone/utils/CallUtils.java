package com.idogfooding.backbone.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.idogfooding.backbone.R;
import com.kongzue.dialog.v3.MessageDialog;

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
        new android.support.v7.app.AlertDialog.Builder(context, android.app.AlertDialog.THEME_HOLO_LIGHT)
                .setMessage("确认拨打电话:" + phone + "?")
                .setPositiveButton(R.string.confirm, (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    /**
     * 拨打电话
     */
    public static void call(AppCompatActivity activity, String phone) {
        MessageDialog.build(activity)
                .setMessage("确认拨打电话:" + phone + "?")
                .setOkButton(R.string.confirm, (baseDialog, v) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                    return false;
                })
                .setCancelButton(R.string.cancel)
                .show();
    }

}
