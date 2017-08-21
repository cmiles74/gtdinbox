package com.nervestaple.gtdinbox.gui.browser.renderer;

import com.nervestaple.utility.Platform;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Provides the default render for the browser tree. All cells are rendered by the default renderer, then passed on to
 * the more specific renderers as needed.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class DefaultRenderer {

    /**
     * The default tree cell renderer.
     */
    private TreeCellRenderer defaultTreeCellRenderer;

    /**
     * Font for the label.
     */
    private Font font;

    /**
     * Font size for the label.
     */
    private final static int FONT_SIZE = 11;

    /**
     * Flag if we aren't on Macintosh.
     */
    private static boolean isMacintosh = false;

    static {

        isMacintosh = Platform.checkMacintosh();
    }

    /**
     * Creates a new DefaultRenderer.
     */
    public DefaultRenderer() {

        super();

        defaultTreeCellRenderer = new DefaultTreeCellRenderer();

        font = UIManager.getFont("Label.font").deriveFont(Font.PLAIN, FONT_SIZE);
    }

    /**
     * Returns the rendered component.
     *
     * @param jTree      Tree the component is a part of
     * @param object     The object to render
     * @param isSelected True if the object is selected
     * @param isExpanded True if the object is expanded
     * @param isLeaf     True if the object has no children
     * @param row        The object's row index in the tree
     * @param hasFocus   True if the object has focus
     * @return The rendered component
     */
    public JLabel getTreeCellRendererComponent(JTree jTree, Object object, boolean isSelected, boolean isExpanded,
                                               boolean isLeaf, int row, boolean hasFocus) {

        JLabel label = (JLabel) defaultTreeCellRenderer.getTreeCellRendererComponent(jTree, object, isSelected,
                isExpanded, isLeaf, row, hasFocus);

        label.setFont(font);

        if (!isMacintosh) {

            label.setBackground(jTree.getBackground());
            label.setOpaque(true);

            if (isSelected) {

                label.setForeground(Color.DARK_GRAY);
                label.setBackground(Color.WHITE);
            }
        }

        return (label);
    }
}
