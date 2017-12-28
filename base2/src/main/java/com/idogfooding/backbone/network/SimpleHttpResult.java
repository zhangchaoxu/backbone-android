package com.idogfooding.backbone.network;

/**
 * SimpleHttpResult
 *
 * @author Charles
 */
public class SimpleHttpResult extends BaseEntity {

    private int code;
    private String msg;

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

    public HttpResult toHttpResult() {
        HttpResult result = new HttpResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
