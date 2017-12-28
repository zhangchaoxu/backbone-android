package com.idogfooding.backbone.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idogfooding.backbone.widget.CommonTitleBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseFragment
 * lazy load {http://www.cnblogs.com/leevey/p/5678037.html}
 *
 * @author Charles
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    public String getSimpleName() {
        return TAG;
    }

    private Unbinder unbinder;

    private boolean isPrepared;
    private boolean isFirstResume = true;
    private boolean isFirstInvisible = true;
    private boolean isFirstVisible = true;

    CommonTitleBar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        restoreFragmentState(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutView(inflater, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    protected View getLayoutView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initFakeToolbar(view);
        onSetupFragment(view, savedInstanceState);
    }

    protected abstract void onSetupFragment(View view, Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstInvisible();
            } else {
                onInvisible();
            }
        }
    }

    /*protected void initFakeToolbar(View view) {
        fakeToolbar = view.findViewById(R.id.fake_toolbar);
    }*/

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getUserVisibleHint()) {
            onInvisible();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // BINDING RESET
        // Fragments have a different view lifecycle than activities.
        // When binding a fragment in onCreateView, set the views to null in onDestroyView.
        // Butter Knife returns an Unbinder instance when you call bind to do this for you.
        // Call its unbind method in the appropriate lifecycle callback.
        unbinder.unbind();
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * on fragment first visible
     * do init
     */
    public void onFirstVisible() {

    }

    /**
     * fragment visible
     * onResume or scroll to this fragment
     */
    public void onVisible() {

    }

    /**
     * on first invisible
     * on first fragment invisible
     * do nothing is this method as setUserVisibleHint called before onCreate, bound and view will be null*
     */
    public void onFirstInvisible() {

    }

    /**
     * on fragment invisible
     * scroll out or onPause
     */
    public void onInvisible() {

    }

    private void restoreFragmentState(Bundle savedInstanceState) {

    }

    //##########  Protected helper methods ##########
    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    // finish activity with result
    protected void finishActivity() {
        this.finishActivity(Activity.RESULT_CANCELED, null);
    }

    protected void finishActivity(int resultCode) {
        this.finishActivity(resultCode, null);
    }

    protected void finishActivity(int resultCode, Intent data) {
        if (null != getActivity()) {
            getActivity().setResult(resultCode, data);
            getActivity().finish();
        }
    }

    @ColorInt
    protected int color(@ColorRes int res) {
        return ContextCompat.getColor(getContext(), res);
    }
}
