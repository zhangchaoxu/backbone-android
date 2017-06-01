package com.idogfooding.backbone.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.permission.PermissionRequest;
import com.idogfooding.backbone.statusbar.StatusBarFontHelper;
import com.idogfooding.backbone.ui.component.UIComponent;
import com.idogfooding.backbone.utils.SettingsUtils;
import com.idogfooding.backbone.widget.FakeToolbar;
import com.idogfooding.backbone.widget.ViewPager;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.subjects.BehaviorSubject;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * BaseActivity
 *
 * @author Charles
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    public String getSimpleName() {
        return TAG;
    }

    private boolean mRegisterEventBus;
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    private ArrayMap<String, UIComponent> mUIComponents;

    private boolean mConfigured;
    private int mRootLayoutId;

    protected FakeToolbar fakeToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onConfigureActivity();
        mConfigured = true;
        super.onCreate(savedInstanceState);

        setActivityView(getLayoutId());

        ButterKnife.bind(this);
        onIntentReceived(getIntent());

        initFakeToolbar();
        onSetupActivity(savedInstanceState);
    }

    protected void initFakeToolbar() {
        fakeToolbar = ButterKnife.findById(this, R.id.fake_toolbar);
        if (null != fakeToolbar) {
            fakeToolbar.setOnLeftClickListener(v -> onToolbarLeftClick());
            fakeToolbar.setOnRightClickListener(v -> onToolbarRightClick());
        }
    }

    protected void onSetupActivity(Bundle savedInstanceState) {

    }


    @Override
    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (fakeToolbar != null) {
            fakeToolbar.setTitle(title);
        }
    }

    protected void onToolbarLeftClick() {
        onBackPressed();
    }

    protected void onToolbarRightClick() {}

    /**
     * called when onCreate and fragment has Arguments
     */
    protected void onIntentReceived(Intent intent) {
        if (null == intent)
            return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register eventBus
        if (mRegisterEventBus) {
            EventBus eventBus = EventBus.getDefault();
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mRegisterEventBus) {
            EventBus eventBus = EventBus.getDefault();
            if (eventBus.isRegistered(this)) {
                eventBus.unregister(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        super.onDestroy();
    }

    /**
     * register EventBus on resume/pause by default, must be called before onResume/onPause
     */
    protected void registerEventBus() {
        mRegisterEventBus = true;
    }

    /**
     * hidden status bar
     */
    protected void fullScreen() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void darkStatusBarIcon(boolean bDark) {
        StatusBarFontHelper.setStatusBarMode(this, bDark);
    }

    /**
     * add UIComponents, it will use {@link UIComponent#getTag()} as name, same component will be replaced
     * should be called {@link #attachContentView(View, int)}
     *
     * @return current uiComponents
     */
    protected final ArrayMap<String, UIComponent> addUIComponents(UIComponent... uiComponents) {
        if (mUIComponents == null) {
            mUIComponents = new ArrayMap<>();
        }
        for (int i = 0, s = uiComponents.length; i < s; i++) {
            mUIComponents.put(uiComponents[i].getTag(), uiComponents[i]);
        }
        return mUIComponents;
    }

    protected final ArrayMap<String, UIComponent> getUIComponents() {
        return mUIComponents;
    }

    @SuppressWarnings("unchecked")
    protected final <T> T getUIComponent(String tag) {
        if (mUIComponents != null) {
            return (T) mUIComponents.get(tag);
        }
        return null;
    }

    public final View getContainerView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    protected void onConfigureActivity() {
        translucentStatusBar();
    }

    @BeforeConfigActivity
    protected void setRootLayoutId(@LayoutRes int rootLayoutId) {
        checkConfigured();
        mRootLayoutId = rootLayoutId;
    }

    protected void setActivityView(@LayoutRes int layoutResID) {
        // if root layout has been set, then it's a container, so let subclass
        // to handle content view.
        boolean hasNewRoot = mRootLayoutId > 0;
        setContentView(hasNewRoot ? mRootLayoutId : layoutResID);
        if (hasNewRoot) {
            attachContentView(getContainerView(), layoutResID);
            inflateComponents(getContainerView(), getUIComponents());
        }
    }

    /**
     * Attach views to layout. It's good time to add UIComponent here.
     *
     * @param containerView
     * @param layoutResID
     */
    @CallSuper
    protected void attachContentView(View containerView, @LayoutRes int layoutResID) {
        if (containerView == null) {
            throw new IllegalStateException("Cannot find container view");
        }
        if (layoutResID == 0) {
            throw new IllegalStateException("Invalid layout id");
        }
        View contentView = getLayoutInflater().inflate(layoutResID, null, false);
        ((ViewGroup) containerView).addView(contentView, 0);
    }

    protected void inflateComponents(View containerView, ArrayMap<String, UIComponent> uiComponents) {
        if (uiComponents == null) {
            return;
        }
        for (int i = 0; i < uiComponents.size(); i++) {
            uiComponents.get(uiComponents.keyAt(i)).inflate(containerView);
        }
    }

    protected final boolean isConfigured() {
        return mConfigured;
    }

    protected final void checkConfigured() {
        if (mConfigured) {
            throw new IllegalStateException("You must call this method in onConfigureActivity");
        }
    }

    protected int getLayoutId() {
        return R.layout.common_pager;
    }

    //##########  Protected helper methods ##########

    //##########  PERMISSION REQUEST ##########
    protected List<PermissionRequest> permissionRequests = new ArrayList<>();

    /**
     * call requestForPermission in start
     * <p>
     * Request Permission
     */
    protected void requestForPermission() {
        initPermissionRequests();

        for (PermissionRequest permissionRequest : permissionRequests) {
            new RxPermissions(this)
                    .requestEach(permissionRequest.getName())
                    .subscribe(permission -> {
                        permissionRequest.setRequested(true);
                        if (permission.granted) {
                            permissionRequest.setGranted(true);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            permissionRequest.setDenied(true);
                        } else {
                            permissionRequest.setNeverAskAgain(true);
                        }
                        checkPermissionRequestResult();
                    });
        }
    }

    protected void initPermissionRequests() {
        permissionRequests.clear();
        permissionRequests.add(new PermissionRequest(Manifest.permission.WRITE_EXTERNAL_STORAGE));
        permissionRequests.add(new PermissionRequest(Manifest.permission.READ_PHONE_STATE));
        /* permissionRequests.add(new PermissionRequest(Manifest.permission.ACCESS_COARSE_LOCATION));
        permissionRequests.add(new PermissionRequest(Manifest.permission.ACCESS_FINE_LOCATION));
        permissionRequests.add(new PermissionRequest(Manifest.permission.CAMERA));
        permissionRequests.add(new PermissionRequest(Manifest.permission.CALL_PHONE));*/
    }

    protected void afterGranted() {
        // do after granted all request permission
    }

    protected void checkPermissionRequestResult() {
        boolean requestAll = true;
        boolean grantedAll = true;
        boolean showDeniedForPermission = false;
        boolean showNeverAskForPermission = false;
        for (PermissionRequest permissionRequest : permissionRequests) {
            if (permissionRequest.isRequested()) {
                if (!permissionRequest.isGranted()) {
                    grantedAll = false;
                    if (permissionRequest.isDenied()) {
                        showDeniedForPermission = true;
                    } else if (permissionRequest.isNeverAskAgain()) {
                        showNeverAskForPermission = true;
                    }
                }
            } else {
                requestAll = false;
                break;
            }
        }

        if (requestAll) {
            if (grantedAll) {
                afterGranted();
            } else if (showNeverAskForPermission) {
                showNeverAskForPermission();
            } else if (showDeniedForPermission) {
                showDeniedForPermission();
            }
        }
    }

    /**
     * Denied permission without ask never again
     */
    protected void showDeniedForPermission() {
        new MaterialDialog.Builder(this)
                .title("请允许获取设备信息")
                .content("我们需要获取设备信息,为您进行设备识别;\n否则您将无法正常使用" + getString(R.string.app_name))
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.no)
                .onPositive((dialog, which) -> requestForPermission())
                .onNegative((dialog, which) -> finish())
                .show();
    }

    /**
     * Denied permission with ask never again,Need to go to the settings
     */
    protected void showNeverAskForPermission() {
        new MaterialDialog.Builder(this)
                .title("请允许获取设备信息")
                .content("我们需要获取设备信息,为您进行设备识别;\n否则您将无法正常使用" + getString(R.string.app_name)
                        + "\n\n设置路径: 系统设置->" + getString(R.string.app_name) + "->权限")
                .positiveText(R.string.settings)
                .negativeText(android.R.string.cancel)
                .onPositive((dialog, which) -> {
                    SettingsUtils.openApplicationSettings(this);
                    finish();
                })
                .onNegative((dialog, which) -> finish())
                .show();
    }

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

    /**
     * Annotated methods should run in {@link #onConfigureActivity()}
     */
    @Target(ElementType.METHOD)
    @Retention(SOURCE)
    public @interface BeforeConfigActivity {
    }

    @ColorInt
    protected int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

}
