package com.idogfooding.backbone.route;

import android.support.annotation.NonNull;

import com.chenenyu.router.RouteInterceptor;
import com.chenenyu.router.RouteResponse;

/**
 * base router interceptor
 *
 * @author Charles
 */
public class BaseRouteInterceptor implements RouteInterceptor {

    @NonNull
    @Override
    public RouteResponse intercept(Chain chain) {
        return null;
    }
}
