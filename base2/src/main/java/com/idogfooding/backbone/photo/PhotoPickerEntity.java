package com.idogfooding.backbone.photo;

import java.io.Serializable;

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

}
