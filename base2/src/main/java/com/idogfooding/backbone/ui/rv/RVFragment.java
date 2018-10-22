package com.idogfooding.backbone.ui.rv;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.network.ApiException;
import com.idogfooding.backbone.network.PageResult;
import com.idogfooding.backbone.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * RVFragment
 * 支持列表和分页模式
 * BaseQuickAdapter自动加载下一页
 * SwipeRefreshLayout实现下拉刷新
 *
 * @author Charles
 */
public abstract class RVFragment<T, A extends BaseQuickAdapter<T, BaseViewHolder>> extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

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

    protected boolean initHeaderAndFooterView() {
        return false;
    }

    protected abstract void createAdapter();

    /**
     * 设置底部右下角的按钮
     * 默认翻到顶部
     */
    protected boolean cfgTopButton() {
        if (null == mTopButton)
            return false;

        mTopButton.setOnClickListener(v -> smoothScrollToPosition(0));
        return true;
    }

    /**
     * 平滑移动到指定位置
     *
     * @param position 指定位置
     */
    protected boolean smoothScrollToPosition(int position) {
        if (mRecyclerView == null)
            return false;

        mRecyclerView.smoothScrollToPosition(position);
        return true;
    }

    protected boolean cfgRecyclerView() {
        // cfg RecyclerView
        mRecyclerView.setLayoutManager(getLayoutManager());
        clearItemChangeAnimations();
        cfgItemDecoration();
        return true;
    }

    /**
     * fix notifyItemChanged闪烁问题
     * see {https://www.jianshu.com/p/654dac931667}
     */
    protected void clearItemChangeAnimations() {
        if (null != mRecyclerView.getItemAnimator()) {
            mRecyclerView.getItemAnimator().setChangeDuration(0);
        }
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
     * @param spanCount   每行(列)的数目
     * @return LayoutManager
     */
    protected RecyclerView.LayoutManager getGridLayoutManager(@RecyclerView.Orientation int orientation, int spanCount) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), spanCount);
        manager.setOrientation(orientation);
        return manager;
    }

    /**
     * 获得瀑布布局
     *
     * @param orientation 布局方向
     * @param spanCount   每行(列)的数目
     * @return LayoutManager
     */
    protected RecyclerView.LayoutManager getStaggeredGridLayoutManager(@RecyclerView.Orientation int orientation, int spanCount) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(spanCount, orientation);
        return manager;
    }

    /**
     * 配置下拉刷新
     */
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
            mAdapter.setEmptyView(R.layout.view_loading,  (ViewGroup) mRecyclerView.getParent());
        }
    }

    protected void onLoadNext(List<T> list, boolean refresh) {
        this.onLoadNext(new PageResult<>(list), refresh, false);
    }

    protected void onLoadNext(PageResult<T> pagedResult, boolean refresh, boolean loadMore) {
        if (pagedResult == null || null == pagedResult.getList()) {
            pagedResult = new PageResult<>(new ArrayList<>());
        }
        List<T> list = pagedResult.getList();

        if (refresh) {
            mAdapter.setNewData(list);
        } else {
            mAdapter.addData(list);
        }

        if (list.size() > 0) {
            if (pagedResult.isLastPage()) {
                mAdapter.loadMoreEnd();
            } else {
                pageNumber++;
                mAdapter.loadMoreComplete();
            }
        } else {
            if (refresh) {
                mAdapter.setEmptyView(getEmptyView());
            }
            mAdapter.loadMoreEnd();
        }

        mSwipeRefreshLayout.setEnabled(refreshable);
        // cache
        onLoadCache(refresh, loadMore, pagedResult.getList());
    }

    protected View emptyView;
    protected View errorView;

    protected View getEmptyView() {
        return getEmptyView(R.layout.view_empty);
    }

    protected View getEmptyView(int viewResId) {
        if (emptyView == null) {
            emptyView = getLayoutInflater().inflate(viewResId, (ViewGroup) mRecyclerView.getParent(), false);
            emptyView.setOnClickListener(v -> onRefresh());
        }
        return emptyView;
    }

    protected View getErrorView() {
        return getEmptyView(R.layout.view_error);
    }

    protected View getErrorView(int viewResId) {
        if (errorView == null) {
            errorView = getLayoutInflater().inflate(viewResId, (ViewGroup) mRecyclerView.getParent(), false);
            errorView.setOnClickListener(v -> onRefresh());
        }
        return errorView;
    }

    /**
     * on list load error
     */
    protected void onLoadError(Throwable throwable) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (mAdapter.getData().isEmpty()) {
            mAdapter.setEmptyView(getErrorView());
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
