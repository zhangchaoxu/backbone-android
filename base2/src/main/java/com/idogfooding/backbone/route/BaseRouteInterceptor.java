package com.idogfooding.backbone.route;

import android.support.annotation.NonNull;

import com.chenenyu.router.IRouter;
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
        return chain.process();
    }

    /**
     * 跳转
     * @param iRouter
     * @param chain
     */
    protected void routerGo(IRouter iRouter, Chain chain) {
        if (chain.getFragmentV4() != null) {
            iRouter.go(chain.getFragmentV4());
        } else if (chain.getFragment() != null) {
            iRouter.go(chain.getFragment());
        } else {
            iRouter.go(chain.getContext());
        }
    }
}
