package com.nervestaple.gtdinbox.gui.browser.renderer;

import com.nervestaple.gtdinbox.model.project.Project;

import javax.swing.*;

/**
 * Returns a JLabel that we can use when rendering a Project in the browser tree.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ProjectRenderer {

    /**
     * Image icon for projects.
     */
    private ImageIcon imageIconProject;

    public ProjectRenderer() {

        imageIconProject = new ImageIcon(getClass().getResource("images/project-18.png"));
    }

    /**
     * Returns the rendered component.
     *
     * @param jTree            Tree the component is a part of
     * @param project          The Project to render
     * @param isSelected       True if the object is selected
     * @param isExpanded       True if the object is expanded
     * @param isLeaf           True if the object has no children
     * @param row              The object's row index in the tree
     * @param hasFocus         True if the object has focus
     * @param defaultComponent The default rendering of the component
     * @return The rendered component
     */
    public JLabel getTreeCellRendererComponent(JTree jTree, Project project, boolean isSelected, boolean isExpanded,
                                               boolean isLeaf, int row, boolean hasFocus, JLabel defaultComponent) {

        defaultComponent.setText(project.getName());
        defaultComponent.setIcon(imageIconProject);

        return (defaultComponent);
    }
}
