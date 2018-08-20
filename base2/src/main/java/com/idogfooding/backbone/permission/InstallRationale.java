package com.idogfooding.backbone.permission;

import android.app.AlertDialog;
import android.content.Context;

import com.idogfooding.backbone.R;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.io.File;

/**
 * Created by YanZhenjie on 2018/4/29.
 */
public class InstallRationale implements Rationale<File> {

    @Override
    public void showRationale(Context context, File data, final RequestExecutor executor) {
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.tips)
                .setMessage(R.string.message_install_failed)
                .setPositiveButton(R.string.settings, (dialog, which) -> executor.execute())
                .setNegativeButton(R.string.cancel, (dialog, which) -> executor.cancel())
                .show();
    }
}