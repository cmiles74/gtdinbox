package com.nervestaple.gtdinbox.gui.browser.renderer;

import com.nervestaple.gtdinbox.gui.browser.TopLevelContainer;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a renderer for our top-level containers (Project, Category, etc.)
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TopLevelContainerRenderer {

    /**
     * ImageIcon for projects.
     */
    private ImageIcon imageIconProjects;

    /**
     * IamgeIcon for contexts.
     */
    private ImageIcon imageIconContexts;

    /**
     * ImageIcon for categories.
     */
    private ImageIcon imageIconCategories;

    /**
     * ImageIcon for trash.
     */
    private ImageIcon imageIconTrash;

    /**
     * ImageIcon for archive.
     */
    private ImageIcon imageIconArchive;

    /**
     * Font for the text.
     */
    private Font font;

    /**
     * Font size for the label.
     */
    private final static int FONT_SIZE = 11;

    public TopLevelContainerRenderer() {

        // load in the images
        imageIconProjects = new ImageIcon(getClass().getResource("images/projects-18.png"));
        imageIconContexts = new ImageIcon(getClass().getResource("images/contexts-18.png"));
        imageIconCategories = new ImageIcon(getClass().getResource("images/categories-18.png"));
        imageIconTrash = new ImageIcon(getClass().getResource("images/trash-18.png"));
        imageIconArchive = new ImageIcon(getClass().getResource("images/archive-18.png"));

        font = UIManager.getFont("Label.font").deriveFont(Font.PLAIN, FONT_SIZE);
    }

    /**
     * Returns the rendered component.
     *
     * @param jTree            Tree the component is a part of
     * @param container        The top level container to render
     * @param isSelected       True if the object is selected
     * @param isExpanded       True if the object is expanded
     * @param isLeaf           True if the object has no children
     * @param row              The object's row index in the tree
     * @param hasFocus         True if the object has focus
     * @param defaultComponent The default rendering of the component
     * @return The rendered component
     */
    public JLabel getTreeCellRendererComponent(JTree jTree, TopLevelContainer container, boolean isSelected,
                                               boolean isExpanded, boolean isLeaf, int row, boolean hasFocus, JLabel defaultComponent) {

        defaultComponent.setText(container.getName());
        defaultComponent.setFont(font);

        if (container == TopLevelContainer.PROJECTS) {
            defaultComponent.setIcon(imageIconProjects);
        } else if (container == TopLevelContainer.CONTEXTS) {
            defaultComponent.setIcon(imageIconContexts);
        } else if (container == TopLevelContainer.CATEGORIES) {
            defaultComponent.setIcon(imageIconCategories);
        } else if (container == TopLevelContainer.TRASH) {
            defaultComponent.setIcon(imageIconTrash);
        } else if (container == TopLevelContainer.ARCHIVE) {
            defaultComponent.setIcon(imageIconArchive);
        }

        return (defaultComponent);
    }
}
