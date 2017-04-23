package com.idogfooding.backbone.network;

import com.google.common.base.Optional;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * trans http result
 * see {http://gank.io/post/560e15be2dca930e00da1083}
 *
 * @param <T> useful data
 **/
public class HttpResultFunc<T> implements Function<HttpResult<T>, Optional<T>> {

    @Override
    public Optional<T> apply(@NonNull HttpResult<T> httpResult) throws Exception {
        if (null != httpResult && httpResult.isSuccess()) {
            return Optional.fromNullable(httpResult.getData());
        } else {
            throw new ApiException(httpResult.getCode(), httpResult.getMsg(), httpResult.getData());
        }
    }
}
