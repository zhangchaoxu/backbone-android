package com.idogfooding.backbone.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chenenyu.router.IRouter;
import com.chenenyu.router.RouteRequest;
import com.chenenyu.router.Router;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.RequestCode;
import com.idogfooding.backbone.widget.TopBar;
import com.idogfooding.backbone.widget.ViewPager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.SettingService;

import java.util.List;

import butterknife.ButterKnife;
import me.bakumon.statuslayoutmanager.library.DefaultOnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * BaseActivity
 *
 * @author Charles
 */
public abstract class BaseActivity extends AppCompatActivity {

    // toolbar stub
    protected boolean showToolbar = true; // 是否显示toolbar
    protected TopBar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onConfig(savedInstanceState);
        super.onCreate(savedInstanceState);

        // set view
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        onSetup(savedInstanceState);

        // 动态注册广播,用于关闭所有activity
        mExistAppBroadcastReceiver = new ExistAppBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("com.idogfooding.backbone.ui.BaseActivity");
        registerReceiver(mExistAppBroadcastReceiver, intentFilter);
    }

    /**
     * onConfigureActivity
     */
    protected void onConfig(Bundle savedInstanceState) {
        try {
            Router.injectParams(this);
        } catch (Exception e) {
            // no inject router params
        }
    }

    /**
     * 设置layout
     */
    protected int getLayoutId() {
        return R.layout.common_pager;
    }

    /**
     * 启动activity
     */
    protected void onSetup(Bundle savedInstanceState) {
        // init toolbar
        if (showToolbar) {
            initToolbar();
        }
    }

    //##########  toolbar ##########
    protected void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar == null)
            return;

        // 左侧按钮默认关闭
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setOnLeftTextClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        if (!FragmentUtils.dispatchBackPress(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (null == toolbar)
            return;

        toolbar.setTitleMainText(title);
    }

    protected void setSubTitle(@StringRes int titleId) {
        setSubTitle(getText(titleId));
    }

    public void setSubTitle(CharSequence title) {
        if (null == toolbar)
            return;

        toolbar.setTitleSubText(title);
    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        super.onDestroy();
        // 注销广播
        if (mExistAppBroadcastReceiver != null) {
            unregisterReceiver(mExistAppBroadcastReceiver);
        }
    }

    //##########  fragment ##########

    /**
     * replace fragment
     *
     * @param pagerId
     * @param fragment
     */
    protected void replaceFragment(@IdRes int pagerId, Fragment fragment) {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragment;
            }

            @Override
            public int getCount() {
                return 1;
            }
        };
        ((ViewPager) findViewById(pagerId)).setAdapter(adapter);
        // replace content with fragment
        /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragmentId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
        fragment.setUserVisibleHint(true);*/
    }

    protected void replaceFragment(Fragment fragment) {
        replaceFragment(R.id.pager, fragment);
    }

    // [+] Loading Dialog
    /**
     * Shows the progress UI and hides the login_bg form.
     */
    private MaterialDialog mLoadingDialog;

    public void dismissLoading() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing() && !isFinishing())
            mLoadingDialog.dismiss();
    }

    public void showLoadingDialog() {
        showLoadingDialog(R.string.msg_loading);
    }

    public void showLoadingDialog(int contentResId) {
        showLoadingDialog(getString(contentResId));
    }

    public void showLoadingDialog(String content) {
        if (null == mLoadingDialog) {
            mLoadingDialog = new MaterialDialog.Builder(this)
                    .content(content)
                    .progress(true, 0)
                    .cancelable(false)
                    .show();
        } else {
            mLoadingDialog.setContent(content);
            mLoadingDialog.show();
        }
    }
    // [-] Loading Dialog

    @ColorInt
    protected int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    ExistAppBroadcastReceiver mExistAppBroadcastReceiver;

    /**
     * ExistAppBroadcastReceiver
     */
    public class ExistAppBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean closeAll = intent.getBooleanExtra("closeAll", false);
            if (closeAll) {
                finish();
            }
        }
    }

    protected void closeAllActivity() {
        Intent intent = new Intent("com.idogfooding.backbone.ui.BaseActivity");
        intent.putExtra("closeAll", true);
        sendBroadcast(intent);
    }

    //##########  permissions 权限申请 ##########

    /**
     * ask for permission
     * 申请获取权限
     *
     * @param permissions
     */
    protected void askForPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(this)
                    .permission(permissions)
                    .rationale((context, permissions1, executor) -> {
                        List<String> permissionNames = Permission.transformText(context, permissions1);
                        String message = context.getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames));
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setTitle(R.string.tips)
                                .setMessage(message)
                                .setPositiveButton(R.string.resume, (dialog, which) -> executor.execute())
                                .setNegativeButton(R.string.cancel, (dialog, which) -> executor.cancel())
                                .show();
                    })
                    .onGranted(permissions12 -> afterPermissionGranted())
                    .onDenied(permissions13 -> {
                        //if (AndPermission.hasAlwaysDeniedPermission(this, permissions13)) {
                        List<String> permissionNames = Permission.transformText(this, permissions13);
                        String message = getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

                        SettingService settingService = AndPermission.permissionSetting(this);
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setTitle(R.string.tips)
                                .setMessage(message)
                                .setPositiveButton(R.string.settings, (dialog, which) -> settingService.execute())
                                .setNegativeButton(R.string.no, (dialog, which) -> settingService.cancel())
                                .show();
                        //}
                    })
                    .start();
        } else {
            afterPermissionGranted();
        }

    }

    /**
     * after permission granted
     * 成功获取到权限
     */
    protected void afterPermissionGranted() {

    }

    //##########  图片加载 Image Loader ##########

    /**
     * 用Glide加载图片
     *
     * @param imageView        图片view
     * @param model            加载对象
     * @param placeholderResId placeholader资源id
     * @param errorResId       error 资源id
     */
    protected void loadImage(ImageView imageView, Object model, int placeholderResId, int errorResId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);

        this.loadImage(imageView, model, requestOptions);
    }

    protected void loadImage(ImageView imageView, Object model, RequestOptions requestOptions) {
        Glide.with(this).load(model)
                .apply(requestOptions)
                .into(imageView);
    }

    protected void loadImage(ImageView imageView, Object model) {
        this.loadImage(imageView, model, R.mipmap.ic_placeholder, R.mipmap.ic_error);
    }

    // 键盘监听

    /**
     * 注册监听软键盘显示状态发生变化
     */
    protected void registerSoftInputChanged() {
        KeyboardUtils.registerSoftInputChangedListener(this, height -> onSoftInputChanged(KeyboardUtils.isSoftInputVisible(this)));
        // 点击空白区域,隐藏软键盘
        KeyboardUtils.clickBlankArea2HideSoftInput();
        // 修复内存泄露问题
        KeyboardUtils.fixSoftInputLeaks(this);
    }

    /**
     * 当软键盘显示状态发生变化
     * 使用之前先调用registerKeyboardChanged
     *
     * @param visible
     */
    protected void onSoftInputChanged(boolean visible) {

    }

    /**
     * 清除EditText上的焦点
     * 再表单填写的时候,如果有图片选择等其它内容，跳转回来以后页面会滚动到聚焦的editext上
     * 因此需要清除焦点
     */
    protected void clearEditTextFocus() {
        View view = getCurrentFocus();
        if (null != view && view instanceof EditText)
            view.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.USER_LOGIN && resultCode == Activity.RESULT_OK) {
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
     *
     * @param routeRequest
     */
    protected void handleRouteRequest(RouteRequest routeRequest) {
        if (routeRequest == null)
            return;

        IRouter iRouter = Router.build(routeRequest.getUri());
        if (null != routeRequest.getExtras()) {
            iRouter.with(routeRequest.getExtras());
            // 是否跳转，默认跳转
            boolean redirect = routeRequest.getExtras().getBoolean("redirect", true);
            if (!redirect)
                return;
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
     *
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
