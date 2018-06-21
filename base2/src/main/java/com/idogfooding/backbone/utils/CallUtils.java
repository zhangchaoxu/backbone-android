package com.idogfooding.backbone.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.afollestad.materialdialogs.MaterialDialog;
import com.idogfooding.backbone.R;

public class CallUtils {

    public static void call(Context context, String phone) {
        new MaterialDialog.Builder(context)
                .content("确认拨打电话:" + phone + "?")
                .positiveText(R.string.confirm)
                .onPositive((dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                })
                .negativeText(R.string.cancel)
                .show();
    }

}
