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

    // is item checked
    private transient boolean __checked;

    public boolean is__checked() {
        return __checked;
    }

    public void set__checked(boolean __checked) {
        this.__checked = __checked;
    }

    public void change__checked() {
        set__checked(!is__checked());
    }
}
