package com.idogfooding.backbone.route;

import android.app.Fragment;
import android.content.Context;

import com.chenenyu.router.RouteInterceptor;
import com.chenenyu.router.RouteRequest;

/**
 * base router interceptor
 *
 * @author Charles
 */
public class BaseRouteInterceptor implements RouteInterceptor {

    @Override
    public boolean intercept(Object source, RouteRequest routeRequest) {
        return false;
    }

    protected Context getContextFromObject(Object source) {
        if (source instanceof Fragment) {
            return ((Fragment) source).getActivity();
        } else if (source instanceof android.support.v4.app.Fragment) {
            return ((android.support.v4.app.Fragment) source).getContext();
        } else if (source instanceof Context) {
            return (Context) source;
        } else {
            throw new IllegalStateException("未定义的source类型");
        }
    }

    protected void routerGo() {

    }
}
