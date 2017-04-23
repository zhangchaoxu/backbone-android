package com.idogfooding.backbone.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.network.BasePagedResult;
import com.idogfooding.backbone.utils.ToastUtils;
import com.idogfooding.backbone.utils.ViewUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * RecyclerViewFragment
 *
 * @author Charles
 */
public abstract class RecyclerViewFragment<T, A extends RecyclerArrayAdapter<T>> extends BaseFragment {

    protected EasyRecyclerView recyclerView;
    protected FloatingActionButton btnTop;
    protected A adapter;

    protected int pageNumber = 1;
    protected static int pageSize = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.view_list;
    }

    @Override
    protected void onSetupFragment(View view, Bundle savedInstanceState) {
        // find view
        btnTop = (FloatingActionButton) view.findViewById(R.id.btn_top);
        recyclerView = (EasyRecyclerView) view.findViewById(R.id.recycler_view);

        btnTop.setOnClickListener(v -> recyclerView.scrollToPosition(0));

        // init adapter
        createAdapter();

        // init recyclerView
        recyclerView.setLayoutManager(getLayoutManager());

        recyclerView.addItemDecoration(getItemDecoration());

        recyclerView.setAdapterWithProgress(adapter);
        adapter.setOnItemClickListener(position -> onListItemClick(position, adapter.getItem(position)));
        adapter.setOnItemLongClickListener(position -> {
            onListItemLongClick(position, adapter.getItem(position));
            return true;
        });
        adapter.setError(R.layout.view_error, new RecyclerArrayAdapter.OnErrorListener() {
            @Override
            public void onErrorShow() {
                adapter.resumeMore();
            }

            @Override
            public void onErrorClick() {
                adapter.resumeMore();
            }
        });
        if (isLoadMore()) {
            adapter.setMore(R.layout.view_more, new RecyclerArrayAdapter.OnMoreListener() {
                @Override
                public void onMoreShow() {
                    loadData(false, true);
                }

                @Override
                public void onMoreClick() {

                }
            });
            adapter.setNoMore(R.layout.view_no_more);
        }
        if (isRefreshable()) {
            recyclerView.setRefreshListener(() -> {
                pageNumber = 1;
                loadData(true, false);
            });
        }

        initHeaderAndFooterView();
    }

    protected abstract void createAdapter();

    protected void onListItemClick(int position, T data) {
    }

    protected void onListItemLongClick(int position, T data) {
    }

    protected boolean isLoadMore() {
        return true;
    }

    protected boolean isRefreshable() {
        return true;
    }

    protected void initHeaderAndFooterView() {

    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    protected void loadData(boolean refresh, boolean loadMore) {

    }

    protected void loadDataFromCache() {

    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, ViewUtils.dip2px(getContext(), 0.5f), ViewUtils.dip2px(getContext(), 10), ViewUtils.dip2px(getContext(), 10));
        itemDecoration.setDrawLastItem(false);
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
    }

    protected RecyclerView.ItemDecoration getEmptyItemDecoration() {
        DividerDecoration itemDecoration = new DividerDecoration(Color.TRANSPARENT, 0, 0, 0);
        itemDecoration.setDrawLastItem(false);
        itemDecoration.setDrawHeaderFooter(false);
        return itemDecoration;
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
                onLoadComplete();
            }
        };
    }

    protected void onLoadNext(BasePagedResult<T> pagedResult, boolean refresh, boolean loadMore) {
        if (refresh) {
            adapter.clear();
        }
        List<T> list = pagedResult.getList();
        if (list.size() > 0) {
            adapter.addAll(list);
            if (pagedResult.hasNextPage()) {
                pageNumber++;
            } else {
                adapter.stopMore();
            }
        } else {
            adapter.stopMore();
        }

        // cache
        onLoadCache();
    }

    protected void onLoadError(Throwable e) {
        recyclerView.setRefreshing(false);
        recyclerView.showError();
        ToastUtils.show(e.getMessage());
    }

    protected void onLoadComplete() {
        recyclerView.setRefreshing(false);
    }

    protected void onLoadCache() {

    }

    protected Map<String, Object> getPagedQueryMap() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("pageNumber", pageNumber);
        fields.put("pageSize", pageSize);
        return fields;
    }

}
