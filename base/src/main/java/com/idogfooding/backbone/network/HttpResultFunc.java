package com.idogfooding.backbone.network;

import com.google.common.base.Optional;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 * see {http://gank.io/post/560e15be2dca930e00da1083}
 *
 * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
 */
public class HttpResultFunc<T> implements Function<HttpResult<T>, T> {

    @Override
    public T apply(@NonNull HttpResult<T> httpResult) throws Exception {
        if (httpResult.isSuccess()) {
            return httpResult.getData();
        } else {
            throw new ApiException(httpResult.getCode(), httpResult.getMsg(), httpResult.getData());
        }
    }
}
