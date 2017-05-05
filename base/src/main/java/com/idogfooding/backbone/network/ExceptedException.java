package com.idogfooding.backbone.network;

/**
 * ExceptedException
 * excepted exception, do need to handle
 * some time, wo can no handle properties, just throw an excepted exception.
 * <p>
 * * do not abuse this exception *
 *
 * @author Charles
 */
public class ExceptedException extends RuntimeException {

    public ExceptedException() {
        super("");
    }
}
