package com.nervestaple.gtdinbox.gui.event;

/**
 *
 */
public class MessageActionEvent<T> extends ApplicationActionEvent {

    private String action;

    private String message;

    public MessageActionEvent(T instance, String action, String message) {
        super(instance);
        this.action = action;
        this.message = message;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }
}
