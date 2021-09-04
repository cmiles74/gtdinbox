package com.nervestaple.gtdinbox.gui.browser.detail.trash;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a data model for the TrashDetail form.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TrashDetailModel {

    /**
     * Name of the trash view.
     */
    private final static String HEADER_NAME = "Trash";

    /**
     * Name.
     */
    private String name;

    /**
     * Description.
     */
    private String description;

    /**
     * List of items in the trash.
     */
    private EventList listItems;

    /**
     * Action listener for the empty trash button.
     */
    private ActionListener actionListenerEmptyTrash;

    /**
     * Action listener for the put away button.
     */
    private ActionListener actionListenerPutAway;

    /**
     * Selected items.
     */
    private List selectedItems;

    /**
     * Property change support object.
     */
    private transient PropertyChangeSupport propertychangesupport;

    /**
     * Creates a new TrashDetailModel.
     */
    public TrashDetailModel() {

        propertychangesupport = new PropertyChangeSupport(this);

        initializeValues();
    }

    // property change support methods

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertychangesupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertychangesupport.removePropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property,
                                          PropertyChangeListener listener) {
        propertychangesupport.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property,
                                             PropertyChangeListener listener) {
        propertychangesupport.removePropertyChangeListener(property, listener);
    }

    // accessor and mutator methods

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        String valueOld = this.description;
        this.description = description;
        propertychangesupport.firePropertyChange("description", valueOld, this.description);
    }

    public EventList getListItems() {
        return listItems;
    }

    public void setListItems(final EventList listItems) {
        EventList valueOld = this.listItems;
        this.listItems = listItems;
        propertychangesupport.firePropertyChange("listItems", valueOld, this.listItems);
    }

    public ActionListener getActionListenerEmptyTrash() {
        return actionListenerEmptyTrash;
    }

    public void setActionListenerEmptyTrash(final ActionListener actionListenerEmptyTrash) {
        ActionListener valueOld = this.actionListenerEmptyTrash;
        this.actionListenerEmptyTrash = actionListenerEmptyTrash;
        propertychangesupport.firePropertyChange("actionListenerEmptyTrash", valueOld, this.actionListenerEmptyTrash);
    }

    public ActionListener getActionListenerPutAway() {
        return actionListenerPutAway;
    }

    public void setActionListenerPutAway(final ActionListener actionListenerPutAway) {
        ActionListener valueOld = this.actionListenerPutAway;
        this.actionListenerPutAway = actionListenerPutAway;
        propertychangesupport.firePropertyChange("actionListenerPutAway", valueOld, this.actionListenerPutAway);
    }

    public List getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(final List selectedItems) {
        List valueOld = this.selectedItems;
        this.selectedItems = selectedItems;
        propertychangesupport.firePropertyChange("selectedItems", valueOld, this.selectedItems);
    }

    // private methods

    private void initializeValues() {

        this.name = HEADER_NAME;

        listItems = new BasicEventList();

        selectedItems = new ArrayList();
    }
}
