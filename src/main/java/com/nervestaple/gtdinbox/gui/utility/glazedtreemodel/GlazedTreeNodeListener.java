package com.nervestaple.gtdinbox.gui.utility.glazedtreemodel;

/**
 * Provides an interface an object can implement if it wants to listen for GlazedTreeNodeEvents.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface GlazedTreeNodeListener {

    public void handleGlazedTreeModelEvent( GlazedTreeModelEvent event );
}
