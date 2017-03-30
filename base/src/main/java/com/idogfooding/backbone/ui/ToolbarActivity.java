package com.idogfooding.backbone.ui;

import android.support.annotation.LayoutRes;
import android.view.MenuItem;
import android.view.View;

import com.idogfooding.backbone.R;

/**
 * ToolbarActivity
 *
 * @author Charles
 */
public abstract class ToolbarActivity extends BaseActivity {

    @Override
    protected void onConfigureActivity() {
        setRootLayoutId(R.layout._internal_activity_base);
    }

    @Override
    protected void attachContentView(View containerView, @LayoutRes int layoutResID) {
        super.attachContentView(containerView, layoutResID);
        //addUIComponents(new ToolbarComponent());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
