package com.idogfooding.backbone.photo;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;

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
    private boolean addEnable = true; // 是否允许添加
    private int morePhotoResId = R.mipmap.ic_photo_add; // 添加更多的图片资源

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public boolean isAddEnable() {
        return addEnable;
    }

    public void setAddEnable(boolean addEnable) {
        this.addEnable = addEnable;
    }

    public int getMorePhotoResId() {
        return morePhotoResId;
    }

    public void setMorePhotoResId(int morePhotoResId) {
        this.morePhotoResId = morePhotoResId;
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
    }

    @Override
    protected void convert(BaseViewHolder holder, PhotoPickerEntity data) {
        //Step.3
        switch (holder.getItemViewType()) {
            case PhotoPickerEntity.TYPE_ADD:
                ((ImageView)holder.getView(R.id.iv_photo)).setImageResource(getMorePhotoResId());
                break;
            case PhotoPickerEntity.TYPE_FILE:
                holder.setVisible(R.id.iv_delete, false);
                Uri uri = Uri.fromFile(new File(data.getPath()));
                Glide.with(mContext)
                        .load(uri)
                        .apply(new RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_photo_placeholder)
                                .error(R.mipmap.ic_photo_placeholder))
                        .thumbnail(0.1f)
                        .into((ImageView) holder.getView(R.id.iv_photo));
                break;
            case PhotoPickerEntity.TYPE_URL:
                holder.setVisible(R.id.iv_delete, false);
                Glide.with(mContext)
                        .load(data.getThumbnail())
                        .apply(new RequestOptions()
                                .centerCrop()
                                .placeholder(R.mipmap.ic_photo_placeholder)
                                .error(R.mipmap.ic_photo_placeholder))
                        .thumbnail(0.1f)
                        .into((ImageView) holder.getView(R.id.iv_photo));
                break;
        }

    }

    @Override
    public void setNewData(@Nullable List<PhotoPickerEntity> data) {
        if (addEnable) {
            if (data == null)
                data = new ArrayList<>();
            if (data.size() < getMaxCount())
                data.add(new PhotoPickerEntity(PhotoPickerEntity.TYPE_ADD));
            super.setNewData(data);
        } else {
            super.setNewData(data);
        }
    }

    @Override
    public void addData(@NonNull Collection<? extends PhotoPickerEntity> newData) {
        super.addData(newData);
    }

    public List<PhotoPickerEntity> getRealPhotoEntities() {
        List<PhotoPickerEntity> list = getData();
        for (PhotoPickerEntity entity : list) {
            if (entity.getType() == PhotoPickerEntity.TYPE_ADD) {
                list.remove(entity);
            }
        }
        return list;
    }

    public ArrayList<String> getRealPhotos() {
        ArrayList<String> list = new ArrayList<>();
        for (PhotoPickerEntity entity : getData()) {
            if (entity.getType() != PhotoPickerEntity.TYPE_ADD) {
                list.add(entity.getPath());
            }
        }
        return list;
    }
}