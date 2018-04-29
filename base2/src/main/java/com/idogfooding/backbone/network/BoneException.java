package com.idogfooding.backbone.network;

import android.text.TextUtils;

/**
 * BoneException
 * 自定义异常
 *
 * @author Charles
 */
public class BoneException extends RuntimeException {

    public static final int CODE_SUCCESS = 0;
    public static final String MSG_SUCCESS = "SUCCESS";

    public static final int CODE_API_UNAUTH = 401;
    public static final String MSG_API_UNAUTH = "接口未授权";

    public static final int CODE_NULL_EXCEPTION = 300001;
    public static final String MSG_NULL_EXCEPTION = "网络请求无详细异常";

    public static final int CODE_JSON_EXCEPTION = 300002;
    public static final String MSG_JSON_EXCEPTION = "网络数据解析错误";

    public static final int CODE_NO_RESPONSE_EXCEPTION = 300003;
    public static final String MSG_NO_RESPONSE_EXCEPTION = "网络请求无响应";

    public static final int CODE_NO_BODY_EXCEPTION = 300004;
    public static final String MSG_NO_BODY_EXCEPTION = "网络请求返回数据为空";

    public static final int CODE_UNKNOWNHOST_EXCEPTION = 300005;
    public static final String MSG_UNKNOWNHOST_EXCEPTION = "网络请求目标服务器无法解析";

    public static final int CODE_CONNECT_EXCEPTION = 300006;
    public static final String MSG_CONNECT_EXCEPTION = "网络请求服务器连接异常";

    public static final int CODE_IO_EXCEPTION = 300007;
    public static final String MSG_IO_EXCEPTION = "IO异常";

    public static final int CODE_SOCKETTIMEOUT_EXCEPTION = 300008;
    public static final String MSG_SOCKETTIMEOUT_EXCEPTION = "网络请求连接超时";

    public static final int CODE_HTTP_EXCEPTION = 300009;
    public static final String MSG_HTTP_EXCEPTION = "网络请求返回信息异常";

    public static final int CODE_STORAGE_EXCEPTION = 300010;
    public static final String MSG_STORAGE_EXCEPTION = "存储读取异常,请检查存储是否存在并有权限";

    public static final int CODE_API_EXCEPTION = 300011;
    public static final String MSG_API_EXCEPTION = "网络请求接口返回异常";

    public static final int CODE_OTHER_EXCEPTION = 300012;
    public static final String MSG_OTHER_EXCEPTION = "其他未知异常";

    public static String getMsgByCode(int errorCode) {
        switch (errorCode) {
            case CODE_SUCCESS:
                return MSG_SUCCESS;
            case CODE_API_UNAUTH:
                return MSG_API_UNAUTH;
            case CODE_NULL_EXCEPTION:
                return MSG_NULL_EXCEPTION;
            case CODE_JSON_EXCEPTION:
                return MSG_JSON_EXCEPTION;
            case CODE_NO_RESPONSE_EXCEPTION:
                return MSG_NO_RESPONSE_EXCEPTION;
            case CODE_NO_BODY_EXCEPTION:
                return MSG_NO_BODY_EXCEPTION;
            case CODE_UNKNOWNHOST_EXCEPTION:
                return MSG_UNKNOWNHOST_EXCEPTION;
            case CODE_CONNECT_EXCEPTION:
                return MSG_CONNECT_EXCEPTION;
            case CODE_IO_EXCEPTION:
                return MSG_IO_EXCEPTION;
            case CODE_SOCKETTIMEOUT_EXCEPTION:
                return MSG_SOCKETTIMEOUT_EXCEPTION;
            case CODE_HTTP_EXCEPTION:
                return MSG_HTTP_EXCEPTION;
            case CODE_STORAGE_EXCEPTION:
                return MSG_STORAGE_EXCEPTION;
            case CODE_API_EXCEPTION:
                return MSG_API_EXCEPTION;
            case CODE_OTHER_EXCEPTION:
                return MSG_OTHER_EXCEPTION;
            default:
                return MSG_OTHER_EXCEPTION;
        }
    }

    private int code;
    private String msg;

    public BoneException(int code) {
        super(getMsgByCode(code));
        this.code = code;
        this.msg = getMessage();
    }

    public BoneException(int code, String msg) {
        super(TextUtils.isEmpty(msg) ? MSG_OTHER_EXCEPTION : msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
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

}
