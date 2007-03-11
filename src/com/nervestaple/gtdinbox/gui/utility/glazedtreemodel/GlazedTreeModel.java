package com.nervestaple.gtdinbox.gui.utility.glazedtreemodel;

import org.apache.log4j.Logger;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.HashSet;

/**
 * Provides a TreeModel backed by GlazedLists objects.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class GlazedTreeModel implements TreeModel {

    /** Logger instance. */
    private Logger logger = Logger.getLogger( this.getClass() );

    /** The root of the tree. */
    private GlazedTreeNode nodeRoot;

    /** List of tree model listeners. */
    private HashSet listeners;

    /**
     * Creates a new GlazedTreeModel.
     */
    public GlazedTreeModel() {

        nodeRoot = new GlazedTreeNode();

        listeners = new HashSet();

        initializeGlazedTreeModel();
    }

    /**
     * Creates new new GlazedTreeModel and sets its root node.
     * @param nodeRoot
     */
    public GlazedTreeModel( GlazedTreeNode nodeRoot ) {

        this.nodeRoot = nodeRoot;

        listeners = new HashSet();

        initializeGlazedTreeModel();
    }

    /**
     * Sends a notification that the model has changed. This method always sends a TreeStructureChanged event that
     * should cause the tree to redraw the affected branch.
     *
     * @param source Node that was changed
     * @param path Array of nodes indicating the path to the node
     * @param childIndices Indices identifying previous child positions
     * @param children Child objects that have been removed
     */
    public void fireTreeStructureChanged( Object source, Object[] path, int[] childIndices, Object[] children ) {

        TreeModelListener[] listenerArray = ( TreeModelListener[] )
                listeners.toArray( new TreeModelListener[listeners.size()] );

        TreeModelEvent event = new TreeModelEvent( source, path, childIndices, children );

        for( int index = 0; index < listenerArray.length ; index++ ) {

            listenerArray[ index ].treeStructureChanged( event );
        }
    }

    // private methods

    /**
     * Sets up the model, initializes listeners.
     */
    private void initializeGlazedTreeModel() {

        nodeRoot.addListener( new GlazedTreeNodeListener() {

            public void handleGlazedTreeModelEvent( GlazedTreeModelEvent event ) {

                fireTreeStructureChanged( event.getListEvent().getSource(), event.getTreePathNodes(), null, null );
            }
        } );
    }

    // tree model methods

    /**
     * Returns the root of the tree.
     * @return Root of the tree
     */
    public Object getRoot() {

        return( nodeRoot );
    }

    /**
     * Returns the child of the parent that is at the provided index.
     * @param parent Parent node
     * @param index Index of the child to retrieve
     * @return Child
     */
    public Object getChild( Object parent, int index ) {

        if( parent instanceof TreeNode ) {

            TreeNode node = ( TreeNode ) parent;

            return( node.getChildAt( index ) );
        }

        return( null );
    }

    /**
     * Returns the number of children for the parent node.
     * @param parent Node in the tree
     * @return Number of children
     */
    public int getChildCount( Object parent ) {

        if( parent instanceof TreeNode ) {

            TreeNode node = ( TreeNode ) parent;

            return ( node.getChildCount() );
        }

        return( 0 );
    }

    /**
     * Returns true if the node is a leaf.
     * @param node Node in the tree
     * @return True if the node is a leaf
     */
    public boolean isLeaf( Object node ) {

        if( node instanceof TreeNode ) {

            TreeNode treeNode = ( TreeNode ) node;

            return ( treeNode.isLeaf() );
        }

        return( false );
    }

    /**
     * Fires off a valueForPathChanged event. This is usually used when the user object for a node is changed.
     * @param treePath Path to the node that has changed
     * @param object The new user object for that node
     */
    public void valueForPathChanged( TreePath treePath, Object object ) {

        TreeModelListener[] listenerArray = ( TreeModelListener[] )
                listeners.toArray( new TreeModelListener[ listeners.size() ] );

        TreeModelEvent event = new TreeModelEvent( object, treePath );

        for( int index = 0; index < listenerArray.length; index++ ) {

            listenerArray[ index ].treeNodesChanged( event );
        }
    }

    /**
     * Returns the index for the child, returns -1 if the provided parent isn't really the parent of the ndoe.
     * @param parent Parent node
     * @param child Child node of the parent node
     * @return Index of the child in the parent member list
     */
    public int getIndexOfChild( Object parent, Object child ) {

        if( parent instanceof TreeNode && child instanceof TreeNode) {

            TreeNode treeNodeParent = ( TreeNode ) parent;
            TreeNode treeNodeChild = ( TreeNode ) child;

            return( treeNodeParent.getIndex( treeNodeChild ) );
        }

        return( -1 );
    }

    /**
     * Adds a TreeModelListener to this model.
     * @param treeModelListener Listener
     */
    public void addTreeModelListener( TreeModelListener treeModelListener ) {

        if( !listeners.contains( treeModelListener ) ) {

            listeners.add( treeModelListener );
        }
    }

    /**
     * Removes a TreeModelListener from this model.
     * @param treeModelListener Listener
     */
    public void removeTreeModelListener( TreeModelListener treeModelListener ) {

        listeners.remove( treeModelListener );
    }
}
