package com.idogfooding.backbone.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.permission.PermissionRequest;
import com.idogfooding.backbone.ui.component.UIComponent;
import com.idogfooding.backbone.utils.SettingsUtils;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * BaseActivity
 *
 * @author Charles
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    private ArrayMap<String, UIComponent> mUIComponents;

    private boolean mConfigured;
    private int mRootLayoutId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        onConfigureActivity();
        mConfigured = true;
        super.onCreate(savedInstanceState);
        setActivityView(getLayoutId());
        ButterKnife.bind(this);
        Logger.d(TAG + ".onCreate->afterContentView");
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

    protected final ArrayMap<String, UIComponent> getUIComponents() {
        return mUIComponents;
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

    public final View getContainerView() {
        return ((ViewGroup) $(android.R.id.content)).getChildAt(0);
    }

    //##########  construct methods  ##########
    protected void onConfigureActivity() {
        //Stub
    }

    protected abstract int getLayoutId();

    //##########  Protected helper methods ##########
    @SuppressWarnings("unchecked")
    protected final <T extends View> T $(int viewId) {
        return (T) findViewById(viewId);
    }

    @SuppressWarnings("unchecked")
    protected final <T extends View> T $(View view, int viewId) {
        return (T) view.findViewById(viewId);
    }

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

}
