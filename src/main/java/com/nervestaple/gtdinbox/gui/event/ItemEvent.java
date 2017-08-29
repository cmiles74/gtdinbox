package com.nervestaple.gtdinbox.gui.event;

/**
 * Provides an object that indicates an application-wide event has occurred as well as
 * data about that event.
 */
public class ItemEvent<T> implements ApplicationEvent {

    /**
     * The data object for this event.
     */
    private T entity;

    /**
     * Creates a new event.
     *
     * @param entity data object for this event
     */
    public ItemEvent(T entity) {
        this.entity = entity;
    }

    /**
     * Returns the data object for this event.
     *
     * @return data object for this event.
     */
    public T getEntity() {
        return entity;
    }
}