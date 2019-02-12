package com.idogfooding.backbone.photo;

import com.blankj.utilcode.util.ObjectUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * photo picker entity
 *
 * @author Charles
 */
public class PhotoPickerEntity implements Serializable {

    public final static int TYPE_ADD = 1; // 添加图片
    public final static int TYPE_FILE = 2; // 文件
    public final static int TYPE_URL = 3; // http开头的网址

    public PhotoPickerEntity(int type) {
        this.type = type;
    }

    public PhotoPickerEntity(int type, String path) {
        this.type = type;
        this.path = path;
        this.thumbnail = path;
    }

    public PhotoPickerEntity(int type, String path, String thumbnail) {
        this.type = type;
        this.path = path;
        this.thumbnail = thumbnail;
    }

    private int type;
    private String path;
    private String thumbnail;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     * 将文件数组转换为entity数组
     * @param files 文件数组
     * @return entity数组
     */
    public static List<PhotoPickerEntity> filesToEntities(List<File> files) {
        List<PhotoPickerEntity> entities = new ArrayList<>();
        for (File file : files) {
            entities.add(new PhotoPickerEntity(PhotoPickerEntity.TYPE_FILE, file.getPath()));
        }
        return entities;
    }

    /**
     * 将文件数组转换为entity数组
     */
    public static List<PhotoPickerEntity> pathsToEntities(List<String> rawPaths, List<String> thumbnailPaths) {
        if (ObjectUtils.isEmpty(rawPaths) || ObjectUtils.isEmpty(thumbnailPaths)) {
            return new ArrayList<>();
        } else if (rawPaths.size() != thumbnailPaths.size()) {
            return new ArrayList<>();
        } else {
            List<PhotoPickerEntity> entities = new ArrayList<>();
            for (int i = 0; i < rawPaths.size(); i++) {
                entities.add(new PhotoPickerEntity(PhotoPickerEntity.TYPE_FILE, rawPaths.get(i), thumbnailPaths.get(i)));
            }
            return entities;
        }
    }


    /**
     * 将文件数组转换为entity数组
     */
    public static List<PhotoPickerEntity> filesAndPathsToEntities(List<String> rawPaths, List<File> thumbnailFiles) {
        if (ObjectUtils.isEmpty(rawPaths) || ObjectUtils.isEmpty(thumbnailFiles)) {
            return new ArrayList<>();
        } else if (rawPaths.size() != thumbnailFiles.size()) {
            return new ArrayList<>();
        } else {
            List<PhotoPickerEntity> entities = new ArrayList<>();
            for (int i = 0; i < rawPaths.size(); i++) {
                entities.add(new PhotoPickerEntity(PhotoPickerEntity.TYPE_FILE, rawPaths.get(i), thumbnailFiles.get(i).getPath()));
            }
            return entities;
        }
    }

}
