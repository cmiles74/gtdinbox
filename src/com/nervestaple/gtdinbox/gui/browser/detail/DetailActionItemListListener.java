package com.nervestaple.gtdinbox.gui.browser.detail;

/**
 * Provides an interface objects can implement if they want to received DetailActionItemList events.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface DetailActionItemListListener {

    public void selectionChanged();

    public void selectedItemDoubleClicked();

    public void componentSizeChanged();
}
