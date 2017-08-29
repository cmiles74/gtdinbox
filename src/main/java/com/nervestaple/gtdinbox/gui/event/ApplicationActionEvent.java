package com.nervestaple.gtdinbox.gui.event;

/**
 *
 */
public class ApplicationActionEvent<T> {

    private T instance;

    public ApplicationActionEvent(T instance) {
        this.instance = instance;
    }

    public T ApplicationActionEvent() {
        return instance;
    }
}