package com.idogfooding.backbone.network;

import java.io.Serializable;

/**
 * HttpResult
 * generic T may be List<Object> Object or Empty
 *
 * @author Charles
 */
public class HttpResult<T> implements Serializable {

    private static final int CODE_SUCCESS = 0;

    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return code == CODE_SUCCESS;
    }

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
