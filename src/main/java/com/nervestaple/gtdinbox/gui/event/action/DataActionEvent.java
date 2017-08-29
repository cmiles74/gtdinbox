package com.nervestaple.gtdinbox.gui.event.action;

import com.nervestaple.gtdinbox.gui.event.ApplicationEvent;

import java.util.List;

/**
 * Provides an object that indicates data has been updated in some way.
 */
public class DataActionEvent<T> implements ApplicationEvent {

    /**
     * Collection of instances subject to this event.
     */
    List<T> instances;

    /**
     * The action for this event.
     */
    private DataAction action;

    /**
     * An optional descriptive message for this event.
     */
    private String message;

    /**
     * Creates a new event.
     *
     * @param entities List of instances for this event
     * @param action Action for this event
     * @param message Optional message for this event.
     */
    public DataActionEvent(List<T> entities, DataAction action, String message) {
        this.instances = entities;
        this.action = action;
        this.message = message;
    }

    /**
     * Creates a new event.
     *
     * @param entities List of instances for this event
     * @param action Action for this event
     */
    public DataActionEvent(List<T> entities, DataAction action) {
        this(entities, action, null);
    }

    /**
     * Returns the instances for this event.
     *
     * @return List of instances
     */
    public List<T> getInstances() {
        return instances;
    }

    /**
     * Returns the action for this event.
     *
     * @return Action
     */
    public DataAction getAction() {
        return action;
    }

    /**
     * Returns the optional message for this event.
     *
     * @return Message
     */
    public String getMessage() {
        return message;
    }
}
