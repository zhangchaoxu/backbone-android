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
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chenenyu.router.RouteRequest;
import com.chenenyu.router.Router;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.RequestCode;
import com.idogfooding.backbone.permission.RuntimeRationale;
import com.idogfooding.backbone.utils.DoubleClickExit;
import com.idogfooding.backbone.widget.TopBar;
import com.idogfooding.backbone.widget.ViewPager;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.interfaces.OnDismissListener;
import com.kongzue.dialog.interfaces.OnNotificationClickListener;
import com.kongzue.dialog.v3.MessageDialog;
import com.kongzue.dialog.v3.Notification;
import com.kongzue.dialog.v3.TipDialog;
import com.kongzue.dialog.v3.WaitDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

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
        Router.injectParams(this);
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
        super.onDestroy();
        // 修复内存泄露问题
        // KeyboardUtils.fixSoftInputLeaks(this);
        // 注销keyboard监听器
        // KeyboardUtils.unregisterSoftInputChangedListener(this);
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
    public void dismissLoading() {
        if (isFinishing())
            return;

        WaitDialog.dismiss();
    }

    public void showLoading() {
        showLoading(getString(R.string.msg_loading));
    }

    public void showLoading(int contentResId) {
        showLoading(getString(contentResId));
    }

    public void showLoading(String content) {
        if (isFinishing())
            return;

        WaitDialog.show(this, content);
    }
    // [-] Loading Dialog

    // [+] tip dialog
    public void showMessageDialog(String title, String msg, OnDialogButtonClickListener onClickListener) {
        if (isFinishing())
            return;

        MessageDialog.build(this)
                .setTitle(title)
                .setMessage(msg)
                .setOkButton(R.string.confirm, onClickListener)
                .show();
    }

    public void showMessageDialog(String msg, OnDialogButtonClickListener onClickListener) {
        this.showMessageDialog(null, msg, onClickListener);
    }

    public void showMessageDialog(String msg) {
        showMessageDialog(msg, null);
    }

    public void showConfirmDialog(String msg, OnDialogButtonClickListener onConfirmClickListener) {
        showConfirmDialog(msg, getString(R.string.confirm), onConfirmClickListener, getString(R.string.cancel), null);
    }

    public void showConfirmDialog(String msg, String confirmText, OnDialogButtonClickListener onConfirmClickListener, String cancelText, OnDialogButtonClickListener onCancelClickListener) {
        this.showConfirmDialog( null, msg, confirmText, onConfirmClickListener, cancelText, onCancelClickListener);
    }

    public void showConfirmDialog(String title, String msg, String confirmText, OnDialogButtonClickListener onConfirmClickListener, String cancelText, OnDialogButtonClickListener onCancelClickListener) {
        if (isFinishing())
            return;

        MessageDialog.build(this)
                .setTitle(title)
                .setMessage(msg)
                .setOkButton(confirmText, onConfirmClickListener)
                .setCancelButton(cancelText, onCancelClickListener)
                .show();
    }

    public TipDialog showTipDialog(String msg) {
        return showTipDialog(msg, TipDialog.TYPE.WARNING);
    }

    public TipDialog showTipDialog(String msg, TipDialog.TYPE toastType) {
        return showTipDialog(msg, toastType, null);
    }

    public TipDialog showTipDialog(String msg, TipDialog.TYPE toastType, OnDismissListener onDismissListener) {
        if (isFinishing())
            return null;

        return TipDialog.show(this, msg, toastType)
                .setOnDismissListener(onDismissListener)
                .setCancelable(true)
                .setTipTime(1500);

    }

    public void showNotifyDialog(String msg) {
        showNotifyDialog(msg);
    }

    public Notification showNotifyDialog(String msg, OnNotificationClickListener onNotificationClickListener) {
        if (isFinishing())
            return null;

        return Notification.show(this, msg).setOnNotificationClickListener(onNotificationClickListener);
    }
    // [-] tip dialog

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
                    .runtime()
                    .permission(permissions)
                    .rationale(new RuntimeRationale())
                    .onGranted(permissions12 -> afterPermissionGranted())
                    .onDenied(permissions13 -> showSettingDialog(BaseActivity.this, permissions13))
                    .start();
        } else {
            afterPermissionGranted();
        }
    }

    /**
     * Display setting dialog.
     */
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));

        showConfirmDialog(message, getString(R.string.settings), (baseDialog, v) -> {
            AndPermission.with(this).runtime().setting().start(RequestCode.PERMISSION_SETTINGS);
            finish();
            return false;
        }, getString(R.string.cancel), (baseDialog, v) -> {
            finish();
            return false;
        });
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
     * @param placeholderResId place holder资源id
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
        // fix bug 5497
        KeyboardUtils.fixAndroidBug5497(this);
        // 点击空白区域,隐藏软键盘
        KeyboardUtils.clickBlankArea2HideSoftInput();
        // 监听键盘变化
        KeyboardUtils.registerSoftInputChangedListener(this, height -> onSoftInputChanged(KeyboardUtils.isSoftInputVisible(this), height));
    }

    /**
     * **谨慎使用**
     * 会引发内存溢出
     *
     * 当软键盘显示状态发生变化
     * 使用之前先调用registerKeyboardChanged
     *
     * @param visible
     */
    protected void onSoftInputChanged(boolean visible, int height) {

    }

    /**
     * 清除EditText上的焦点
     * 再表单填写的时候,如果有图片选择等其它内容，跳转回来以后页面会滚动到聚焦的editext上
     * 因此需要清除焦点
     */
    protected void clearEditTextFocus() {
        View view = getCurrentFocus();
        if (view instanceof EditText)
            view.clearFocus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.USER_LOGIN && resultCode == Activity.RESULT_OK) {
            handleUserLoginSuccess(data);
        } if (requestCode == RequestCode.PERMISSION_SETTINGS) {
            askForPermissions();
            // Toast.makeText(this, R.string.message_setting_comeback, Toast.LENGTH_SHORT).show();
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

        // 是否跳转，默认跳转
        if (null != routeRequest.getExtras() && !routeRequest.getExtras().getBoolean("redirect", true)) {
            return;
        }

        Router.build(routeRequest).go(this);
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

    /**
     * 双击返回退出App
     * 检查fragment中的backpressed
     * 在Home的onBackPressed中调用
     */
    protected void doubleClickExit() {
        if (!FragmentUtils.dispatchBackPress(getSupportFragmentManager())) {
            if (DoubleClickExit.check()) {
                closeAllActivity();
            } else {
                ToastUtils.showShort(R.string.msg_double_click_exit);
            }
        }
    }

    // finish activity with result
    public void finish(int resultCode) {
        this.finish(resultCode, null);
    }

    public void finish(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

}
