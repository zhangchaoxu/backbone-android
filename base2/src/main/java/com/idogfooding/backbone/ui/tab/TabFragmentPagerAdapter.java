package com.idogfooding.backbone.ui.tab;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;


/**
 * Pager adapter that provides the current fragment
 *
 * @author Charles
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    protected final Activity activity;

    protected ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    protected ArrayList<Fragment> mFragments = new ArrayList<>();

    public TabFragmentPagerAdapter(FragmentActivity activity) {
        super(activity.getSupportFragmentManager());
        this.activity = activity;
        onConfigPagerAdapter(activity);
        initTabEntities();
        initFragments();
    }

    /**
     * 配置pager adapter
     * @param activity
     */
    protected void onConfigPagerAdapter(FragmentActivity activity) {

    }

    protected void initTabEntities() {
        mTabEntities.clear();
    }

    protected void initFragments() {
        mFragments.clear();
    }

    @Override
    public int getCount() {
        return mTabEntities.size();
    }

    ArrayList<CustomTabEntity> getTabEntities() {
        if (mTabEntities.isEmpty()) {
            initTabEntities();
        }
        return mTabEntities;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position < mTabEntities.size() ? mTabEntities.get(position).getTabTitle() : null;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < 0 || position >= mFragments.size())
            return null;

        return mFragments.get(position);
    }
}
