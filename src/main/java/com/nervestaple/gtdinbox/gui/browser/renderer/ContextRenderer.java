package com.nervestaple.gtdinbox.gui.browser.renderer;

import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;

import javax.swing.*;
import java.awt.*;

/**
 * Returns a JLabel we can use when rendering the Contexts tree.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ContextRenderer {

    /** Image icon for the contexts. */
    private ImageIcon imageIconContext;

    public ContextRenderer() {

        imageIconContext = new ImageIcon( getClass().getResource( "images/context-18.png" ) );
    }

    /**
     * Returns the rendered component.
     *
     * @param jTree            Tree the component is a part of
     * @param context          The Context to render
     * @param isSelected       True if the object is selected
     * @param isExpanded       True if the object is expanded
     * @param isLeaf           True if the object has no children
     * @param row              The object's row index in the tree
     * @param hasFocus         True if the object has focus
     * @param defaultComponent The default rendering of the component
     * @return The rendered component
     */
    public JLabel getTreeCellRendererComponent( JTree jTree, InboxContext context, boolean isSelected, boolean isExpanded,
                                                boolean isLeaf, int row, boolean hasFocus, JLabel defaultComponent ) {

        defaultComponent.setText( context.getName() );
        defaultComponent.setIcon( imageIconContext );

        return ( defaultComponent );
    }
}
