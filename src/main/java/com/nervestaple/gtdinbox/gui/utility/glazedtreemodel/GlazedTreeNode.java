package com.nervestaple.gtdinbox.gui.utility.glazedtreemodel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import org.apache.commons.collections.iterators.IteratorEnumeration;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.HashSet;

/**
 * Provides a node for the GlazedTreeModel.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class GlazedTreeNode implements MutableTreeNode {

    /**
     * The user object for this node.
     */
    private Object userObject;

    /**
     * Flag to indicate if this node can have children.
     */
    private boolean allowsChildren;

    /**
     * List of children.
     */
    private EventList listChildren;

    /**
     * Parent node.
     */
    private MutableTreeNode nodeParent;

    /**
     * List of listeners.
     */
    private HashSet listeners;

    /**
     * Creates a new GlazedTreeNode.
     */
    public GlazedTreeNode() {

        allowsChildren = true;

        listChildren = new BasicEventList();

        initializeNode();
    }

    /**
     * Creates a new GlazedTreeNode and sets its children.
     *
     * @param listChildren EventList of children
     */
    public GlazedTreeNode(EventList listChildren) {

        this.listChildren = listChildren;

        allowsChildren = true;

        initializeNode();
    }

    /**
     * Creates a new GlazedTreeNode and sets its userObject.
     *
     * @param userObject The user object for the node
     */
    public GlazedTreeNode(Object userObject) {

        this.userObject = userObject;

        allowsChildren = true;

        listChildren = new BasicEventList();

        initializeNode();
    }

    /**
     * Creates a new GlazedTreeNode, sets its user object and sets its children.
     *
     * @param userObject   The user object for the node
     * @param listChildren EventList of children
     */
    public GlazedTreeNode(Object userObject, EventList listChildren) {

        this.userObject = userObject;

        allowsChildren = true;

        this.listChildren = listChildren;

        initializeNode();
    }

    /**
     * Creates a new GlazedTreeNode, sets its userObject and flags if it can have children or not.
     *
     * @param userObject     The user object for the node
     * @param allowsChildren Flag to indicate if the node can have children
     */
    public GlazedTreeNode(Object userObject, boolean allowsChildren) {

        this.userObject = userObject;

        this.allowsChildren = allowsChildren;

        if (allowsChildren) {

            listChildren = new BasicEventList();
        }

        initializeNode();
    }

    /**
     * Passes the provided list event to the parent node. This node will add itself to the path of nodes, this is used
     * to create a TreePath for the TreeModel.
     *
     * @param event Event to pass
     */
    public void passListEvent(GlazedTreeModelEvent event) {

        event.addNodeToPath(this);

        fireGlazedTreeModelEvent(event);

        if (nodeParent != null && nodeParent instanceof GlazedTreeNode) {

            ((GlazedTreeNode) nodeParent).passListEvent(event);
        }
    }

    /**
     * Adds a new GlazedTreeNodeListener to this node.
     *
     * @param listener Listener
     */
    public void addListener(GlazedTreeNodeListener listener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a GlazedTreeNodeListener from this node.
     *
     * @param listener Listener
     */
    public void removeListener(GlazedTreeNodeListener listener) {

        listeners.remove(listener);
    }

    // private methods

    /**
     * Initializes the node and sets up listeners.
     */
    private void initializeNode() {

        listeners = new HashSet();

        if (listChildren != null) {

            listChildren.addListEventListener(new ListEventListener() {

                public void listChanged(ListEvent listEvent) {

                    GlazedTreeModelEvent event = new GlazedTreeModelEvent(listEvent);

                    passListEvent(event);
                }
            });
        }
    }

    /**
     * Fires off a GlazedTreeModelEvent and notifies all of the listeners.
     *
     * @param event Event
     */
    private void fireGlazedTreeModelEvent(GlazedTreeModelEvent event) {

        GlazedTreeNodeListener[] listenerArray = (GlazedTreeNodeListener[])
                listeners.toArray(new GlazedTreeNodeListener[listeners.size()]);

        for (int index = 0; index < listenerArray.length; index++) {

            listenerArray[index].handleGlazedTreeModelEvent(event);
        }
    }

    // mutable tree node methods

    /**
     * Inserts the child node at the index provided.
     *
     * @param child Child node
     * @param index Index used to palce the child
     */
    public void insert(MutableTreeNode child, int index) {

        child.setParent(this);
        listChildren.add(index, child);
    }

    /**
     * Removes the child node at the provided index.
     *
     * @param index Index of the child to be removed
     */
    public void remove(int index) {

        Object object = listChildren.get(index);

        if (object instanceof MutableTreeNode) {

            ((MutableTreeNode) object).setParent(null);
        }

        listChildren.remove(index);
    }

    /**
     * Removes the child from the node.
     *
     * @param child Node to remove
     */
    public void remove(MutableTreeNode child) {

        child.setParent(null);
        listChildren.remove(child);
    }

    /**
     * Sets the user object for the node.
     *
     * @param userObject New user object
     */
    public void setUserObject(Object userObject) {

        this.userObject = userObject;
    }

    /**
     * Returns the user object for the node.
     *
     * @return User object
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * Removes this node from its parent node.
     */
    public void removeFromParent() {

        nodeParent.remove(this);
        nodeParent = null;
    }

    /**
     * Sets the parent node for this node.
     *
     * @param nodeParent Parent node.
     */
    public void setParent(MutableTreeNode nodeParent) {

        this.nodeParent = nodeParent;
    }

    // tree node methods

    /**
     * Returns the child node at the index provided.
     *
     * @param index Index used to pull the child node
     * @return Child node
     */
    public TreeNode getChildAt(int index) {

        Object object = listChildren.get(index);

        TreeNode treeNode = null;
        if (object instanceof TreeNode) {

            treeNode = (TreeNode) object;
        } else {

            // this isn't a tree node, wrap it in one
            treeNode = new GlazedTreeNode(object, false);
        }

        return (treeNode);
    }

    /**
     * Returns the number of children for this node.
     *
     * @return Number of children
     */
    public int getChildCount() {

        return (listChildren.size());
    }

    /**
     * Returns the parent for this node.
     *
     * @return Parent node=
     */
    public TreeNode getParent() {

        return (nodeParent);
    }

    /**
     * Returns the index of the child node.
     *
     * @param child Child node
     * @return Index in the parent
     */
    public int getIndex(TreeNode child) {

        return (listChildren.indexOf(child));
    }

    /**
     * Returns true if this node can have children.
     *
     * @return True if this node can have children
     */
    public boolean getAllowsChildren() {

        return (allowsChildren);
    }

    /**
     * Returns true if this node does have children.
     *
     * @return True if this node has children
     */
    public boolean isLeaf() {

        boolean isLeaf = true;

        if (listChildren != null && listChildren.size() > 0) {

            isLeaf = false;
        }

        return (isLeaf);
    }

    /**
     * Returns an enumeration of children for this node.
     *
     * @return Enumeration of child nodes
     */
    public Enumeration children() {

        return (new IteratorEnumeration(listChildren.listIterator()));
    }

    // other methods

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GlazedTreeNode that = (GlazedTreeNode) o;

        if (allowsChildren != that.allowsChildren) {
            return false;
        }
        if (nodeParent != null ? !nodeParent.equals(that.nodeParent) : that.nodeParent != null) {
            return false;
        }
        if (userObject != null ? !userObject.equals(that.userObject) : that.userObject != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (userObject != null ? userObject.hashCode() : 0);
        result = 31 * result + (allowsChildren ? 1 : 0);
        result = 31 * result + (nodeParent != null ? nodeParent.hashCode() : 0);
        return result;
    }
}
