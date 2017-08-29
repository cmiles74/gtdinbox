package com.nervestaple.gtdinbox.gui.event;

/**
 *
 */
public class ChangedEvent<T> extends ApplicationActionEvent {

    public ChangedEvent(T instance) {
        super(instance);
    }
}
