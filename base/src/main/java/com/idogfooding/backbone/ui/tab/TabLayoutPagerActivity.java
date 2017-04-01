package com.idogfooding.backbone.ui.tab;

import android.os.Bundle;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.ui.BaseActivity;
import com.idogfooding.backbone.widget.ViewPager;

import butterknife.ButterKnife;

/**
 * TabLayoutPagerActivity
 *
 * @author Charles
 */
public abstract class TabLayoutPagerActivity extends BaseActivity {

    CommonTabLayout tabLayout;
    ViewPager pager;
    TabFragmentPagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.tab_layout_pager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabLayout = ButterKnife.findById(this, R.id.tab_layout);
        pager = ButterKnife.findById(this, R.id.pager);

        // setup ViewPager
        pager.setScrollable(true);

        //set up tab layout
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                onTabClick(position);
                updateCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                onTabClick(position);
                onTabLayoutReselect(position);
            }
        });

        //tabLayout.setTabData(mTabEntities, this, android.R.id.tabcontent, fragments);
        adapter = createAdapter();
        pager.setAdapter(adapter);
        tabLayout.setTabData(adapter.getTabEntities());
    }

    protected abstract TabFragmentPagerAdapter createAdapter();

    protected void updateCurrentItem(final int newPosition) {
        if (newPosition > -1 && newPosition < adapter.getCount()) {
            pager.setItem(newPosition);
            setCurrentItem(newPosition);
        }
    }

    protected void onTabClick(final int newPosition) {

    }

    protected void onTabLayoutReselect(int position) {

    }

    protected void setCurrentItem(int position) {
        // Intentionally left blank
    }
}
