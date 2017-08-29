package com.nervestaple.gtdinbox.gui.event.action;

import com.nervestaple.gtdinbox.gui.event.ApplicationEvent;

import java.util.List;

/**
 * Provides an object that indicates data has been updated in some way.
 */
public class DataEvent<T> implements ApplicationEvent {

    /**
     * Collection of entities subject to this event.
     */
    List<T> entities;

    private DataAction action;

    private String message;

    public DataEvent(List<T> entities, DataAction action, String message) {
        this.entities = entities;
        this.action = action;
        this.message = message;
    }

    public List<T> getEntities() {
        return entities;
    }

    public DataAction getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
