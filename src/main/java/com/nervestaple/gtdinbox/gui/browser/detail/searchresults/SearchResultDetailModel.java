package com.nervestaple.gtdinbox.gui.browser.detail.searchresults;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import org.apache.lucene.document.Document;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a data model for the search result detail form.
 */
public class SearchResultDetailModel {

    /**
     * Name of the trash view.
     */
    private final static String HEADER_NAME = "Search Results";

    /**
     * Name.
     */
    private String name;

    /**
     * Description.
     */
    private String description;

    /**
     * List of items in the result view.
     */
    private EventList<Document> listItems;

    /**
     * Selected items.
     */
    private List<Document> selectedItems;

    /**
     * Property change support object.
     */
    private transient PropertyChangeSupport propertychangesupport;

    /**
     * Creates a new SearchResultDetailModel.
     */
    public SearchResultDetailModel() {

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

    public void setName(String name) {
        String valueOld = this.name;
        this.name = name;
        propertychangesupport.firePropertyChange("name", valueOld, this.name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String valueOld = this.description;
        this.description = description;
        propertychangesupport.firePropertyChange("description", valueOld, this.description);
    }

    public EventList getListItems() {
        return listItems;
    }

    public void setListItems(EventList<Document> listItems) {
        EventList valueOld = this.listItems;
        this.listItems = listItems;
        propertychangesupport.firePropertyChange("listItems", valueOld, this.listItems);
    }

    public List getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Document> selectedItems) {
        List valueOld = this.selectedItems;
        this.selectedItems = selectedItems;
        propertychangesupport.firePropertyChange("selectedItems", valueOld, this.selectedItems);
    }

    // private methods

    private void initializeValues() {

        this.name = HEADER_NAME;

        listItems = new BasicEventList<>();

        selectedItems = new ArrayList<>();
    }
}
