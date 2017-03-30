package com.idogfooding.backbone.event;

import org.greenrobot.eventbus.EventBus;

/**
 * BaseEvent for event bus
 *
 * @author Charles
 */
public class BaseEvent {

    /**
     * @param targetClass specify target class that will receive this event
     */
    public final void post(Class... targetClass) {
        EventBus.getDefault().post(this);
    }

    /**
     * @param targetClass specify target class that will receive this event
     */
    public final void postSticky(Class... targetClass) {
        EventBus.getDefault().postSticky(this);
    }

}
