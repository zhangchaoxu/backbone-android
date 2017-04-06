package com.idogfooding.backbone.network;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * BaseEntity
 *
 * @author Charles
 */
public class BaseEntity implements Serializable {

    public String getJson() {
        return new Gson().toJson(this);
    }
}
