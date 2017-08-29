package com.nervestaple.gtdinbox.gui.event.action;

import com.nervestaple.gtdinbox.gui.event.SimpleApplicationEvent;

/**
 * Provides an action event that includes a message.
 */
public class MessageActionEvent<T> extends SimpleApplicationEvent<T> {

    /**
     * The action for this event.
     */
    private MessageAction action;

    /**
     * Optional message for this event.
     */
    private String message;

    /**
     * Creates a new event.
     *
     * @param instance Object instance subject to this event
     * @param action The action for this event.
     */
    public MessageActionEvent(T instance, MessageAction action) {
        this(instance, action, null);
    }

    /**
     * Creates a new event.
     *
     * @param instance Object instance subject to this event
     * @param action The action for this event
     * @param message Optional message for this event
     */
    public MessageActionEvent(T instance, MessageAction action, String message) {
        super(instance);
        this.action = action;
        this.message = message;
    }

    /**
     * Returns the action for this event.
     * @return action
     */
    public MessageAction getAction() {
        return action;
    }

    /**
     * Returns the message for this event.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
