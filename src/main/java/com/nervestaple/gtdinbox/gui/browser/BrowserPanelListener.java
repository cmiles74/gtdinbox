package com.nervestaple.gtdinbox.gui.browser;

/**
 * Provides an interface that objects can implement if they want to monitor BrowserPanel events.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface BrowserPanelListener {

    public void detailPanelChanged();

    public void selectedDetailItemChanged();

    public void confirmEmptyTrash(String message);
}
