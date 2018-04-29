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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chenenyu.router.IRouter;
import com.chenenyu.router.RouteRequest;
import com.chenenyu.router.Router;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.RequestCode;
import com.idogfooding.backbone.ui.tab.TabPagerActivity;
import com.idogfooding.backbone.widget.TopBar;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.bakumon.statuslayoutmanager.library.DefaultOnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

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
        onConfig(savedInstanceState);
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

        onSetup(view, savedInstanceState);
    }

    /**
     * onSetup
     *
     * @param view
     * @param savedInstanceState
     */
    protected void onSetup(View view, Bundle savedInstanceState) {
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

    /**
     * 配置fragment
     */
    protected void onConfig(Bundle savedInstanceState) {
        try {
            Router.injectParams(this);
        } catch (Exception e) {
            // no inject router params
        }
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
        if (getActivity() == null || !(getActivity() instanceof TabPagerActivity))
            return;

        ((TabPagerActivity) getActivity()).setCurrentTab(index);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.USER_LOGIN && resultCode == Activity.RESULT_OK && null != data) {
            handleUserLoginSuccess(data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 监听登录结果,登录成功则跳转到被拦截的页面
     *
     * @param data
     */
    protected void handleUserLoginSuccess(Intent data) {
        if (data == null)
            return;

        handleRouteRequest(data.getParcelableExtra("routeRequest"));
    }

    /**
     * 处理RouteRequest
     * @param routeRequest
     */
    protected void handleRouteRequest(RouteRequest routeRequest) {
        if (routeRequest == null)
            return;

        IRouter iRouter = Router.build(routeRequest.getUri());
        if (null != routeRequest.getExtras()) {
            iRouter.with(routeRequest.getExtras());
        }
        if (0 != routeRequest.getRequestCode()) {
            iRouter.requestCode(routeRequest.getRequestCode());
        }
        if (null != routeRequest.getAddedInterceptors() && !routeRequest.getAddedInterceptors().isEmpty()) {
            iRouter.addInterceptors(routeRequest.getAddedInterceptors().toArray(new String[routeRequest.getAddedInterceptors().size()]));
        }
        if (null != routeRequest.getRemovedInterceptors() && !routeRequest.getRemovedInterceptors().isEmpty()) {
            iRouter.skipInterceptors(routeRequest.getRemovedInterceptors().toArray(new String[routeRequest.getRemovedInterceptors().size()]));
        }
        if (0 != routeRequest.getFlags()) {
            iRouter.addFlags(routeRequest.getFlags());
        }
        if (null != routeRequest.getRouteCallback()) {
            iRouter.callback(routeRequest.getRouteCallback());
        }
        if (!TextUtils.isEmpty(routeRequest.getAction())) {
            iRouter.setAction(routeRequest.getAction());
        }
        if (!TextUtils.isEmpty(routeRequest.getType())) {
            iRouter.setType(routeRequest.getType());
        }
        if (0 != routeRequest.getExitAnim() && 0 != routeRequest.getEnterAnim()) {
            iRouter.anim(routeRequest.getEnterAnim(), routeRequest.getExitAnim());
        }
        iRouter.go(this);
    }

    //##########  状态布局  ##########
    protected StatusLayoutManager statusLayoutManager;

    /**
     * 初始化状态布局
     * @param container
     */
    protected void initStatusLayout(View container) {
        statusLayoutManager = new StatusLayoutManager.Builder(container)
                .setLoadingLayout(R.layout.view_loading)
                .setOnStatusChildClickListener(new DefaultOnStatusChildClickListener() {

                    @Override
                    public void onErrorChildClick(View view) {
                        loadData();
                    }

                    @Override
                    public void onEmptyChildClick(View view) {
                        loadData();
                    }

                    @Override
                    public void onCustomerChildClick(View view) {
                        loadData();
                    }

                }).build();
    }

    /**
     * 加载数据
     */
    protected void loadData() {

    }

}
