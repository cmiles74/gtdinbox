package com.nervestaple.gtdinbox.gui.event.item;

/**
 *
 */
public class ChangedEvent<T> extends ItemEvent {

    public ChangedEvent(T instance) {
        super(instance);
    }
}
