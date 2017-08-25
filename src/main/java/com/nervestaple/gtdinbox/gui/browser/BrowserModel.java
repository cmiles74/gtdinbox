package com.nervestaple.gtdinbox.gui.browser;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import com.nervestaple.gtdinbox.datastore.DataStoreException;
import com.nervestaple.gtdinbox.datastore.DataStoreManager;
import com.nervestaple.gtdinbox.gui.utility.glazedtreemodel.GlazedTreeNode;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.SimpleReferenceItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import org.apache.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Provides a data model for the BrowserPanel.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class BrowserModel {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * List of projects.
     */
    private EventList<Project> projects;

    /**
     * List of contexts.
     */
    private EventList<InboxContext> contexts;

    /**
     * List of categories.
     */
    private EventList<Category> categories;

    /**
     * List of trash.
     */
    private EventList<Trashable> trash;

    /**
     * Selected tree node.
     */
    private GlazedTreeNode selectedTreeNode;

    /**
     * Search string.
     */
    private String searchText;

    /**
     * Property change support object.
     */
    private transient PropertyChangeSupport propertychangesupport;

    /**
     * Creates a new BrowserModel.
     */
    public BrowserModel() {

        initializeBrowserModel(false);
    }

    /**
     * Creates a new BrowserModel.
     *
     * @param generateTestData indicates if the model should fill itself with test data
     */
    public BrowserModel(boolean generateTestData) {

        initializeBrowserModel(generateTestData);
    }

    /**
     * Loads in data form the database
     *
     * @throws DataStoreException
     */
    public void loadData() throws DataStoreException {

        // clear all collections
        projects.clear();
        contexts.clear();
        categories.clear();
        trash.clear();

        // load in data
        projects.addAll(DataStoreManager.getProjects());
        contexts.addAll(DataStoreManager.getContexts());
        categories.addAll(DataStoreManager.getCategories());
        trash.addAll(DataStoreManager.getTrash());
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

    public EventList getProjects() {
        return projects;
    }

    public EventList getContexts() {
        return contexts;
    }

    public EventList getCategories() {
        return categories;
    }

    public EventList getTrash() {
        return trash;
    }

    public GlazedTreeNode getSelectedTreeNode() {
        return selectedTreeNode;
    }

    public void setSelectedTreeNode(GlazedTreeNode selectedTreeNode) {
        GlazedTreeNode valueOld = this.selectedTreeNode;
        this.selectedTreeNode = selectedTreeNode;
        propertychangesupport.firePropertyChange("selectedTreeNode", valueOld, this.selectedTreeNode);
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        String valueOld = searchText;
        this.searchText = searchText;
        propertychangesupport.firePropertyChange("searchText", valueOld, this.searchText);
    }

    // private methods

    private void initializeBrowserModel(boolean generateTestData) {

        propertychangesupport = new PropertyChangeSupport(this);

        // create our lists
        projects = new BasicEventList();
        contexts = new BasicEventList();
        categories = new BasicEventList();
        trash = new BasicEventList();

        if (generateTestData) {
            generateTestData();
        }
    }

    private void generateTestData() {

        // create some projects
        Project project = new Project();
        project.setId(new Long(1));
        project.setName("Project 1");
        projects.add(project);

        // create some action items
        ActionItem actionItem = new ActionItem();
        actionItem.setId(new Long(1));
        actionItem.setDescription("Action Item 1.1");
        actionItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        project.addActionItem(actionItem);

        actionItem = new ActionItem();
        actionItem.setId(new Long(2));
        actionItem.setDescription("Action Item 1.2");
        actionItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        project.addActionItem(actionItem);

        project = new Project();
        project.setId(new Long(2));
        project.setName("Project 2");
        projects.add(project);

        // create some action items
        actionItem = new ActionItem();
        actionItem.setId(new Long(3));
        actionItem.setDescription("Action Item 2.1");
        actionItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        project.addActionItem(actionItem);

        actionItem = new ActionItem();
        actionItem.setId(new Long(4));
        actionItem.setDescription("Action Item 2.2");
        actionItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        project.addActionItem(actionItem);

        project = new Project();
        project.setId(new Long(3));
        project.setName("Project 3");
        projects.add(project);

        actionItem = new ActionItem();
        actionItem.setId(new Long(5));
        actionItem.setDescription("Action Item 3.1");
        actionItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        project.addActionItem(actionItem);

        actionItem = new ActionItem();
        actionItem.setId(new Long(6));
        actionItem.setDescription("Action Item 3.2");
        actionItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        project.addActionItem(actionItem);

        // create some contexts
        InboxContext context = new InboxContext();
        context.setId(new Long(1));
        context.setName("Context 1");
        contexts.add(context);

        context = new InboxContext();
        context.setId(new Long(2));
        context.setName("Context 2");
        contexts.add(context);

        context = new InboxContext();
        context.setId(new Long(3));
        context.setName("Context 3");
        contexts.add(context);

        // create some categories
        Category category = new Category();
        category.setId(new Long(1));
        category.setName("Category 1");
        categories.add(category);

        // create some reference items
        ReferenceItem referenceItem = new SimpleReferenceItem();
        referenceItem.setId(new Long(1));
        referenceItem.setDescription("Reference Item 1.1");
        referenceItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        category.addReferenceItem(referenceItem);

        referenceItem = new SimpleReferenceItem();
        referenceItem.setId(new Long(2));
        referenceItem.setDescription("Reference Item 1.2");
        referenceItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        category.addReferenceItem(referenceItem);

        referenceItem = new SimpleReferenceItem();
        referenceItem.setId(new Long(3));
        referenceItem.setDescription("Reference Item 1.3");
        referenceItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        category.addReferenceItem(referenceItem);

        category = new Category();
        category.setId(new Long(2));
        category.setName("Category 2");
        categories.add(category);

        // create some reference items
        referenceItem = new SimpleReferenceItem();
        referenceItem.setId(new Long(4));
        referenceItem.setDescription("Reference Item 2.1");
        referenceItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        category.addReferenceItem(referenceItem);

        referenceItem = new SimpleReferenceItem();
        referenceItem.setId(new Long(5));
        referenceItem.setDescription("Reference Item 2.2");
        referenceItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        category.addReferenceItem(referenceItem);

        referenceItem = new SimpleReferenceItem();
        referenceItem.setId(new Long(6));
        referenceItem.setDescription("Reference Item 2.3");
        referenceItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        category.addReferenceItem(referenceItem);

        category = new Category();
        category.setId(new Long(3));
        category.setName("Category 3");
        categories.add(category);

        referenceItem = new SimpleReferenceItem();
        referenceItem.setId(new Long(7));
        referenceItem.setDescription("Reference Item Trash 1");
        referenceItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        trash.add(referenceItem);

        actionItem = new ActionItem();
        actionItem.setId(new Long(7));
        actionItem.setDescription("Action Item Trash 1");
        actionItem.setDescriptionTextStyleType(TextStyleType.PLAIN_TEXT);
        trash.add(actionItem);

        Thread thread = new Thread(new Runnable() {

            public void run() {

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    logger.warn(e);
                }

                Project projectNew = new Project();
                projectNew.setId(new Long(10));
                projectNew.setName("New Project!");

                logger.debug("Adding a new project...");
                projects.add(projectNew);

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    logger.warn(e);
                }

                logger.debug("Removing the new project...");
                projects.remove(projectNew);
            }
        });
        thread.start();
    }
}
