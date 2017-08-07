package com.idogfooding.backbone.network;

import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * trans http result
 *
 * @param <T> paged data
 **/
public class OptionalListToPagedResultFunc<T> implements Function<Optional<List<T>>, BasePagedResult<T>> {

    @Override
    public BasePagedResult<T> apply(@NonNull Optional<List<T>> optional) throws Exception {
        if (optional.isPresent()) {
            return new BasePagedResult<>(optional.get().size(), false, optional.get());
        } else {
            throw new ApiException(601, "数据格式错误");
        }
    }
}
