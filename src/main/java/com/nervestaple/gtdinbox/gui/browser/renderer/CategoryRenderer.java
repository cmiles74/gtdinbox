package com.nervestaple.gtdinbox.gui.browser.renderer;

import com.nervestaple.gtdinbox.model.reference.category.Category;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a JLabel we can use when rendering the Categories tree.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class CategoryRenderer {

    /** Image icon for the categories. */
    private ImageIcon imageIconCategory;

    public CategoryRenderer() {

        imageIconCategory = new ImageIcon( getClass().getResource( "images/category-18.png" ) );
    }

    /**
     * Returns the rendered component.
     *
     * @param jTree            Tree the component is a part of
     * @param category         The Category to render
     * @param isSelected       True if the object is selected
     * @param isExpanded       True if the object is expanded
     * @param isLeaf           True if the object has no children
     * @param row              The object's row index in the tree
     * @param hasFocus         True if the object has focus
     * @param defaultComponent The default rendering of the component
     * @return The rendered component
     */
    public JLabel getTreeCellRendererComponent( JTree jTree, Category category, boolean isSelected, boolean isExpanded,
                                                boolean isLeaf, int row, boolean hasFocus, JLabel defaultComponent ) {

        defaultComponent.setText( category.getName() );
        defaultComponent.setIcon( imageIconCategory );

        return ( defaultComponent );
    }
}
