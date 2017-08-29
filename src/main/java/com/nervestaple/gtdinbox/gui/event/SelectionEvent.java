package com.nervestaple.gtdinbox.gui.event;

/**
 *
 */
public class SelectionEvent<T> extends ApplicationActionEvent {

    public SelectionEvent(T instance) {
        super(instance);
    }
}
