package com.idogfooding.backbone.ui.rv;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.network.ApiException;
import com.idogfooding.backbone.network.PageResult;
import com.idogfooding.backbone.ui.BaseFragment;

import java.util.List;

/**
 * RecyclerViewFragment
 * support list and page
 * support refresh and auto load next page
 *
 * @author Charles
 */
public abstract class RecyclerViewFragment<T, A extends BaseQuickAdapter<T, BaseViewHolder>> extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    // Refresh and RecyclerView
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected FloatingActionButton mTopButton;
    protected RecyclerView mRecyclerView;
    protected A mAdapter;

    // page param
    protected int pageNumber = 1;
    protected static int pageSize = 10;
    protected boolean loadMore = true; // 是否自动加载下一页
    protected boolean refreshable = true; // 是否可刷新

    @Override
    protected int getLayoutId() {
        return R.layout.srl_recycler_view;
    }

    @Override
    protected void onSetup(View view, Bundle savedInstanceState) {
        super.onSetup(view, savedInstanceState);

        onConfigureRecyclerView();
        mTopButton = view.findViewById(R.id.fab_top);
        cfgTopButton();

        // init RecyclerVie
        mRecyclerView = view.findViewById(R.id.rv_list);
        cfgRecyclerView();

        // init SwipeRefreshLayout
        mSwipeRefreshLayout = view.findViewById(R.id.srl_container);
        cfgSwipeRefresh();

        // init and set adapter
        createAdapter();
        cfgAdapter();
        mRecyclerView.setAdapter(mAdapter);

        initHeaderAndFooterView();
    }

    /**
     * 配置RecyclerView
     * 比如设置refreshable和loadMore
     */
    protected void onConfigureRecyclerView() {

    }

    protected boolean initHeaderAndFooterView() {
        return false;
    }

    protected abstract void createAdapter();

    protected void cfgTopButton() {
        if (null == mTopButton)
            return;

        mTopButton.setOnClickListener(v -> smoothScrollToPosition(0));
    }

    /**
     * 平滑移动到指定位置
     *
     * @param position
     */
    protected void smoothScrollToPosition(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    protected void cfgRecyclerView() {
        // cfg RecyclerView
        mRecyclerView.setLayoutManager(getLayoutManager());
        clearItemChangeAnimations();
        cfgItemDecoration();
    }

    /**
     * fix notifyItemChanged闪烁问题
     * see {https://www.jianshu.com/p/654dac931667}
     */
    protected void clearItemChangeAnimations() {
        mRecyclerView.getItemAnimator().setChangeDuration(0);
    }

    /**
     * 配置分割修饰,支持divider和space
     * divider支持高度和颜色自定义,多用于LinearLayout
     * space支持间隔和颜色自定义,多用于GridLayout
     */
    protected void cfgItemDecoration() {
        mRecyclerView.addItemDecoration(new DividerDecoration());
    }

    /**
     * 获得布局，支持LinearLayout和GridLayout
     *
     * @return LayoutManager
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    /**
     * 获得Grid布局
     *
     * @param orientation 布局方向
     * @param spanCount   grid条数
     * @return LayoutManager
     */
    protected RecyclerView.LayoutManager getGridLayoutManager(@RecyclerView.Orientation int orientation, int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), spanCount);
        manager.setOrientation(orientation);
        return manager;
    }

    protected void cfgSwipeRefresh() {
        // cfg SwipeRefreshLayout
        mSwipeRefreshLayout.setEnabled(refreshable);
        if (refreshable) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_start, R.color.swipe_refresh_process, R.color.swipe_refresh_end);
        }
    }

    protected void cfgAdapter() {
        if (loadMore) {
            mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        } else {
            mAdapter.bindToRecyclerView(mRecyclerView);
        }
        mAdapter.openLoadAnimation(getLoadAnimation());
    }

    protected int getLoadAnimation() {
        return BaseQuickAdapter.ALPHAIN;
    }

    protected void setRefreshable(boolean refreshable) {
        this.refreshable = refreshable;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
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

    protected void onLoadNext(List<T> list, boolean refresh) {
        this.onLoadNext(new PageResult<>(list), refresh, false);
    }

    protected void onLoadNext(PageResult<T> pagedResult, boolean refresh, boolean loadMore) {
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

        mSwipeRefreshLayout.setEnabled(refreshable);
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
            ToastUtils.showShort(getString(R.string.msg_data_error) + ":" + throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    protected void onLoadApiException(ApiException e) {
        ToastUtils.showShort(e.getCode() + ":" + e.getMessage());
    }

    protected void onLoadFinish() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void onLoadCache(boolean refresh, boolean loadMore, List<T> list) {

    }

}
