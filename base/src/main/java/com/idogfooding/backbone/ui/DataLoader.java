package com.idogfooding.backbone.ui;

import android.os.Bundle;
import android.os.Looper;

import com.idogfooding.backbone.BaseApplication;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.event.NetworkEvent;
import com.idogfooding.backbone.utils.NetworkUtils;
import com.orhanobut.logger.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * DataLoader
 */
public class DataLoader {
    private static final String PAGE_STATE = "page";
    private static final String LOADING_STATE = "isLoading";
    private static final String REFRESHING_STATE = "isRefreshing";
    private static final String DATA_LOAD_STATE = "dataLoaded";
    private static final String DATA_END_STATE = "dataEnd";
    private static final String LAZY_LOAD_STATE = "lazyLoad";
    private static final String NETWORK_STATE = "useNetwork";

    private LoadCallback callback;

    private boolean isDataLoaded;
    private boolean isDataEnd;
    private boolean isLazyLoadEnabled;
    private boolean useNetwork;
    private AtomicBoolean isLoading;
    private AtomicBoolean isRefreshing;

    private int mPage;

    private DataLoader(LoadCallback callback) {
        isLoading = new AtomicBoolean(false);
        isRefreshing = new AtomicBoolean(false);
        this.callback = callback;
    }

    public static DataLoader init(boolean useNetwork, LoadCallback callbacks) {
        DataLoader dataLoadManager = new DataLoader(callbacks);
        dataLoadManager.resetPage(); // init page
        dataLoadManager.useNetwork(useNetwork);
        return dataLoadManager;
    }

    private static boolean hasNetwork() {
        return NetworkUtils.isNetworkConnected(BaseApplication.context());
    }

    // ########## Load Process ##########

    public void startRefresh() {
        if (isRefreshing() || isLoading()) {
            Logger.t(toString()).d("refresh is in progress, dismiss");
            return;
        } else {
            isRefreshing.set(true);
        }
        if (callback == null) {
            Logger.t(toString()).d("load callback is null");
            isRefreshing.set(false); // reset state
            return;
        }
        if (useNetwork && !hasNetwork()) {
            new NetworkEvent(BaseApplication.resource().getString(R.string.msg_network_unavailable)).post();
            isRefreshing.set(false); // reset state
            Logger.t(toString()).d("network not available, dismiss");
            return;
        }

        // ready to start
        resetPage();
        if (callback instanceof ListLoadCallback) {
            ((ListLoadCallback) callback).onRefreshStart();
        } else {
            isRefreshing.set(false); // reset state
            startLoad();
        }
    }

    public void startLoad() {
        onDataLoad(false);
    }

    public void startLazyLoad() {
        onDataLoad(true);
    }

    private void onDataLoad(boolean lazyMode) {
        if (isRefreshing() || isLoading()) {
            Logger.t(toString()).d("load is in progress, dismiss");
            return;
        } else {
            isLoading.set(true);
        }
        if (callback == null) {
            Logger.t(toString()).d("load callback is null");
            isLoading.set(false); // reset state
            return;
        }

        boolean lazyLoad = isLazyLoadEnabled() && lazyMode;
        boolean normalLoad = !isLazyLoadEnabled() && !lazyMode;

        if (!isDataLoaded() && !isDataEnd() && (lazyLoad || normalLoad)) {
            if (useNetwork && !hasNetwork()) {
                new NetworkEvent(BaseApplication.resource().getString(R.string.msg_network_unavailable)).post();
                isLoading.set(false); // reset state
                Logger.t(toString()).d("network not available, dismiss");
                return;
            }

            boolean isDataLoaded = callback.onLoadStart();
            setDataLoaded(isDataLoaded);
        } else {
            isLoading.set(false);
            Logger.t(toString())
                    .d(isDataLoaded() ? "isDataLoaded" : isDataEnd ? "isDataEnd"
                            : (lazyLoad || normalLoad) ? "load mode" : "unknown" + " is true, dismiss");
        }
    }

    /**
     * you shouldn't manually call this only if you understanding what you are doing.
     */
    public void forceLoad() {
        if (isRefreshing() || isLoading()) {
            Logger.t(toString()).d("load is in progress, dismiss");
            return;
        } else {
            isLoading.set(true);
        }
        if (callback == null) {
            Logger.t(toString()).d("load callback is null");
            isLoading.set(false); // reset state
            return;
        }
        if (useNetwork && !hasNetwork()) {
            new NetworkEvent(BaseApplication.resource().getString(R.string.msg_network_unavailable)).post();
            isLoading.set(false); // reset state
            Logger.t(toString()).d("network not available, dismiss");
            return;
        }

        if (!isDataEnd()) {
            callback.onLoadStart();
        } else {
            Logger.t(toString()).d("isDataEnd is true, dismiss");
            isLoading.set(false);
        }
    }

