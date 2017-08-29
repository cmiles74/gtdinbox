package com.nervestaple.gtdinbox.gui;

import com.google.common.eventbus.EventBus;
import com.nervestaple.gtdinbox.gui.form.FrameManager;

/**
 * Provides a singleton for managing application level data.
 */
public class ApplicationManager {

    private static ApplicationManager applicationManager;

    private EventBus eventBus;

    private FrameManager frameManager;

    static {

        applicationManager = new ApplicationManager();
    }

    private ApplicationManager() {
        this.eventBus = new EventBus();
        this.frameManager = new FrameManager();
    }

    public static ApplicationManager getInstance() {
        return applicationManager;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public FrameManager getFrameManager() {
        return frameManager;
    }
}
