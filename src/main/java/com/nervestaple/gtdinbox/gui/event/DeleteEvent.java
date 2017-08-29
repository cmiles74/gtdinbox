package com.nervestaple.gtdinbox.gui.event;

/**
 * Provides an object that indicates a persistent object has been removed.
 */
public class DeleteEvent<T> extends ItemEvent {

    public DeleteEvent(Object entity) {
        super(entity);
    }
}
