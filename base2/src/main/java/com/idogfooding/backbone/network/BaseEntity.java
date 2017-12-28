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

}
