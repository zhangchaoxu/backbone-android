package com.idogfooding.backbone.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.ui.tab.TabLayoutPagerActivity;
import com.idogfooding.backbone.widget.TopBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseFragment
 * lazy load {http://www.cnblogs.com/leevey/p/5678037.html}
 *
 * @author Charles
 */
public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;

    // fragment状态
    private boolean isPrepared;
    private boolean isFirstResume = true;
    private boolean isFirstInvisible = true;
    private boolean isFirstVisible = true;

    // toolbar stub
    protected boolean showToolbar = false; // 是否显示toolbar
    // 放弃viewstub填充,是因为viewstub加载自定义控件无法wrap_content
    // wrap_content会变成和父控件一致
    protected TopBar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        onConfigureFragment();
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        restoreFragmentState(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

        onSetupFragment(view, savedInstanceState);
    }

    /**
     * onSetupFragment
     *
     * @param view
     * @param savedInstanceState
     */
    protected void onSetupFragment(View view, Bundle savedInstanceState) {
        // init toolbar
        if (showToolbar) {
            toolbar = view.findViewById(R.id.toolbar);
            initToolbar();
        }
    }

    //##########  toolbar ##########
    protected void initToolbar() {
        if (toolbar == null)
            return;

        // 左侧按钮默认关闭
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setOnLeftTextClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
    }

    protected void setTitle(@StringRes int titleId) {
        setTitle(getText(titleId));
    }

    protected void setTitle(CharSequence title) {
        if (null == toolbar)
            return;

        toolbar.setTitleMainText(title);
    }

    protected void setSubTitle(@StringRes int titleId) {
        setSubTitle(getText(titleId));
    }

    protected void setSubTitle(CharSequence title) {
        if (null == toolbar)
            return;

        toolbar.setTitleSubText(title);
    }

    protected void onConfigureFragment() {
        // cfg fragment, like set showToolbar
    }

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

    /**
     * 跳转到TabLayoutPagerActivity中的某个tab
     *
     * @param index
     */
    protected void switchToTab(int index) {
        if (getActivity() == null || !(getActivity() instanceof TabLayoutPagerActivity))
            return;

        ((TabLayoutPagerActivity) getActivity()).setCurrentTab(index);
    }

    // glide load
    protected void loadImage(ImageView imageView, Object model, RequestOptions requestOptions) {
        Glide.with(this).load(model)
                .apply(requestOptions)
                .into(imageView);
    }

    protected void loadImage(ImageView imageView, Object model, int placeholderResId, int errorResId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);

        this.loadImage(imageView, model, requestOptions);
    }

    protected void loadImage(ImageView imageView, Object model) {
        this.loadImage(imageView, model, R.mipmap.ic_placeholder, R.mipmap.ic_error);
    }

}
