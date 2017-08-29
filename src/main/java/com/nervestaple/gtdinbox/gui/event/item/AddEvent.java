package com.nervestaple.gtdinbox.gui.event.item;

/**
 * Provides an Event class that indicates a new object has been persisted.
 */
public class AddEvent<T> extends ItemEvent {

    public AddEvent(Object entity) {
        super(entity);
    }
}
