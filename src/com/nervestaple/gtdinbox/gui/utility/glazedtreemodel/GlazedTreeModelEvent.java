package com.nervestaple.gtdinbox.gui.utility.glazedtreemodel;

import ca.odell.glazedlists.event.ListEvent;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Provides an event for the GlazedTreeModel, it's used to notify the tree model of changes to the nodes.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class GlazedTreeModelEvent {

    /** The source list event. */
    private ListEvent listEvent;

    /** The path to the target node. */
    private List listPath;

    /**
     * Creates a new GlazedTreeModelEvent.
     * @param event ListEvent
     */
    public GlazedTreeModelEvent( ListEvent event ) {

        listPath = new ArrayList();

        listEvent = event;
    }

    /**
     * Adds the provided node to the path. This path is used by the TreeModel to locate the changed node.
     * @param node Node to add to the path
     */
    public void addNodeToPath( GlazedTreeNode node ) {

        listPath.add( node );
    }

    /**
     * Returns an array of TreeNodes that are the path to the changed node.
     * @return Array of TreeNode objects
     */
    public TreeNode[] getTreePathNodes() {

        TreeNode[] path = new TreeNode[ listPath.size() ];

        Collections.reverse( listPath );
        Iterator iterator = listPath.iterator();
        int index = 0;
        while( iterator.hasNext() ) {

            TreeNode treeNode = ( TreeNode ) iterator.next();
            path[ index ] = treeNode;
            index++;
        }
        Collections.reverse( listPath );

        return( path );
    }

    /**
     * Returns a TreePath if the TreeNode objects.
     * @return TreePath to the changed node.
     */
    public TreePath getTreePath() {

        return( new TreePath( getTreePathNodes() ) );
    }

    /**
     * ListEvent for this GlazedTreeModelEvent.
     * @return
     */
    public ListEvent getListEvent() {
        return listEvent;
    }
}
