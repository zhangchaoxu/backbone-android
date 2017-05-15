package com.idogfooding.backbone.network;

import com.google.common.base.Optional;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * trans http result
 *
 * @param <T> paged data
 **/
public class DataResultFunc<T> implements Function<Optional<T>, T> {

    @Override
    public T apply(@NonNull Optional<T> optional) throws Exception {
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new ApiException(201, "数据为空");
        }
    }
}
