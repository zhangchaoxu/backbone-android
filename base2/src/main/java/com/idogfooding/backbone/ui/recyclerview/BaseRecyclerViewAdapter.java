package com.idogfooding.backbone.ui.recyclerview;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.idogfooding.backbone.R;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseRecyclerViewAdapter
 * 支持单选和多选模式
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
    protected void convert(K helper, T item) {}

    protected int getColor(int colorRes) {
        return mContext.getResources().getColor(colorRes);
    }

    // single check mode 单选模式
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

    // multi check mode 多选模式
    private List<Integer> checkedPositions = new ArrayList<>();

    public void setMultiCheckedPosition(Integer position) {
        if (checkedPositions.contains(position)) {
            checkedPositions.remove(position);
        } else {
            checkedPositions.add(position);
        }
        notifyDataSetChanged();
    }

    protected boolean hasPositionChecked(int position) {
        if (ObjectUtils.isEmpty(checkedPositions))
            return false;
        else return checkedPositions.contains(position);
    }

    protected List<T> getCheckItems() {
        List<T> checkedItems = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(checkedPositions)) {
            for (Integer pos : checkedPositions) {
                if (null != getItem(pos)) {
                    checkedItems.add(getItem(pos));
                }
            }
        }
        return checkedItems;
    }

    // glide load
    protected void loadImage(ImageView imageView, Object model, RequestOptions requestOptions) {
        Glide.with(mContext).load(model)
                .apply(requestOptions)
                .into(imageView);
    }

    protected void loadImage(ImageView imageView, Object model, int placeholderResId, int errorResId) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId);

        this.loadImage(imageView, model, requestOptions);
    }

    protected void loadImage(ImageView imageView, Object model) {
        this.loadImage(imageView, model, R.mipmap.ic_placeholder, R.mipmap.ic_error);
    }

}