    // ############ Flags ############

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    // mark data requested, then it won't call startLoad() again in onResume().
    public void setDataLoaded(boolean loaded) {
        isDataLoaded = loaded;
        isLazyLoadEnabled = false;
    }

    public boolean isDataEnd() {
        return isDataEnd;
    }

    /**
     * mark data is on the bottom, no more refresh should be requested.
     */
    public void setDataEnd(boolean end) {
        isDataEnd = end;
    }

    public boolean isLazyLoadEnabled() {
        return isLazyLoadEnabled;
    }

    /**
     * better be used with fragments in ViewPager
     */
    public void enableLazyLoad() {
        this.isLazyLoadEnabled = true;
    }

    public boolean isLoading() {
        return isLoading.get();
    }

    public boolean isRefreshing() {
        return isRefreshing.get();
    }

    public void useNetwork(boolean useNetwork) {
        this.useNetwork = useNetwork;
    }

    // ########### Functions ###########

    public void notifyPageLoaded() {
        isLoading.set(false);
        isRefreshing.set(false);
        if (callback != null && callback instanceof ListLoadCallback) {
            mPage++;
            if (Looper.myLooper() != Looper.getMainLooper()) {
                Observable.just(null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) {
                                ((ListLoadCallback) callback).notifyDataLoaded();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
            } else {
                ((ListLoadCallback) callback).notifyDataLoaded();
            }
        }
        setDataLoaded(true);
    }

    public void notifyPageLoadFailed() {
        isLoading.set(false);
        isRefreshing.set(false);
        setDataLoaded(false);
        if (callback != null && callback instanceof ListLoadCallback) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                Observable.just(null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {
                            @Override
                            public void accept(Object o) {
                                ((ListLoadCallback) callback).notifyDataLoaded();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        });
            } else {
                ((ListLoadCallback) callback).notifyDataLoaded();
            }
        }
    }

    /**
     * It's work in main thread, make sure it will be called after scheduler transformer
     */
    /*public <T> Observable.Transformer<T, T> rxNotifier() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                notifyPageLoaded();
                            }
                        })
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                notifyPageLoadFailed();
                            }
                        });
            }
        };
    }*/

    public int getPage() {
        return mPage;
    }

    public void resetPage() {
        mPage = 1;
    }

    public void destroy() {
        callback = null;
    }

    public void onSavedState(Bundle savedInstanceState) {
        savedInstanceState.putInt(PAGE_STATE, mPage);
        savedInstanceState.putBoolean(LOADING_STATE, isLoading());
        savedInstanceState.putBoolean(REFRESHING_STATE, isRefreshing());
        savedInstanceState.putBoolean(DATA_LOAD_STATE, isDataLoaded);
        savedInstanceState.putBoolean(DATA_END_STATE, isDataEnd);
        savedInstanceState.putBoolean(LAZY_LOAD_STATE, isLazyLoadEnabled);
        savedInstanceState.putBoolean(NETWORK_STATE, useNetwork);
    }

    public void onRestoreState(Bundle savedInstanceState) {
        mPage = savedInstanceState.getInt(PAGE_STATE);
        isLoading.set(savedInstanceState.getBoolean(LOADING_STATE));
        isRefreshing.set(savedInstanceState.getBoolean(REFRESHING_STATE));
        savedInstanceState.putBoolean(DATA_LOAD_STATE, isDataLoaded);
        savedInstanceState.putBoolean(DATA_END_STATE, isDataEnd);
        savedInstanceState.putBoolean(LAZY_LOAD_STATE, isLazyLoadEnabled);
        savedInstanceState.putBoolean(NETWORK_STATE, useNetwork);
    }

    public interface LoadCallback {
        /**
         * load data in this callback, should be called on setUserVisibleHint() at first time and later on onResume()
         *
         * @return true if data load finished, which means it won't load data again while visible lifecycle.
         * If you want it handled by load task, leave it false.
         */
        boolean onLoadStart();
    }

    public interface ListLoadCallback extends LoadCallback {
        /**
         * setRefresh data list due to loadType, should be called in onLoadStart().
         */
        void onRefreshStart();

        void notifyDataLoaded();
    }
}
