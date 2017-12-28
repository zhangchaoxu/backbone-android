package com.idogfooding.backbone.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

/**
 * EmptyFragment
 * for test only
 *
 * @author Charles Zhang
 */
public class EmptyFragment extends BaseFragment {

    String msg;
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
        Logger.d(msg);
    }

    @Override
    protected void onSetupFragment(View view, Bundle savedInstanceState) {
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    @Override
    public void onFirstVisible() {
        Logger.d(msg + "onFirstUserVisible");
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    @Override
    public void onVisible() {
        Logger.d(msg + "onUserVisible");
    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    @Override
    public void onFirstInvisible() {
        Logger.d(msg + "onFirstUserInvisible");

    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    @Override
    public void onInvisible() {
        Logger.d(msg + "onUserInvisible");
    }

}
