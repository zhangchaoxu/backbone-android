package com.idogfooding.backbone.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.idogfooding.backbone.utils.Fragments;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseFragment
 * lazy load {http://www.cnblogs.com/leevey/p/5678037.html}
 *
 * @author Charles
 */
public abstract class BaseFragment extends RxFragment {

    protected final String TAG = getClass().getSimpleName();

    public String getSimpleName() {
        return TAG;
    }

    private Unbinder unbinder;
    private DataLoader mDataLoader;
    private boolean mRegisterEventBus;

    private boolean isPrepared;
    private boolean isFirstResume = true;
    private boolean isFirstInvisible = true;
    private boolean isFirstVisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstInvisible();
            } else {
                onInvisible();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        Bundle data = getArguments();
        if (data != null) {
            onBundleReceived(data);
        }
        restoreFragmentState(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getLayoutView(inflater, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        onSetupFragment(view, savedInstanceState);

        if (getDataLoader() != null) {
            if (savedInstanceState != null) {
                getDataLoader().onRestoreState(savedInstanceState);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onVisible();
        }

        // register eventBus
        if (mRegisterEventBus) {
            EventBus eventBus = EventBus.getDefault();
            if (!eventBus.isRegistered(this)) {
                eventBus.register(this);
            }
        }

        // handle data loader
        if (mDataLoader != null) {
            mDataLoader.startLoad();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (getUserVisibleHint()) {
            onInvisible();
        }

        if (mRegisterEventBus) {
            EventBus eventBus = EventBus.getDefault();
            if (eventBus.isRegistered(this)) {
                eventBus.unregister(this);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // BINDING RESET
        // Fragments have a different view lifecycle than activities.
        // When binding a fragment in onCreateView, set the views to null in onDestroyView.
        // Butter Knife returns an Unbinder instance when you call bind to do this for you.
        // Call its unbind method in the appropriate lifecycle callback.
        unbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mDataLoader != null) {
            mDataLoader.destroy();
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * on fragment first visible
     * do init
     */
    public void onFirstVisible() {

    }

    /**
     * fragment visible
     * onResume or scroll to this fragment
     */
    public void onVisible() {

    }

    /**
     * on first invisible
     * on first fragment invisible
     * *do nothing is this method as setUserVisibleHint called before onCreate, bound and view will be null*
     */
    public void onFirstInvisible() {

    }

    /**
     * on fragment invisible
     * scroll out or onPause
     */
    public void onInvisible() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Fragments.KEY_RESTORE, isVisible());
        outState.putBoolean(Fragments.KEY_RESTORE_VIEWPAGER, getView() != null && getView().getParent() instanceof ViewPager);
        if (getDataLoader() != null) {
            getDataLoader().onSavedState(outState);
        }
    }

    public DataLoader getDataLoader() {
        return mDataLoader;
    }

    /**
     * register EventBus on resume/pause by default, must be called before onResume/onPause
     */
    protected void registerEventBus() {
        mRegisterEventBus = true;
    }

    /**
     * use a data loader to control data load state
     *
     * @param useNetwork if is network data loader, it will not request if no network there.
     */
    protected DataLoader registerDataLoader(boolean useNetwork, DataLoader.LoadCallback callback) {
        mDataLoader = DataLoader.init(useNetwork, callback);
        return mDataLoader;
    }

    private void restoreFragmentState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean(Fragments.KEY_RESTORE_VIEWPAGER, false)) {
                return;
            }
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (savedInstanceState.getBoolean(Fragments.KEY_RESTORE, false)) {
                transaction.show(this);
            } else {
                transaction.hide(this);
            }
            transaction.commitAllowingStateLoss();
        }
    }

    /**
     * called when onCreate and fragment has Arguments
     */
    protected void onBundleReceived(Bundle data) {
        Logger.d("onBundleReceived, data = " + data.toString());
    }

    protected int getLayoutId() {
        return 0;
    }

    protected View getLayoutView(LayoutInflater inflater, @Nullable ViewGroup container) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    protected abstract void onSetupFragment(View view, Bundle savedInstanceState);

    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    protected void finishActivity() {
        if (null != getActivity()) {
            getActivity().finish();
        }
    }

}
