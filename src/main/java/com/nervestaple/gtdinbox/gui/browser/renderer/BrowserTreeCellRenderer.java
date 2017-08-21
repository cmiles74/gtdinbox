package com.nervestaple.gtdinbox.gui.browser.renderer;

import com.nervestaple.gtdinbox.gui.browser.TopLevelContainer;
import com.nervestaple.gtdinbox.gui.utility.glazedtreemodel.GlazedTreeNode;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Provides a custom renderer for the browser's tree of data.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class BrowserTreeCellRenderer implements TreeCellRenderer {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Default renderer for the components.
     */
    private DefaultRenderer defaultRenderer;

    /**
     * Top level container renderer.
     */
    private TopLevelContainerRenderer topLevelContainerRenderer;

    /**
     * Renderer for Project objects.
     */
    private ProjectRenderer projectRenderer;

    /**
     * Renderer for Context objects.
     */
    private ContextRenderer contextRenderer;

    /**
     * Renderer for Category objecs.
     */
    private CategoryRenderer categoryRenderer;

    /**
     * Creates a new BrowserTreeCellRenderer.
     */
    public BrowserTreeCellRenderer() {

        super();

        // get a default tree cell renderer.
        defaultRenderer = new DefaultRenderer();

        // get our other renderers
        topLevelContainerRenderer = new TopLevelContainerRenderer();
        projectRenderer = new ProjectRenderer();
        contextRenderer = new ContextRenderer();
        categoryRenderer = new CategoryRenderer();
    }

    /**
     * Returns a rendered tree node.
     *
     * @param jTree
     * @param object
     * @param isSelected
     * @param isExpanded
     * @param isLeaf
     * @param row
     * @param hasFocus
     * @return
     */
    public Component getTreeCellRendererComponent(JTree jTree, Object object, boolean isSelected, boolean isExpanded,
                                                  boolean isLeaf, int row, boolean hasFocus) {

        JLabel label = defaultRenderer.getTreeCellRendererComponent(jTree, object, isSelected, isExpanded,
                isLeaf, row, hasFocus);

        if (object instanceof GlazedTreeNode) {

            // get the node
            GlazedTreeNode treeNode = (GlazedTreeNode) object;

            // make sure we have a user object
            if (treeNode.getUserObject() != null) {

                if (treeNode.getUserObject() instanceof TopLevelContainer) {

                    topLevelContainerRenderer.getTreeCellRendererComponent(jTree,
                            (TopLevelContainer) treeNode.getUserObject(), isSelected, isExpanded, isLeaf, row,
                            hasFocus, label);
                } else if (treeNode.getUserObject() instanceof Project) {

                    projectRenderer.getTreeCellRendererComponent(jTree, (Project) treeNode.getUserObject(),
                            isSelected, isExpanded, isLeaf, row, hasFocus, label);
                } else if (treeNode.getUserObject() instanceof InboxContext) {

                    contextRenderer.getTreeCellRendererComponent(jTree, (InboxContext) treeNode.getUserObject(),
                            isSelected, isExpanded, isLeaf, row, hasFocus, label);
                } else if (treeNode.getUserObject() instanceof Category) {

                    categoryRenderer.getTreeCellRendererComponent(jTree, (Category) treeNode.getUserObject(),
                            isSelected, isExpanded, isLeaf, row, hasFocus, label);
                } else {

                    // set the label to the text value of the user object
                    label.setText(treeNode.getUserObject().toString());
                }
            }
        }

        // user the default renderer
        return (label);
    }
}
