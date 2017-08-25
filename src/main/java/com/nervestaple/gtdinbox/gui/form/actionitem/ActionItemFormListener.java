package com.nervestaple.gtdinbox.gui.form.actionitem;

import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;

/**
 * Provides an interface objects can implement if they want to be notified of ActionItemForm events.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface ActionItemFormListener {

    /**
     * Called when an ActionItem is added.
     *
     * @param actionItem ActionItem added
     */
    public void actionItemAdded(ActionItem actionItem);

    /**
     * Called when an ActionItem is updated.
     *
     * @param actionItem ActionItem updated
     */
    public void actionItemUpdated(ActionItem actionItem);

    /**
     * Called when an error occurs.
     *
     * @param exception The exception that occurred
     */
    public void exceptionOccurred(GTDInboxException exception);
}
