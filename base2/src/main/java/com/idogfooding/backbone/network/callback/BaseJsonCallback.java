package com.idogfooding.backbone.network.callback;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.idogfooding.backbone.network.ApiException;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;

/**
 * 默认将返回的数据解析成需要的Bean
 * 可以是 BaseBean，String，List，Map
 */
public abstract class BaseJsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public BaseJsonCallback() {
    }

    public BaseJsonCallback(Type type) {
        this.type = type;
    }

    public BaseJsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     */
    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (null == body)
            return null;

        JsonReader jsonReader = new JsonReader(body.charStream());
        if (null != type) {
            return new Gson().fromJson(jsonReader, type);
        } else if (null != clazz) {
            return new Gson().fromJson(jsonReader, clazz);
        } else {
            Type genType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            response.close();
            return new Gson().fromJson(jsonReader, type);
        }
    }

    /**
     * 网络请求发生错误,包括系统错误、接口返回错误等
     * @param response
     */
    @Override
    public void onError(Response<T> response) {
        Throwable exception = response.getException();

        if (null != exception)
            exception.printStackTrace();

        if (null == exception) {
            response.setException(new IllegalStateException("网络请求发生未知错误"));
            onSysError(response);
        } else if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            response.setException(new IllegalStateException("网络连接失败,请检查网络链接!"));
            onSysError(response);
        } else if (exception instanceof SocketTimeoutException) {
            response.setException(new IllegalStateException("网络请求超时"));
            onSysError(response);
        } else if (exception instanceof HttpException) {
            response.setException(new IllegalStateException("服务器返回异常:" + exception.getMessage()));
            onSysError(response);
        } else if (exception instanceof StorageException) {
            response.setException(new IllegalStateException("存储读取异常,请检查存储是否存在并有权限"));
            onSysError(response);
        } else if (exception instanceof ApiException) {
            onApiError(response, (ApiException) exception);
        } else {
            response.setException(new IllegalStateException("网络请求未知错误"));
            onSysError(response);
        }
    }

    /**
     * 除服务器返回错误以外的系统错误
     * @param response
     */
    protected void onSysError(Response<T> response) {
        ToastUtils.showShort(response.getException().getMessage());
    }

    /**
     * 服务器返回错误
     * @param response
     * @param exception
     */
    protected void onApiError(Response<T> response, ApiException exception) {
        ToastUtils.showShort(exception.getMessage());
    }

}