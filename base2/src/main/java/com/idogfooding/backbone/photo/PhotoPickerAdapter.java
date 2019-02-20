package com.idogfooding.backbone.photo;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.idogfooding.backbone.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * PhotoPickerAdapter
 * 图片选择器
 *
 * @author Charles
 */
public class PhotoPickerAdapter extends BaseQuickAdapter<PhotoPickerEntity, BaseViewHolder> {

    private int maxCount = 9; // 最大支持数量
    private boolean moreEnable = true; // 是否允许添加
    private boolean deleteEnable = false; // 是否允许删除
    private int morePhotoResId = R.mipmap.ic_photo_add; // 添加更多的图片资源
    private int deletePhotoResId = R.mipmap.ic_photo_delete; //删除的图片资源

    public int getMaxCount() {
        return maxCount;
    }

    public PhotoPickerAdapter setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    public boolean isMoreEnable() {
        return moreEnable;
    }

    public PhotoPickerAdapter setMoreEnable(boolean moreEnable) {
        this.moreEnable = moreEnable;
        return this;
    }

    public boolean isDeleteEnable() {
        return deleteEnable;
    }

    public PhotoPickerAdapter setDeleteEnable(boolean deleteEnable) {
        this.deleteEnable = deleteEnable;
        return this;
    }

    public int getMorePhotoResId() {
        return morePhotoResId;
    }

    public PhotoPickerAdapter setMorePhotoResId(int morePhotoResId) {
        this.morePhotoResId = morePhotoResId;
        return this;
    }

    public int getDeletePhotoResId() {
        return deletePhotoResId;
    }

    public PhotoPickerAdapter setDeletePhotoResId(int deletePhotoResId) {
        this.deletePhotoResId = deletePhotoResId;
        return this;
    }

    /**
     * 初始化一下，把更多按钮加上
     */
    public PhotoPickerAdapter init() {
        if (isMoreEnable()) {
            List<PhotoPickerEntity> data = new ArrayList<>();
            data.add(new PhotoPickerEntity(PhotoPickerEntity.TYPE_ADD));
            super.setNewData(data);
        }
        return this;
    }

    public PhotoPickerAdapter(ArrayList<PhotoPickerEntity> photoPaths) {
        super(photoPaths);
        //Step.1
        setMultiTypeDelegate(new MultiTypeDelegate<PhotoPickerEntity>() {
            @Override
            protected int getItemType(PhotoPickerEntity entity) {
                return entity.getType();
            }
        });
        //Step.2
        getMultiTypeDelegate().registerItemType(PhotoPickerEntity.TYPE_ADD, R.layout.__picker_item_photo_add)
                .registerItemType(PhotoPickerEntity.TYPE_FILE, R.layout.__picker_item_photo)
                .registerItemType(PhotoPickerEntity.TYPE_URL, R.layout.__picker_item_photo);
        // click listener
        setOnItemChildClickListener((adapter, view, position) -> {
            if (R.id.iv_delete == view.getId()) {
                remove(position);
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder holder, PhotoPickerEntity data) {
        //Step.3
        switch (holder.getItemViewType()) {
            case PhotoPickerEntity.TYPE_ADD:
                ((ImageView) holder.getView(R.id.iv_photo)).setImageResource(getMorePhotoResId());
                break;
            case PhotoPickerEntity.TYPE_FILE:
                Glide.with(mContext)
                        .load(Uri.fromFile(new File(StringUtils.isTrimEmpty(data.getThumbnail()) ? data.getPath() : data.getThumbnail())))
                        .apply(new RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_photo_placeholder)
                                .error(R.mipmap.ic_photo_placeholder))
                        .thumbnail(0.1f)
                        .into((ImageView) holder.getView(R.id.iv_photo));
                if (isDeleteEnable()) {
                    holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                    ((ImageView) holder.getView(R.id.iv_delete)).setImageResource(getDeletePhotoResId());
                } else {
                    holder.getView(R.id.iv_delete).setVisibility(View.GONE);
                }
                holder.addOnClickListener(R.id.iv_delete);
                break;
            case PhotoPickerEntity.TYPE_URL:
                Glide.with(mContext)
                        .load(StringUtils.isTrimEmpty(data.getThumbnail()) ? data.getPath() : data.getThumbnail())
                        .apply(new RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_photo_placeholder)
                                .error(R.mipmap.ic_photo_placeholder))
                        .thumbnail(0.1f)
                        .into((ImageView) holder.getView(R.id.iv_photo));
                if (isDeleteEnable()) {
                    holder.getView(R.id.iv_delete).setVisibility(View.VISIBLE);
                    ((ImageView) holder.getView(R.id.iv_delete)).setImageResource(getDeletePhotoResId());
                } else {
                    holder.getView(R.id.iv_delete).setVisibility(View.GONE);
                }
                holder.addOnClickListener(R.id.iv_delete);
                break;
        }
    }

    @Override
    public void remove(int position) {
        if (isMoreEnable() && getRealPhotoCount() == getMaxCount()) {
            super.remove(position);
            getData().add(new PhotoPickerEntity(PhotoPickerEntity.TYPE_ADD));
        } else {
            super.remove(position);
        }
    }

    @Override
    public void setNewData(@Nullable List<PhotoPickerEntity> data) {
        if (isMoreEnable()) {
            if (data == null)
                data = new ArrayList<>();

            if (data.size() < getMaxCount())
                data.add(new PhotoPickerEntity(PhotoPickerEntity.TYPE_ADD));
        }
        super.setNewData(data);
    }

    @Override
    public void addData(@NonNull Collection<? extends PhotoPickerEntity> newData) {
        List<PhotoPickerEntity> list = getRealPhotoEntities();
        list.addAll(newData);
        this.setNewData(list);
    }

    @Override
    public void addData(@NonNull PhotoPickerEntity entity) {
        List<PhotoPickerEntity> list = getRealPhotoEntities();
        list.add(entity);
        this.setNewData(list);
    }

    /**
     * 获得实际的图片数量
     */
    public int getRealPhotoCount() {
        int count = 0;
        for (PhotoPickerEntity entity : getData()) {
            if (entity.getType() != PhotoPickerEntity.TYPE_ADD) {
                count ++;
            }
        }
        return count;
    }

    public List<PhotoPickerEntity> getRealPhotoEntities() {
        if (isMoreEnable()) {
            List<PhotoPickerEntity> list = getData();
            for (PhotoPickerEntity entity : list) {
                if (entity.getType() == PhotoPickerEntity.TYPE_ADD) {
                    list.remove(entity);
                }
            }
            return list;
        } else {
            return getData();
        }
    }

    public ArrayList<String> getRawPhotos() {
        ArrayList<String> list = new ArrayList<>();
        for (PhotoPickerEntity entity : getData()) {
            if (entity.getType() != PhotoPickerEntity.TYPE_ADD) {
                list.add(entity.getPath());
            }
        }
        return list;
    }

    public ArrayList<String> getThumbnailPhotos() {
        ArrayList<String> list = new ArrayList<>();
        for (PhotoPickerEntity entity : getData()) {
            if (entity.getType() != PhotoPickerEntity.TYPE_ADD) {
                list.add(entity.getThumbnail());
            }
        }
        return list;
    }
}