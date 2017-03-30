package com.idogfooding.backbone.event;

/**
 * NetworkEvent
 *
 * @author Charles
 */
public class NetworkEvent extends BaseEvent {

    public String message;
    public boolean available;

    public NetworkEvent(String message) {
        this.message = message;
    }

    public NetworkEvent(boolean available) {
        this.available = available;
    }

}
