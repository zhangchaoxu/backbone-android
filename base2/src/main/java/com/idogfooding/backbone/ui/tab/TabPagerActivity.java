package com.idogfooding.backbone.ui.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.ui.BaseActivity;
import com.idogfooding.backbone.widget.ViewPager;

/**
 * TabPagerActivity
 *
 * @author Charles
 */
public abstract class TabPagerActivity extends BaseActivity {

    protected CommonTabLayout tabLayout;
    protected ViewPager pager;
    TabFragmentPagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.tab_layout_pager;
    }

    @Override
    protected void onSetup(Bundle savedInstanceState) {
        super.onSetup(savedInstanceState);

        tabLayout = findViewById(R.id.tab_layout);
        pager = findViewById(R.id.pager);

        adapter = createAdapter();

        // init ViewPager
        pager.setScrollable(isPagerScrollable());
        pager.setOffscreenPageLimit(getPagerOffscreenPageLimit());
        pager.setAdapter(adapter);

        // init TabLayout
        tabLayout.setTabData(adapter.getTabEntities());
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (onTabClick(position)) {
                    updateCurrentItem(position);
                }
            }

            @Override
            public void onTabReselect(int position) {
                if (onTabClick(position)) {
                    onTabLayoutReselect(position);
                }
            }
        });
    }

    public void setCurrentTab(int currentTab) {
        if (tabLayout != null && currentTab > -1 && currentTab < adapter.getCount()) {
            tabLayout.setCurrentTab(currentTab);
            updateCurrentItem(currentTab);
        }
    }

    /**
     * is pager scrollable
     *
     * @return scrollable
     */
    protected boolean isPagerScrollable() {
        return false;
    }

    /**
     * get Pager OffscreenPageLimit
     *
     * @return offscreenPageLimit
     */
    protected int getPagerOffscreenPageLimit() {
        return adapter.getCount() - 1;
    }

    protected abstract TabFragmentPagerAdapter createAdapter();

    public void updateCurrentItem(final int newPosition) {
        if (newPosition > -1 && newPosition < adapter.getCount()) {
            pager.setItem(newPosition);
            setCurrentItem(newPosition);
        }
    }

    protected boolean onTabClick(final int position) {
        return true;
    }

    protected void onTabLayoutReselect(int position) {

    }

    protected void setCurrentItem(int position) {
        // Intentionally left blank
    }

    protected Fragment getCurrentFragment() {
        if (tabLayout.getCurrentTab() >= 0 && tabLayout.getCurrentTab() <= adapter.getCount()) {
            return adapter.getItem(tabLayout.getCurrentTab());
        } else {
            return null;
        }
    }

    public void showTabMsg(int position, int num) {
        if (0 >= position || position < adapter.getCount() - 1) {
            if (num <= 0) {
                tabLayout.hideMsg(position);
            } else {
                tabLayout.showMsg(position, num);
            }
        } else {
            LogUtils.e("showTabMsg in error position = " + position);
        }
    }
}
