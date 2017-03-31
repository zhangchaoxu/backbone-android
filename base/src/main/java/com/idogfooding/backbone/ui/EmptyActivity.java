package com.idogfooding.backbone.ui;

import com.chenenyu.router.annotation.Route;

/**
 * EmptyActivity
 *
 * @author Charles
 */
@Route({"empty"})
public class EmptyActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }
}
