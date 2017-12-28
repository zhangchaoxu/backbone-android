package com.idogfooding.backbone.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chenenyu.router.Router;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.RequestCode;
import com.idogfooding.backbone.widget.CommonTitleBar;
import com.idogfooding.backbone.widget.ViewPager;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import butterknife.ButterKnife;

/**
 * BaseActivity
 *
 * @author Charles
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    public String getSimpleName() {
        return TAG;
    }

    private boolean mConfigured;
    CommonTitleBar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onConfigureActivity();
        mConfigured = true;
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        Router.injectParams(this);
        ButterKnife.bind(this);
        onSetupActivity(savedInstanceState);

        // register receiver
        mExistAppBroadcastReceiver = new ExistAppBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter("com.idogfooding.backbone.ui.BaseActivity");
        registerReceiver(mExistAppBroadcastReceiver, intentFilter);
    }

    /**
     * 启动activity
     *
     * @param savedInstanceState
     */
    protected void onSetupActivity(Bundle savedInstanceState) {
        initToolbar();
    }

    /**
     * init toolbar
     */
    protected void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (null == toolbar)
            return;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (null == toolbar)
            return;

        toolbar.setCenterText(title.toString());
    }

    public void setSubTitle(CharSequence title) {
        if (null == toolbar)
            return;

        toolbar.setCenterSubText(title.toString());
    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        super.onDestroy();
        // unregister receiver
        if (mExistAppBroadcastReceiver != null) {
            unregisterReceiver(mExistAppBroadcastReceiver);
        }
    }

    protected void onConfigureActivity() {
    }

    protected int getLayoutId() {
        return R.layout.common_pager;
    }

    //##########  Protected helper methods ##########
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

    // permission
    protected void askForPermissions(String... permissions) {
        askForPermissions(RequestCode.PERMISSION_MULTI, permissions);
    }

    protected void askForPermissions(int code, String... permissions) {
        if (null == permissionListener) {
            initPermissionListener();
        }
        AndPermission.with(this)
                .requestCode(code)
                .permission(permissions)
                .callback(permissionListener)
                .rationale((requestCode, rationale) -> AndPermission.rationaleDialog(BaseActivity.this, rationale).show())
                .start();
    }

    private PermissionListener permissionListener;

    private void initPermissionListener() {
        permissionListener = new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                if (AndPermission.hasPermission(BaseActivity.this, grantPermissions)) {
                    afterPermissionGranted(requestCode);
                } else {
                    AndPermission.defaultSettingDialog(BaseActivity.this, requestCode).show();
                }
            }

            @Override
            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                if (AndPermission.hasPermission(BaseActivity.this, deniedPermissions)) {
                    afterPermissionGranted(requestCode);
                } else if (AndPermission.hasAlwaysDeniedPermission(BaseActivity.this, deniedPermissions)) {
                    AndPermission.defaultSettingDialog(BaseActivity.this, RequestCode.PERMISSION_SETTINGS).show();
                }
            }
        };
    }

    protected void afterPermissionGranted(int requestCode) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.PERMISSION_SETTINGS) {
            askForPermissions();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
