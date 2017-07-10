package com.idogfooding.backbone.ui.recyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.network.ApiException;
import com.idogfooding.backbone.network.BasePagedResult;
import com.idogfooding.backbone.ui.BaseFragment;
import com.idogfooding.backbone.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * RecyclerViewFragment
 *
 * @author Charles
 */
public abstract class RecyclerViewFragment<T, A extends BaseQuickAdapter<T, BaseViewHolder>>
        extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    protected RecyclerView mRecyclerView;
    protected A mAdapter;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected FloatingActionButton mTopButton;

    // page param
    protected int pageNumber = 1;
    protected static int pageSize = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.srl_recycler_view;
    }

    @Override
    protected void onSetupFragment(View view, Bundle savedInstanceState) {
        mTopButton = (FloatingActionButton) view.findViewById(R.id.fab_top);
        cfgTopButton();

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

    protected void cfgTopButton() {
        if (null == mTopButton)
            return;

        mTopButton.setOnClickListener(v -> mRecyclerView.smoothScrollToPosition(0));
    }

    protected void cfgRecyclerView() {
        // cfg RecyclerView
        mRecyclerView.setLayoutManager(getLayoutManager());
        cfgItemDecoration();
    }

    protected void cfgItemDecoration() {

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
        } else {
            mAdapter.bindToRecyclerView(mRecyclerView);
        }
        mAdapter.openLoadAnimation(getLoadAnimation());
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
        pageNumber = 1;
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

        if (refresh) {
            mAdapter.setNewData(list);
        } else {
            mAdapter.addData(list);
        }

        if (list.size() > 0) {
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

    /**
     * on list load error
     */
    protected void onLoadError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (mAdapter.getData().isEmpty()) {
            mAdapter.setEmptyView(R.layout.view_error);
        }
        mAdapter.loadMoreFail();
        if (throwable instanceof ApiException) {
            onLoadApiException((ApiException) throwable);
        } else {
            ToastUtils.show(getString(R.string.msg_data_error) + ":" + throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    protected void onLoadApiException(ApiException e) {
        ToastUtils.show(e.getCode() + ":" + e.getMessage());
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

}
