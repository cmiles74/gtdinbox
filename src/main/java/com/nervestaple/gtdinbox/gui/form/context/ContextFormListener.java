package com.nervestaple.gtdinbox.gui.form.context;

import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.GTDInboxException;

/**
 * Provides an interface that objects may implement if they want to handle form events.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface ContextFormListener {

    /**
     * Called when a new inboxContext is added.
     *
     * @param inboxContext InboxContext that was added
     */
    public void inboxContextAdded(InboxContext inboxContext);

    /**
     * Called when a new inboxContext is updated.
     *
     * @param inboxContext InboxContext that was updated
     */
    public void inboxContextUpdated(InboxContext inboxContext);

    /**
     * Called when an error occurs.
     *
     * @param exception The exception that occurred
     */
    public void exceptionOccurred(GTDInboxException exception);
}
