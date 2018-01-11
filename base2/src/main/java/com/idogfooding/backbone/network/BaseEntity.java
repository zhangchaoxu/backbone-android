package com.idogfooding.backbone.network;

import com.idogfooding.backbone.utils.GsonUtils;

import java.io.Serializable;

/**
 * BaseEntity
 *
 * @author Charles
 */
public class BaseEntity implements Serializable {

    public String toJson() {
        return GsonUtils.toJson(this);
    }

    private transient boolean checked; // 是否选中

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
