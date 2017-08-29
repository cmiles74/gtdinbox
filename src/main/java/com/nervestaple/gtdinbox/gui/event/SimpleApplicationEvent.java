package com.nervestaple.gtdinbox.gui.event;

/**
 *
 */
public class SimpleApplicationEvent<T> implements ApplicationEvent {

    private T instance;

    public SimpleApplicationEvent(T instance) {
        this.instance = instance;
    }

    public T getInstance() {
        return instance;
    }
}