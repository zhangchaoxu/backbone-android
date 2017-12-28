package com.idogfooding.backbone.network.callback;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.idogfooding.backbone.network.ApiException;
import com.idogfooding.backbone.network.HttpResult;
import com.idogfooding.backbone.network.SimpleHttpResult;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * 默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 */
public abstract class JsonCallback<T> extends BaseJsonCallback<T> {

    public JsonCallback() {
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (null == body)
            return null;

        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        Type genType = getClass().getGenericSuperclass();
        // 第二层泛型的的所有类型
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
        if (!(type instanceof ParameterizedType))
            throw new IllegalStateException("未指定泛型参数");

        // 第二层数据的真实类型
        Type rawType = ((ParameterizedType) type).getRawType();
        // 第二层数据的泛型真实类型
        Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

        if (rawType != HttpResult.class) {
            T data = gson.fromJson(jsonReader, type);
            response.close();
            return data;
        } else {
            if (typeArgument == Void.class) {
                // 无数据类型
                SimpleHttpResult simpleHttpResult = gson.fromJson(jsonReader, SimpleHttpResult.class);
                response.close();
                return (T) simpleHttpResult.toHttpResult();
            } else {
                HttpResult httpResult = gson.fromJson(jsonReader, type);
                response.close();
                if (httpResult.isSuccess()) {
                    return (T) httpResult;
                } else {
                    throw new ApiException(httpResult.getCode(), httpResult.getMsg());
                }
            }
        }
    }

    @Override
    public void onSuccess(Response<T> response) {
        if (response == null || response.body() == null) {
            onError(response);
        }
    }

}
