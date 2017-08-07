package com.idogfooding.backbone.network;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * trans http result
 *
 * @param <T> paged data
 **/
public class ListToPagedResultFunc<T> implements Function<List<T>, BasePagedResult<T>> {

    @Override
    public BasePagedResult<T> apply(@NonNull List<T> list) throws Exception {
        return new BasePagedResult<>(list.size(), false, list);
    }
}
