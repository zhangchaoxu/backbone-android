package com.idogfooding.backbone.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.network.BasePagedResult;
import com.idogfooding.backbone.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * RecyclerViewListFragment
 *
 * @author Charles
 */
public abstract class RecyclerViewListFragment<T, A extends BaseQuickAdapter<T, BaseViewHolder>>
        extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerView mRecyclerView;
    protected A mAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    // page param
    protected int pageNumber = 1;
    protected static int pageSize = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.srl_recycler_view;
    }

    @Override
    protected void onSetupFragment(View view, Bundle savedInstanceState) {
        // init RecyclerVie
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_list);
        cfgRecyclerView();

        // init SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_container);
        cfgSwipeRefresh();

        // init and set adapter
        createAdapter();
        cfgAdapter();
        mRecyclerView.setAdapter(mAdapter);

        initHeaderAndFooterView();
    }

    protected boolean initHeaderAndFooterView() {
        return false;
    }

    protected abstract void createAdapter();

    protected void cfgRecyclerView() {
        // cfg RecyclerView
        mRecyclerView.setLayoutManager(getLayoutManager());
        RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
        if (itemDecoration != null) {
            mRecyclerView.addItemDecoration(itemDecoration);
        }
    }

    protected void cfgSwipeRefresh() {
        // cfg SwipeRefreshLayout
        mSwipeRefreshLayout.setEnabled(isRefreshable());
        if (isRefreshable()) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_start, R.color.swipe_refresh_process, R.color.swipe_refresh_end);
        }
    }

    protected void cfgAdapter() {
        if (isLoadMore()) {
            mAdapter.setOnLoadMoreListener(this, mRecyclerView);
            mAdapter.openLoadAnimation(getLoadAnimation());
        }
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    protected RecyclerView.LayoutManager getGridLayoutManager(int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), spanCount);
        manager.setOrientation(OrientationHelper.VERTICAL);
        return manager;
    }

    protected int getLoadAnimation() {
        return BaseQuickAdapter.ALPHAIN;
    }

    protected boolean isRefreshable() {
        return true;
    }

    protected boolean isLoadMore() {
        return true;
    }

    @Override
    public void onLoadMoreRequested() {
        mSwipeRefreshLayout.setEnabled(false);
        loadData(false, true);
    }

    @Override
    public void onRefresh() {
        mAdapter.setEnableLoadMore(false);
        loadData(true, false);
    }

    protected void loadData(boolean refresh, boolean loadMore) {
        if (mAdapter.getData().isEmpty() && !mSwipeRefreshLayout.isRefreshing()) {
            mAdapter.setEmptyView(R.layout.view_loading);
        }
    }

    // load data
    protected DisposableObserver getDisposableObserver(boolean refresh, boolean loadMore) {
        return new DisposableObserver<BasePagedResult<T>>() {
            @Override
            public void onNext(BasePagedResult<T> pagedResult) {
                onLoadNext(pagedResult, refresh, loadMore);
            }

            @Override
            public void onError(Throwable e) {
                onLoadError(e);
            }

            @Override
            public void onComplete() {
                onLoadFinish();
            }
        };
    }

    protected void onLoadNext(BasePagedResult<T> pagedResult, boolean refresh, boolean loadMore) {
        List<T> list = pagedResult.getList();
        if (list.size() > 0) {
            if (refresh) {
                mAdapter.setNewData(list);
            } else {
                mAdapter.addData(list);
            }
            if (pagedResult.hasNextPage()) {
                pageNumber++;
                mAdapter.loadMoreComplete();
            } else {
                mAdapter.loadMoreEnd();
            }
        } else {
            if (refresh) {
                mAdapter.setEmptyView(R.layout.view_empty);
            }
            mAdapter.loadMoreEnd();
        }

        mSwipeRefreshLayout.setEnabled(isRefreshable());

        // cache
        onLoadCache(refresh, loadMore, pagedResult.getList());
    }

    protected void onLoadError(Throwable e) {
        mSwipeRefreshLayout.setRefreshing(false);
        // recyclerView.showError();
        if (mAdapter.getData().isEmpty()) {
            mAdapter.setEmptyView(R.layout.view_error);
        }
        mAdapter.loadMoreFail();
        ToastUtils.show(e.getMessage());
    }

    protected void onLoadFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void onLoadCache(boolean refresh, boolean loadMore, List<T> list) {

    }

    protected Map<String, Object> getPagedQueryMap() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("pageNumber", pageNumber);
        fields.put("pageSize", pageSize);
        return fields;
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

}
