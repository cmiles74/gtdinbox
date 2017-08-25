package com.nervestaple.gtdinbox.gui;

import com.nervestaple.gtdinbox.GTDInboxException;

/**
 * Provides an interface objects can implement if they are interested in catching exception from child components.
 */
public interface GTDInboxExceptionHandler {

    /**
     * Called when an exception from a child component occurs.
     *
     * @param exception Exception that occurred
     */
    public void handleException(GTDInboxException exception);
}
