package com.idogfooding.backbone.network;

/**
 * NetworkApiException
 *
 * @author Charles
 */

public class NetworkApiException extends ApiException {

    public NetworkApiException() {
        super(1001, "网络不可用，请检查网络后");
    }
}
