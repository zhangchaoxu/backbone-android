package com.idogfooding.backbone.ui.recyclerview;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
    protected void convert(K helper, T item) {
    }

    protected int getColor(int colorRes) {
        return mContext.getResources().getColor(colorRes);
    }

    @ColorInt
    protected int color(@ColorRes int res) {
        return ContextCompat.getColor(mContext, res);
    }

    // 是否多选模式,默认单选模式
    private boolean multiCheckMode = false;

    public boolean isMultiCheckMode() {
        return multiCheckMode;
    }

    public void setMultiCheckMode(boolean multiCheckMode) {
        this.multiCheckMode = multiCheckMode;
    }

    // 选中的位置
    private List<Integer> checkedPositions = new ArrayList<>();

    /**
     * 添加选中记录
     *
     * @param position
     */
    public void addCheckedPosition(Integer position) {
        if (multiCheckMode) {
            // 多选模式
            if (!checkedPositions.contains(position)) {
                checkedPositions.add(position);
            }
        } else {
            // 单选模式
            checkedPositions.clear();
            checkedPositions.add(position);
        }
        notifyDataSetChanged();
    }

    /**
     * 移除选中记录
     *
     * @param position
     */
    public void removeCheckedPosition(Integer position) {
        if (multiCheckMode) {
            // 多选模式
            if (!checkedPositions.contains(position)) {
                checkedPositions.remove(position);
            }
        } else {
            // 单选模式
            checkedPositions.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 设置选中点,切换选中状态
     *
     * @param position
     */
    public void changeCheckedStatus(Integer position) {
        if (checkedPositions.contains(position)) {
            checkedPositions.remove(position);
        } else {
            if (!isMultiCheckMode()) {
                checkedPositions.clear();
            }
            checkedPositions.add(position);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getCheckedPositions() {
        return checkedPositions;
    }

    public int getCheckedPosition() {
        if (checkedPositions.isEmpty()) {
            return -1;
        } else {
            return checkedPositions.get(0);
        }
    }

    public T getCheckedItem() {
        return getItem(getCheckedPosition());
    }

    public List<T> getCheckedItems() {
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

    /**
     * 位置是否选中
     *
     * @param position
     * @return
     */
    public boolean isPositionChecked(int position) {
        if (ObjectUtils.isEmpty(checkedPositions))
            return false;
        else return checkedPositions.contains(position);
    }

    // glide load

    /**
     * 按照指定的RequestOptions加载图片
     */
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
