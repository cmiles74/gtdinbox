package com.nervestaple.gtdinbox.gui.event;

/**
 * Provides a class that indicates a persisted object has been updated.
 */
public class UpdateEvent<T> extends ItemEvent {

    public UpdateEvent(Object entity) {
        super(entity);
    }
}
