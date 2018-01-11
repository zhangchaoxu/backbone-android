package com.idogfooding.backbone.network.callback;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.lzy.okgo.request.base.Request;

/**
 * 对于网络请求是否需要弹出进度对话框
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private ProgressDialog dialog;

    private void initDialog(Context context) {
        dialog = new ProgressDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
    }

    public DialogCallback() {
        super();
    }

    public DialogCallback(Context context, boolean showDialog) {
        super();
        if (showDialog) {
            initDialog(context);
        }
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}