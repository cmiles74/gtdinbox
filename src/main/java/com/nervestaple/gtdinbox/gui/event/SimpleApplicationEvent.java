package com.nervestaple.gtdinbox.gui.event;

/**
 * Provides an application event.
 */
public class SimpleApplicationEvent<T> implements ApplicationEvent {

    /**
     * The instance subject to this event.
     */
    private T instance;

    /**
     * Creates a new event.
     *
     * @param instance Object instance subject to this event.
     */
    public SimpleApplicationEvent(T instance) {
        this.instance = instance;
    }

    /**
     * Returns the object instance subject to this event.
     *
     * @return instance
     */
    public T getInstance() {
        return instance;
    }
}