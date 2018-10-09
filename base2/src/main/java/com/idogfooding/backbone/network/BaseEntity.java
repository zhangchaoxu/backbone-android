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
    private transient boolean _checked;

    public boolean is_checked() {
        return _checked;
    }

    public void set_checked(boolean _checked) {
        this._checked = _checked;
    }
}
