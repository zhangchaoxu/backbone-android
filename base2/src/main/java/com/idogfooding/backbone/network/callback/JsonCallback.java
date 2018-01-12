package com.idogfooding.backbone.network.callback;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.idogfooding.backbone.network.ApiException;
import com.idogfooding.backbone.network.HttpResult;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * 默认将返回的数据解析成需要的HttpResult
 */
public abstract class JsonCallback<T> extends BaseJsonCallback<T> {

    public JsonCallback() {
    }

    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {
        ResponseBody body = response.body();
        if (null == body)
            return null;

        // Gson解析接口返回数据流
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        // genType是Callback类,Callback必须带泛型,肯定是ParameterizedType
        Type genType = getClass().getGenericSuperclass();
        // type是Callback的泛型类,比如HttpResult,HttpResult也可能还带有第二层泛型
        Type type = ((ParameterizedType) genType).getActualTypeArguments()[0];
        // Callback的泛型类的真实类型
        Type rawType;
        // 第二层数据的泛型真实类型
        Type typeArgument = Void.class;
        if (type instanceof ParameterizedType) {
            // Callback泛型(比如HttpResult),带有第二层泛型
            // Callback泛型数据的真实类型
            // 要获取这个rawType是因为typ带泛型以后,无法直接通过instanceof或者==和HttpResult类做判断
            rawType = ((ParameterizedType) type).getRawType();
            typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            // Callback泛型(比如HttpResult),没有再继续指定泛型
            rawType = type;
        }

        if (rawType == HttpResult.class) {
            // Callback泛型是HttpResult,直接解析成HttpResult
            HttpResult httpResult = gson.fromJson(jsonReader, Void.class == typeArgument ? HttpResult.class : type);
            if (httpResult.isSuccess()) {
                return (T) httpResult;
            } else {
                throw new ApiException(httpResult.getCode(), httpResult.getMsg());
            }
        } else {
            // Callback泛型非HttpResult
            T data = gson.fromJson(jsonReader, type);
            response.close();
            return data;
        }
    }

    @Override
    public void onSuccess(Response<T> response) {
        if (response == null || response.body() == null) {
            onError(response);
        }
    }

}
