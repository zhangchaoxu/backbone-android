package com.idogfooding.backbone.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.idogfooding.backbone.R;
import com.idogfooding.backbone.utils.ViewUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

/**
 * ListFragment
 *
 * @author Charles
 */
public abstract class ListFragment<T, A extends RecyclerArrayAdapter<T>> extends BaseFragment {

    protected EasyRecyclerView recyclerView;
    protected FloatingActionButton btnTop;
    protected A adapter;

    protected int pageNumber = 1;

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

        // init recyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, ViewUtils.dip2px(getContext(), 0.5f), ViewUtils.dip2px(getContext(), 72), 0);
        itemDecoration.setDrawLastItem(false);
        itemDecoration.setDrawHeaderFooter(false);
        recyclerView.addItemDecoration(itemDecoration);

        // init adapter
        createAdapter();
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
            adapter.setNoMore(R.layout.view_nomore, new RecyclerArrayAdapter.OnNoMoreListener() {
                @Override
                public void onNoMoreShow() {
                    adapter.resumeMore();
                }

                @Override
                public void onNoMoreClick() {
                    adapter.resumeMore();
                }
            });
        }
        if (isRefreshable()) {
            recyclerView.setRefreshListener(() -> loadData(true, false));
        }

        initHeaderAndFooterView();

        loadData(false, false);
    }

    protected abstract void createAdapter();

    protected void onListItemClick(int position, T data) {}

    protected void onListItemLongClick(int position, T data) {}

    protected boolean isLoadMore() {
        return true;
    }

    protected boolean isRefreshable() {
        return true;
    }

    protected void initHeaderAndFooterView() {

    }

    protected void loadData(boolean refresh, boolean loadMore) {
        if (refresh) {
            pageNumber = 1;
        }
    }

}
