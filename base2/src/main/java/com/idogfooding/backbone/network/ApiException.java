package com.idogfooding.backbone.network;

/**
 * ApiException
 * 接口返回的异常
 *
 * @author Charles
 */
public class ApiException extends BoneException {

    public ApiException(int code, String msg) {
        super(code, msg);
    }

    public boolean isUnauthorized() {
        return getCode() == CODE_API_UNAUTH;
    }

}
