package com.nervestaple.gtdinbox.gui.event.action;

import com.nervestaple.gtdinbox.gui.event.SimpleApplicationEvent;

/**
 *
 */
public class MessageActionEvent<T> extends SimpleApplicationEvent<T> {

    private ApplicationAction action;

    private String message;

    public MessageActionEvent(T instance, ApplicationAction action) {
        this(instance, action, null);
    }

    public MessageActionEvent(T instance, ApplicationAction action, String message) {
        super(instance);
        this.action = action;
        this.message = message;
    }

    public ApplicationAction getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
