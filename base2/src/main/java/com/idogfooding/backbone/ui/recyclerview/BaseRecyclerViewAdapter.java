package com.idogfooding.backbone.ui.recyclerview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * BaseRecyclerViewAdapter
 *
 * @author Charles
 */
public class BaseRecyclerViewAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public BaseRecyclerViewAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseRecyclerViewAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseRecyclerViewAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(K helper, T item) {

    }

    protected int getColor(int colorRes) {
        return mContext.getResources().getColor(colorRes);
    }

    // check mode
    private int checkedPosition = -1;

    public void setCheckedPosition(int position) {
        checkedPosition = position;
        notifyDataSetChanged();
    }

    public int getCheckedPosition() {
        return checkedPosition;
    }

    public T getCheckedItem() {
        if (checkedPosition < 0 || checkedPosition >= getItemCount()) {
            return null;
        } else {
            return getItem(checkedPosition);
        }
    }
}
