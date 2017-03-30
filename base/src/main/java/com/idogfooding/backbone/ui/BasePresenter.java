package com.idogfooding.backbone.ui;

/**
 * BasePresenter
 */
public interface BasePresenter<T> {
    void setView(T view);

    void onDestroy();
}
