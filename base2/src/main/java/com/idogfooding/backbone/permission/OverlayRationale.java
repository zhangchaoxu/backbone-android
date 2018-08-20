package com.idogfooding.backbone.permission;

import android.app.AlertDialog;
import android.content.Context;

import com.idogfooding.backbone.R;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

/**
 * Created by YanZhenjie on 2018/5/30.
 */
public class OverlayRationale implements Rationale<Void> {
    @Override
    public void showRationale(Context context, Void data, final RequestExecutor executor) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.tips)
                .setMessage(R.string.message_overlay_failed)
                .setPositiveButton(R.string.settings, (dialog, which) -> executor.execute())
                .setNegativeButton(R.string.cancel, (dialog, which) -> executor.cancel())
                .show();
    }
}