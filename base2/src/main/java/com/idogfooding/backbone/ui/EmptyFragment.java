package com.idogfooding.backbone.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * EmptyFragment
 * for test only
 *
 * @author Charles Zhang
 */
public class EmptyFragment extends BaseFragment {

    String msg = "msg";
    TextView tvMsg;

    public static EmptyFragment newInstance() {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putString("msg", "empty");
        fragment.setArguments(args);
        return fragment;
    }

    public static EmptyFragment newInstance(String msg) {
        EmptyFragment fragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putString("msg", msg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, @Nullable ViewGroup container) {
        tvMsg = new TextView(getContext());
        return tvMsg;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvMsg.setText(msg);
        tvMsg.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        Log.d("Empty", msg);
    }

    @Override
    protected void onSetup(View view, Bundle savedInstanceState) {
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    @Override
    public void onFirstVisible() {
        Log.d("Empty", msg + "onFirstUserVisible");
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    @Override
    public void onVisible() {
        Log.d("Empty", msg + "onUserVisible");
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    @Override
    public void onFirstInvisible() {
        Log.d("Empty", msg + "onFirstUserInvisible");
    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    @Override
    public void onInvisible() {
        Log.d("Empty", msg + "onUserInvisible");
    }

}
