package com.nervestaple.gtdinbox.gui.event;

import java.util.List;

/**
 * Provides an object that indicates data has been updated in some way.
 */
public class DataEvent<T> implements ApplicationEvent {

    /**
     * Collection of entities subject to this event.
     */
    List<T> entities;

    public DataEvent(List<T> entities) {
        this.entities = entities;
    }

    public enum Type {
        REPLACE;
    }
}
