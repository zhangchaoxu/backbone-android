package com.idogfooding.backbone.network;

import android.content.Context;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<HttpResult<T>> {

    private Context mContext;
    private MaterialDialog mDialog;
    private Disposable mDisposable;
    private final int SUCCESS_CODE = 0;

    public BaseObserver(Context context, MaterialDialog dialog) {
        mContext = context;
        mDialog = dialog;

        mDialog.setOnCancelListener(dialog1 -> mDisposable.dispose());
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(HttpResult<T> value) {
        if (value.getCode() == SUCCESS_CODE) {
            T t = value.getData();
            onHandleSuccess(t);
        } else {
            onHandleError(value.getCode(), value.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        Logger.d("gesanri", "error:" + e.toString());

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }

        Toast.makeText(mContext, "网络异常，请稍后再试", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onComplete() {
        Logger.d("gesanri", "onComplete");

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    abstract void onHandleSuccess(T t);

    void onHandleError(int code, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }
}