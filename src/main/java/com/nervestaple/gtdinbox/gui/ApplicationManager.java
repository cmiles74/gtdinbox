package com.nervestaple.gtdinbox.gui;

import com.google.common.eventbus.EventBus;

/**
 * Provides a singleton for managing application level data.
 */
public class ApplicationManager {

    private static ApplicationManager applicationManager;

    private EventBus eventBus;

    static {

        applicationManager = new ApplicationManager();
    }

    private ApplicationManager() {
        this.eventBus = new EventBus();
    }

    public static ApplicationManager getInstance() {
        return applicationManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
