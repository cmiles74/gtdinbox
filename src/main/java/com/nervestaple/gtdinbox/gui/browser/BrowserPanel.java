package com.nervestaple.gtdinbox.gui.browser;

import ca.odell.glazedlists.EventList;
import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import com.google.common.eventbus.Subscribe;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.datastore.DataStoreException;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.datastore.index.IndexManager;
import com.nervestaple.gtdinbox.datastore.index.IndexManagerListener;
import com.nervestaple.gtdinbox.datastore.index.SearchResultHandler;
import com.nervestaple.gtdinbox.gui.ApplicationManager;
import com.nervestaple.gtdinbox.gui.GTDInboxExceptionHandler;
import com.nervestaple.gtdinbox.gui.browser.detail.DetailActionItemListListener;
import com.nervestaple.gtdinbox.gui.browser.detail.DetailArchivePanel;
import com.nervestaple.gtdinbox.gui.browser.detail.DetailContextPanel;
import com.nervestaple.gtdinbox.gui.browser.detail.DetailProjectPanel;
import com.nervestaple.gtdinbox.gui.browser.detail.searchresults.SearchResultDetailPanel;
import com.nervestaple.gtdinbox.gui.browser.detail.trash.TrashDetailController;
import com.nervestaple.gtdinbox.gui.browser.detail.trash.TrashDetailControllerListener;
import com.nervestaple.gtdinbox.gui.browser.renderer.BrowserTreeCellRenderer;
import com.nervestaple.gtdinbox.gui.event.action.MessageAction;
import com.nervestaple.gtdinbox.gui.event.item.ChangedEvent;
import com.nervestaple.gtdinbox.gui.event.action.MessageActionEvent;
import com.nervestaple.gtdinbox.gui.event.item.SelectionEvent;
import com.nervestaple.gtdinbox.gui.form.actionitem.ActionItemFormListener;
import com.nervestaple.gtdinbox.gui.form.actionitem.ActionItemFrame;
import com.nervestaple.gtdinbox.gui.form.category.CategoryFormListener;
import com.nervestaple.gtdinbox.gui.form.category.CategoryFrame;
import com.nervestaple.gtdinbox.gui.form.context.ContextFormListener;
import com.nervestaple.gtdinbox.gui.form.context.ContextFrame;
import com.nervestaple.gtdinbox.gui.form.project.ProjectFormListener;
import com.nervestaple.gtdinbox.gui.form.project.ProjectFrame;
import com.nervestaple.gtdinbox.gui.utility.glazedtreemodel.GlazedTreeModel;
import com.nervestaple.gtdinbox.gui.utility.glazedtreemodel.GlazedTreeNode;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.utility.swing.GuiSwing;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

/**
 * Provides a form for the BrowserPanel.
 */
public class BrowserPanel extends JPanel implements GTDInboxExceptionHandler {

    /**
     * Application icon.
     */
    private final ImageIcon ICON_APPLICATION;

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Data model for the BrowserPanel.
     */
    private BrowserModel model;

    /**
     * Data model for the tree.
     */
    private TreeModel treeModel;

    /**
     * Project detail panel.
     */
    private DetailProjectPanel detailProjectPanel;

    /**
     * Context detail panel.
     */
    private DetailContextPanel detailContextPanel;

    /**
     * Archive detail panel.
     */
    private DetailArchivePanel detailArchivePanel;

    /**
     * Search detail panel.
     */
    private SearchResultDetailPanel detailSearchPanel;

    /**
     * Trash detail panel.
     */
    private TrashDetailController trashDetailController;

    /**
     * Detail panel being displayed.
     */
    private JPanel panelDetail;

    /**
     * Selected item in the detail pane.
     */
    private Object panelDetailSelectedItem;

    /**
     * Listeners for DetailActionItems.
     */
    private DetailActionItemListListener detailActionItemListListenerProject;

    /**
     * Listeners for the context DetailActionItems.
     */
    private DetailActionItemListListener detailActionItemListListenerContext;

    /**
     * Listeners for the archive DetailActionItems.
     */
    private DetailActionItemListListener detailActionItemListListenerArchive;

    /**
     * Empty detail panel.
     */
    private JPanel panelEmpty;

    /**
     * Timer for the search field.
     */
    private Timer timerSearch;

    /**
     * Delay for the search timer (in milliseconds).
     */
    private final static int SEARCH_TIMER_DELAY = 300;

    /**
     * The last detail panel displayed.
     */
    private JPanel detailPanelLast;

    /**
     * Query parser for searching.
     */
    private QueryParser queryParser;

    /**
     * The query currently being displayed.
     */
    private BooleanQuery querySearch;

    // gui form objects
    private JPanel panelMain;
    private JTextField textFieldSearch;
    private JButton buttonClearSearch;
    private JPanel panelSearchField;
    private JScrollPane scrollPaneItemTree;
    private JTree treeItems;
    private JSplitPane splitPaneMain;
    private JPanel panelMiddle;
    private JButton buttonSearchOptions;
    private JButton buttonAddProject;
    private JToolBar toolBar;
    private JButton buttonAddContext;
    private JButton buttonAddAction;
    private JButton buttonDelete;
    private JScrollPane scrollPaneContent;
    private JPanel panelToolBar;
    private JSeparator seperator;
    private JPanel panelSearch;

    /**
     * Creates a new BrowserPanel.
     */
    public BrowserPanel() {

        super();

        // load icons
        ICON_APPLICATION = new ImageIcon(getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/images/application-32.png"));

        // create a data model for the form
        model = new BrowserModel();

        // load data from the data store
        try {
            model.loadData();
        } catch (DataStoreException e) {
            handleErrorOccurred(e);
        }

        // setup the empty panel
        panelEmpty = new JPanel();
        panelEmpty.setBackground(Color.WHITE);

        // add the main panel to this panel
        setLayout(new GridLayout(1, 1));
        add(panelMain);

        initializePanel();

        generateViewFromModel();

        setupListeners();

        setupTimers();
    }

    /**
     * Prompts the user to add a new project.
     */
    public void doAddProject() {

        ProjectFrame projectFrame = ApplicationManager.getInstance().getFrameManager().getProjectFrame();
        projectFrame.addNewProject();
        GuiSwing.centerWindow(projectFrame);
        projectFrame.setVisible(true);
    }

    public void doRemoveProject(final Project project) {

        try {
            DataBaseManager.getInstance().getEntityManager().persist(project);
        } catch (DataBaseManagerException e) {

            handleErrorOccurred(e);
        }

        final JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty("OptionPane.css") +
                        "<b>Are you sure that you want to remove the project?</b><p>" +
                        "The project \"" + project.getName() + "\" and its " + project.getActionItems().size()
                        + " action items will be moved to the trash.",
                JOptionPane.WARNING_MESSAGE
        );
        Object[] options = {"Okay", "Cancel"};
        pane.setOptions(options);
        pane.setInitialValue(options[1]);
        pane.setIcon(ICON_APPLICATION);
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer(1)
        );

        JSheet.showSheet(pane, this, new SheetListener() {
            public void optionSelected(SheetEvent evt) {

                logger.debug(evt.getValue().toString());
                if (evt.getValue().toString().equals("Okay")) {

                    Thread thread = new Thread(new Runnable() {

                        public void run() {

                            removeProject(project);
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    /**
     * Prompts the user to update the data for a Project.
     *
     * @param project Project to update
     */
    public void doUpdateProject(final Project project) {
        ProjectFrame projectFrame = ApplicationManager.getInstance().getFrameManager().getProjectFrame();
        projectFrame.updateProject(project);
        GuiSwing.centerWindow(projectFrame);
        projectFrame.setVisible(true);
    }

    /**
     * Prompts the user to add a new context.
     */
    public void doAddInboxContext() {
        ContextFrame contextFrame = ApplicationManager.getInstance().getFrameManager().getContextFrame();
        contextFrame.addNewInboxContext();
        GuiSwing.centerWindow(contextFrame);
        contextFrame.setVisible(true);
    }

    /**
     * Warns the user before removing an InboxContext.
     *
     * @param inboxContext The InboxContext to be removed
     */
    public void doRemoveInboxContext(final InboxContext inboxContext) {

        try {
            DataBaseManager.getInstance().getEntityManager().persist(inboxContext);
        } catch (DataBaseManagerException e) {

            handleErrorOccurred(e);
        }

        final JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty("OptionPane.css") +
                        "<b>Are you sure that you want to remove the context?</b><p>" +
                        "The context \"" + inboxContext.getName() + "\" and orphan its "
                        + inboxContext.getActionItems().size() +
                        " action items? The action items will no longer be a " +
                        "part of any context.",
                JOptionPane.WARNING_MESSAGE
        );
        Object[] options = {"Okay", "Cancel"};
        pane.setOptions(options);
        pane.setInitialValue(options[1]);
        pane.setIcon(ICON_APPLICATION);
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer(1)
        );

        JSheet.showSheet(pane, this, new SheetListener() {
            public void optionSelected(SheetEvent evt) {

                logger.debug(evt.getValue().toString());
                if (evt.getValue().toString().equals("Okay")) {

                    Thread thread = new Thread(new Runnable() {

                        public void run() {

                            removeInboxContext(inboxContext);
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    /**
     * Prompts the user to update the data for an InboxContext.
     *
     * @param inboxContext InboxContext to update
     */
    public void doUpdateInboxContext(final InboxContext inboxContext) {
        ContextFrame contextFrame = ApplicationManager.getInstance().getFrameManager().getContextFrame();
        contextFrame.updateInboxContext(inboxContext);
        GuiSwing.centerWindow(contextFrame);
        contextFrame.setVisible(true);
    }

    /**
     * Prompts the user to add a new category.
     */
    public void doAddCategory() {
        CategoryFrame categoryFrame = ApplicationManager.getInstance().getFrameManager().getCategoryFrame();
        categoryFrame.addNewCategory();
        GuiSwing.centerWindow(categoryFrame);
        categoryFrame.setVisible(true);
    }

    /**
     * Warns the user before removing an Category.
     *
     * @param category The Category to be removed
     */
    public void doRemoveCategory(final Category category) {

        try {
            DataBaseManager.getInstance().getEntityManager().persist(category);
        } catch (DataBaseManagerException e) {

            handleErrorOccurred(e);
        }

        final JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty("OptionPane.css") +
                        "<b>Are you sure that you want to remove the category?</b><p>" +
                        "The category \"" + category.getName() + "\" and orphan its "
                        + category.getReferenceItems().size() +
                        " reference items? The reference items will no longer be a " +
                        "part of any category and will be put in the trash.",
                JOptionPane.WARNING_MESSAGE
        );
        Object[] options = {"Okay", "Cancel"};
        pane.setOptions(options);
        pane.setInitialValue(options[1]);
        pane.setIcon(ICON_APPLICATION);
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer(1)
        );

        JSheet.showSheet(pane, this, new SheetListener() {
            public void optionSelected(SheetEvent evt) {

                logger.debug(evt.getValue().toString());
                if (evt.getValue().toString().equals("Okay")) {

                    Thread thread = new Thread(new Runnable() {

                        public void run() {

                            removeCategory(category);
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    /**
     * Prompts the user to update the data for a Category.
     *
     * @param category Category to update
     */
    public void doUpdateCategory(final Category category) {
        CategoryFrame categoryFrame = ApplicationManager.getInstance().getFrameManager().getCategoryFrame();
        categoryFrame.updateCategory(category);
        GuiSwing.centerWindow(categoryFrame);
        categoryFrame.setVisible(true);
    }

    /**
     * Prompts the user to add a new ActionItem.
     *
     * @param project Project to parent the new ActionItem
     */
    public void doAddActionItem(Project project) {
        ActionItemFrame actionItemFrame = ApplicationManager.getInstance().getFrameManager().getActionItemFrame();
        actionItemFrame.addNewActionItem(project);
        GuiSwing.centerWindow(actionItemFrame);
        actionItemFrame.setVisible(true);
    }

    /**
     * Prompts the user to update the data for an ActionItem.
     *
     * @param actionItem ACtionItem to update
     */
    public void doUpdateActionItem(final ActionItem actionItem) {
        ActionItemFrame actionItemFrame = ApplicationManager.getInstance().getFrameManager().getActionItemFrame();
        actionItemFrame.updateActionItem(actionItem);
        GuiSwing.centerWindow(actionItemFrame);
        actionItemFrame.setVisible(true);
    }

    /**
     * Warns the user before removing an ActionItem.
     *
     * @param actionItem The ActionItem to be removed
     */
    public void doRemoveActionItem(final ActionItem actionItem) {

        try {
            DataBaseManager.getInstance().getEntityManager().persist(actionItem);
        } catch (DataBaseManagerException e) {

            handleErrorOccurred(e);
        }

        final JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty("OptionPane.css") +
                        "<b>Are you sure that you want to remove the action item?</b><p>" +
                        "The action item will be removed to the trash.",
                JOptionPane.WARNING_MESSAGE
        );
        Object[] options = {"Okay", "Cancel"};
        pane.setOptions(options);
        pane.setInitialValue(options[1]);
        pane.setIcon(ICON_APPLICATION);
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer(1)
        );

        JSheet.showSheet(pane, this, new SheetListener() {
            public void optionSelected(SheetEvent evt) {

                logger.debug(evt.getValue().toString());
                if (evt.getValue().toString().equals("Okay")) {

                    Thread thread = new Thread(new Runnable() {

                        public void run() {

                            removeActionItem(actionItem);
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    public void doEmptyTrash() {

        trashDetailController.emptyTrash();
    }

    // exception handler methods

    /**
     * Called when an exception from a child component occurs.
     *
     * @param exception Exception that occurred
     */
    public void handleException(GTDInboxException exception) {

        handleErrorOccurred(exception);
    }

    // accessor methods

    /**
     * Returns the data model for the BrowserPanel.
     *
     * @return BrowserModel
     */
    public BrowserModel getModel() {
        return model;
    }

    public JPanel getPanelDetail() {
        return panelDetail;
    }

    public Object getPanelDetailSelectedItem() {
        return panelDetailSelectedItem;
    }

    // private methods

    /**
     * Sets up the timers for the form.
     */
    private void setupTimers() {

        timerSearch = new Timer(SEARCH_TIMER_DELAY, new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (panelDetail == detailSearchPanel) {

                    detailSearchPanel.getModel().getListItems().clear();
                }

                try {

                    // run the search
                    doSearch();
                } catch (GTDInboxException e) {
                    handleException(e);
                }
            }
        });

        timerSearch.setRepeats(false);
    }

    /**
     * Runs a search and displays the results.
     *
     * @throws GTDInboxException on errors running the search
     */
    private void doSearch() throws GTDInboxException {

        final String searchText = StringUtils.trimToNull(model.getSearchText().toLowerCase());

        if (searchText == null) {
            return;
        }

        if (queryParser == null) {
            queryParser = new QueryParser("description", new SimpleAnalyzer());
        }

        querySearch = new BooleanQuery();

        // add a clause for the search text
        try {
            querySearch.add(queryParser.parse(searchText), BooleanClause.Occur.SHOULD);
        } catch (ParseException e) {
            throw new GTDInboxException("There was a problem parsing your search entry", e);
        }

        // add a clause for the parent
        querySearch.add(new TermQuery(new Term("parent", searchText)), BooleanClause.Occur.SHOULD);

        // add a clause for the context
        querySearch.add(new TermQuery(new Term("inboxContext", searchText)), BooleanClause.Occur.SHOULD);

        logger.debug("Running a search with the query '" + querySearch + "'");

        if (panelDetail != detailSearchPanel) {

            detailPanelLast = panelDetail;
        }

        panelDetail = detailSearchPanel;

        detailSearchPanel.getModel().getListItems().clear();
        detailSearchPanel.getModel().setDescription("Looking for \"" + searchText + "\"");

        Box box = Box.createVerticalBox();
        box.add(panelDetail);
        box.add(Box.createVerticalGlue());

        scrollPaneContent.setViewportView(box);
        detailProjectPanel.invalidate();
        scrollPaneContent.validate();

        fireDetailPanelChanged();

        final String queryDescriptionFinal = querySearch.toString();
        IndexManager.getInstance().runSearch(querySearch, new SearchResultHandler() {

            // event lists are not thread safe
            public synchronized void handleSearchResult(Document document) {

                detailSearchPanel.getModel().getListItems().getReadWriteLock().writeLock().lock();
                try {

                    detailSearchPanel.getModel().getListItems().add(document);
                } finally {

                    detailSearchPanel.getModel().getListItems().getReadWriteLock().writeLock().unlock();
                }

                detailSearchPanel.revalidate();
            }

            public void setNumberOfResults(int results) {

                detailSearchPanel.getModel().setDescription("Looking for \"" + searchText + ",\" "
                        + results + " items found");
            }
        });
    }

    private void doClearSearch() {

        logger.debug("Clearing the search results");

        querySearch = null;

        detailSearchPanel.getModel().getListItems().clear();
        detailSearchPanel.getModel().setDescription("");

        panelDetail = detailPanelLast;

        Box box = Box.createVerticalBox();
        box.add(panelDetail);
        box.add(Box.createVerticalGlue());

        scrollPaneContent.setViewportView(box);
        detailProjectPanel.invalidate();
        scrollPaneContent.validate();

        fireDetailPanelChanged();
    }

    /**
     * Notifies listeners that the detail panel has changed.
     */
    private void fireDetailPanelChanged() {
        ApplicationManager.getInstance().getEventBus().post(new ChangedEvent<>(this));
    }

    /**
     * Notifies listeners that the selected item has changed.
     */
    private void fireSelectedDetailItemChanged() {
        ApplicationManager.getInstance().getEventBus().post(new SelectionEvent<>(this));
    }

    /**
     * Prompts the user to empty the trash.
     */
    private void fireConfirmEmptyTrash(String message) {
        ApplicationManager.getInstance().getEventBus().post(
                new MessageActionEvent<>(this, MessageAction.EMPTY_TRASH));
    }

    /**
     * Updates the detail panel to match the selected tree object.
     *
     * @param selectedObject The selected object in the tree
     */
    private void handleTreeSelectionChanged(Object selectedObject) {

        // remove listeners from detail panels
        detailProjectPanel.getDetailActionItemList().removeDetailActionItemListListener(
                detailActionItemListListenerProject);
        detailContextPanel.getDetailActionItemList().removeDetailActionItemListListener(
                detailActionItemListListenerContext);
        detailArchivePanel.getDetailActionItemList().removeDetailActionItemListListener(
                detailActionItemListListenerArchive);

        if (selectedObject instanceof Project) {

            // update the project detail panel
            try {
                detailProjectPanel.setProject((Project) selectedObject);
            } catch (DataBaseManagerException e) {
                handleException(e);
            }

            // add a listener to this panel
            detailProjectPanel.getDetailActionItemList().addDetailActionItemListListener(
                    detailActionItemListListenerProject);

            if (panelDetail != detailProjectPanel) {

                // set the project panel to display
                panelDetail = detailProjectPanel;

                detailProjectPanel.clearSelection();

                Box box = Box.createVerticalBox();
                box.add(detailProjectPanel);
                box.add(Box.createVerticalGlue());

                scrollPaneContent.setViewportView(box);
                detailProjectPanel.invalidate();
                scrollPaneContent.validate();
            } else {

                detailProjectPanel.clearSelection();
            }
        } else if (selectedObject instanceof InboxContext) {

            // update the project detail panel
            try {
                detailContextPanel.setContext((InboxContext) selectedObject);
            } catch (DataBaseManagerException e) {
                handleException(e);
            }

            // add a listener to this panel
            detailContextPanel.getDetailActionItemList().addDetailActionItemListListener(
                    detailActionItemListListenerContext);

            if (panelDetail != detailContextPanel) {

                // set the project panel to display
                panelDetail = detailContextPanel;

                detailContextPanel.clearSelection();

                Box box = Box.createVerticalBox();
                box.add(detailContextPanel);
                box.add(Box.createVerticalGlue());

                scrollPaneContent.setViewportView(box);
                detailContextPanel.invalidate();
                scrollPaneContent.validate();
            } else {

                detailProjectPanel.clearSelection();
                detailContextPanel.clearSelection();
            }
        } else if (selectedObject instanceof TopLevelContainer
                && selectedObject.equals(TopLevelContainer.TRASH)) {

            // set the project panel to display
            logger.debug("Displaying the trash detail view");
            panelDetail = trashDetailController.getForm();

            detailContextPanel.clearSelection();
            detailContextPanel.clearSelection();
            detailArchivePanel.clearSelection();

            Box box = Box.createVerticalBox();
            box.add(trashDetailController.getForm());
            box.add(Box.createVerticalGlue());

            scrollPaneContent.setViewportView(box);
            trashDetailController.getForm().invalidate();
            scrollPaneContent.validate();

            // load in the trash
            trashDetailController.loadData();
        } else if (selectedObject instanceof TopLevelContainer
                && selectedObject.equals(TopLevelContainer.ARCHIVE)) {

            // set the project panel to display
            logger.debug("Displaying the archive detail view");

            try {
                detailArchivePanel.loadData();
            } catch (DataBaseManagerException e) {
                handleException(e);
            }

            // add a listener to this panel
            detailArchivePanel.getDetailActionItemList().addDetailActionItemListListener(
                    detailActionItemListListenerArchive);

            if (panelDetail != detailArchivePanel) {

                // set the project panel to display
                panelDetail = detailArchivePanel;

                detailArchivePanel.clearSelection();

                Box box = Box.createVerticalBox();
                box.add(detailArchivePanel);
                box.add(Box.createVerticalGlue());

                scrollPaneContent.setViewportView(box);
                detailArchivePanel.invalidate();
                scrollPaneContent.validate();
            } else {

                detailArchivePanel.clearSelection();
                detailProjectPanel.clearSelection();
                detailContextPanel.clearSelection();
            }
        } else {

            clearDetailPanel();
        }

        fireDetailPanelChanged();
    }

    /**
     * Removes an ActionItem from the data store.
     *
     * @param actionItem ActionItem to remove
     */
    private void removeActionItem(ActionItem actionItem) {

        try {

            // get a session and attach the project
            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
            entityManager.persist(actionItem);

            // start a new transaction and set the project as deleted
            DataBaseManager.getInstance().beginTransaction();
            actionItem.setDeleted(Boolean.valueOf(true));
            entityManager.persist(actionItem);

            // commit the transaction and close the session
            DataBaseManager.getInstance().commitTransaction();

            // lock for writing
            model.getTrash().getReadWriteLock().writeLock().lock();

            try {

                // put the project in the trash
                model.getTrash().add(actionItem);
            } finally {

                // release the lock
                model.getTrash().getReadWriteLock().writeLock().unlock();
            }

            if (panelDetail == detailProjectPanel) {
                detailProjectPanel.removeActionItem(actionItem);
            } else if (panelDetail == detailContextPanel) {
                detailContextPanel.removeActionItem(actionItem);
            }
        } catch (DataBaseManagerException e) {

            handleErrorOccurred(e);
        }
    }

    /**
     * Removes a category from the data store.
     *
     * @param category Category to remove
     */
    private void removeCategory(Category category) {

        try {

            // get a session and attach the project
            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
            entityManager.persist(category);

            // start a new transaction and set the project as deleted
            DataBaseManager.getInstance().beginTransaction();
            category.setDeleted(Boolean.valueOf(true));
            entityManager.persist(category);

            // loop through the actions attached to the project
            Iterator iterator = category.getReferenceItems().iterator();
            while (iterator.hasNext()) {

                // set the action as deleted and save to the data store
                ReferenceItem referenceItem = (ReferenceItem) iterator.next();
                referenceItem.setDeleted(Boolean.valueOf(true));
                entityManager.persist(referenceItem);
            }

            // commit the transaction and close the session
            DataBaseManager.getInstance().commitTransaction();

            // lock for writing
            model.getCategories().getReadWriteLock().writeLock().lock();

            try {

                Iterator iteratorList = model.getCategories().listIterator();
                while (iteratorList.hasNext()) {

                    Category categoryThis = (Category) iteratorList.next();

                    if (categoryThis.getId().equals(category.getId())) {

                        iteratorList.remove();
                        break;
                    }
                }
            } finally {

                // release the lock
                model.getCategories().getReadWriteLock().writeLock().unlock();
            }

            // lock for writing
            model.getTrash().getReadWriteLock().writeLock().lock();

            try {

                // put the project in the trash
                model.getTrash().add(category);
            } finally {

                // release the lock
                model.getTrash().getReadWriteLock().writeLock().unlock();
            }
        } catch (DataBaseManagerException e) {

            handleErrorOccurred(e);
        }
    }

    /**
     * Removes an InboxContext from the data store.
     *
     * @param inboxContext Context to remove
     */
    private void removeInboxContext(InboxContext inboxContext) {

        try {

            // get a session and attach the project
            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
            entityManager.persist(inboxContext);

            // start a new transaction and set the project as deleted
            DataBaseManager.getInstance().beginTransaction();
            inboxContext.setDeleted(Boolean.valueOf(true));
            entityManager.persist(inboxContext);

            // loop through the actions attached to the project
            Iterator iterator = inboxContext.getActionItems().iterator();
            while (iterator.hasNext()) {

                // clear the context for the action item
                ActionItem actionItem = (ActionItem) iterator.next();
                actionItem.setInboxContext(null);
                entityManager.persist(actionItem);
            }

            // commit the transaction and close the session
            DataBaseManager.getInstance().commitTransaction();

            // lock for writing
            model.getContexts().getReadWriteLock().writeLock().lock();

            try {

                Iterator iteratorList = model.getContexts().listIterator();
                while (iteratorList.hasNext()) {

                    InboxContext inboxContextThis = (InboxContext) iteratorList.next();

                    if (inboxContextThis.getId().equals(inboxContext.getId())) {

                        iteratorList.remove();
                        break;
                    }
                }
            } finally {

                // release the lock
                model.getContexts().getReadWriteLock().writeLock().unlock();
            }

            // lock for writing
            model.getTrash().getReadWriteLock().writeLock().lock();

            try {

                // put the project in the trash
                model.getTrash().add(inboxContext);
            } finally {

                // release the lock
                model.getTrash().getReadWriteLock().writeLock().unlock();
            }
        } catch (DataBaseManagerException e) {

            handleErrorOccurred(e);
        }
    }

    /**
     * Removes a project from the data store.
     *
     * @param project Project to remove
     */
    private void removeProject(Project project) {

        try {

            // get a session and attach the project
            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
            entityManager.persist(project);

            // start a new transaction and set the project as deleted
            DataBaseManager.getInstance().beginTransaction();
            project.setDeleted(Boolean.valueOf(true));
            entityManager.persist(project);

            // loop through the actions attached to the project
            Iterator iterator = project.getActionItems().iterator();
            while (iterator.hasNext()) {

                // set the action as deleted and save to the data store
                ActionItem actionItem = (ActionItem) iterator.next();
                actionItem.setDeleted(Boolean.valueOf(true));
                entityManager.persist(actionItem);
            }

            // commit the transaction and close the session
            DataBaseManager.getInstance().commitTransaction();

            // lock for writing
            model.getProjects().getReadWriteLock().writeLock().lock();

            try {

                Iterator iteratorList = model.getProjects().listIterator();
                while (iteratorList.hasNext()) {

                    Project projectThis = (Project) iteratorList.next();

                    if (projectThis.getId().equals(project.getId())) {

                        iteratorList.remove();
                        break;
                    }
                }
            } finally {

                // release the lock
                model.getProjects().getReadWriteLock().writeLock().unlock();
            }

            // lock for writing
            model.getTrash().getReadWriteLock().writeLock().lock();

            try {

                // put the project in the trash
                model.getTrash().add(project);
            } finally {

                // release the lock
                model.getTrash().getReadWriteLock().writeLock().unlock();
            }
        } catch (DataBaseManagerException e) {

            handleErrorOccurred(e);
        }
    }

    /**
     * Sets up listeners for the various GUI objects.
     */
    private void setupListeners() {

        // setup a debugging listener on the model
        model.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {

                logger.debug(event.getPropertyName() + ": " + event.getOldValue() + " -> " + event.getNewValue());
            }
        });

        buttonAddProject.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                doAddProject();
            }
        });

        buttonAddContext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                doAddInboxContext();
            }
        });

        /*buttonAddCategory.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                doAddCategory();
            }
        } );*/

        buttonAddAction.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (model.getSelectedTreeNode() != null && model.getSelectedTreeNode().getUserObject() != null
                        && model.getSelectedTreeNode().getUserObject() instanceof Project) {

                    doAddActionItem((Project) model.getSelectedTreeNode().getUserObject());
                }
            }
        });

        buttonDelete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (panelDetailSelectedItem != null && panelDetailSelectedItem instanceof ActionItem) {

                    doRemoveActionItem((ActionItem) panelDetailSelectedItem);
                } else if (model.getSelectedTreeNode() != null && model.getSelectedTreeNode().getUserObject() != null
                        && model.getSelectedTreeNode().getUserObject() instanceof Project) {

                    doRemoveProject((Project) model.getSelectedTreeNode().getUserObject());
                } else if (model.getSelectedTreeNode() != null && model.getSelectedTreeNode().getUserObject() != null
                        && model.getSelectedTreeNode().getUserObject() instanceof Category) {

                    doRemoveCategory((Category) model.getSelectedTreeNode().getUserObject());
                } else if (model.getSelectedTreeNode() != null && model.getSelectedTreeNode().getUserObject() != null
                        && model.getSelectedTreeNode().getUserObject() instanceof InboxContext) {

                    doRemoveInboxContext((InboxContext) model.getSelectedTreeNode().getUserObject());
                }
            }
        });

        treeItems.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent treeSelectionEvent) {

                if (treeSelectionEvent.getNewLeadSelectionPath() != null
                        && treeSelectionEvent.getNewLeadSelectionPath().getLastPathComponent() != null) {

                    final GlazedTreeNode treeNode =
                            (GlazedTreeNode) treeSelectionEvent.getNewLeadSelectionPath().getLastPathComponent();

                    model.setSelectedTreeNode(treeNode);

                    handleTreeSelectionChanged(treeNode.getUserObject());
                }
            }
        });

        ApplicationManager.getInstance().getFrameManager().getProjectFrame().addProjectFormListener(
                new ProjectFormListener() {

                    /**
                     * Called when a new project is added.
                     *
                     * @param project Project that was added
                     */
                    public void projectAdded(final Project project) {

                        boolean addProject = true;
                        Iterator iterator = model.getProjects().listIterator();
                        while (iterator.hasNext()) {

                            if (((Project) iterator.next()).getId().equals(project.getId())) {
                                addProject = false;
                                break;
                            }
                        }

                        if (addProject) {

                            // lock for writing
                            model.getProjects().getReadWriteLock().writeLock().lock();

                            try {

                                // add the new entry
                                model.getProjects().add(project);
                            } finally {

                                // release the lock
                                model.getProjects().getReadWriteLock().writeLock().unlock();
                            }
                        }
                    }

                    /**
                     * Called when a new project is updated.
                     *
                     * @param project Project that was updated
                     */
                    public void projectUpdated(Project project) {

                        EventList projects = model.getProjects();

                        Iterator iterator = projects.listIterator();
                        while (iterator.hasNext()) {

                            Project projectThis = (Project) iterator.next();

                            if (projectThis.getId().equals(project.getId())) {

                                int index = projects.indexOf(projectThis);

                                // lock for writing
                                model.getProjects().getReadWriteLock().writeLock().lock();

                                try {

                                    // remove the old entry and add the new one
                                    projects.remove(index);
                                    projects.add(index, project);
                                } finally {

                                    // release the lock
                                    model.getProjects().getReadWriteLock().writeLock().unlock();
                                }

                                break;
                            }
                        }
                    }

                    /**
                     * Called when an error occurs.
                     *
                     * @param exception The exception that occurred
                     */
                    public void exceptionOccurred(GTDInboxException exception) {

                        handleErrorOccurred(exception);
                    }
                });

        ApplicationManager.getInstance().getFrameManager().getContextFrame().addInboxContextFormListener(
                new ContextFormListener() {


                    /**
                     * Called when a new inboxContext is added.
                     *
                     * @param inboxContext InboxContext that was added
                     */
                    public void inboxContextAdded(final InboxContext inboxContext) {

                        boolean addContext = true;
                        Iterator iterator = model.getContexts().listIterator();
                        while (iterator.hasNext()) {

                            if (((InboxContext) iterator.next()).getId().equals(inboxContext.getId())) {

                                addContext = false;

                                break;
                            }
                        }
                        if (addContext) {

                            // lock for writing
                            model.getContexts().getReadWriteLock().writeLock().lock();

                            try {

                                // add the new entry
                                model.getContexts().add(inboxContext);
                            } finally {

                                // release the lock
                                model.getContexts().getReadWriteLock().writeLock().unlock();
                            }
                        }
                    }

                    /**
                     * Called when a new inboxContext is updated.
                     *
                     * @param inboxContext InboxContext that was updated
                     */
                    public void inboxContextUpdated(InboxContext inboxContext) {

                        EventList contexts = model.getContexts();

                        Iterator iterator = contexts.listIterator();
                        while (iterator.hasNext()) {

                            InboxContext inboxContextThis = (InboxContext) iterator.next();

                            if (inboxContextThis.getId().equals(inboxContext.getId())) {

                                int index = contexts.indexOf(inboxContextThis);

                                // lock for writing
                                model.getContexts().getReadWriteLock().writeLock().lock();

                                try {

                                    // remove the old entry and add the new one
                                    contexts.remove(index);
                                    contexts.add(index, inboxContext);
                                } finally {

                                    // release the lock
                                    model.getContexts().getReadWriteLock().writeLock().unlock();
                                }

                                break;
                            }
                        }
                    }

                    /**
                     * Called when an error occurs.
                     *
                     * @param exception The exception that occurred
                     */
                    public void exceptionOccurred(GTDInboxException exception) {

                        handleErrorOccurred(exception);
                    }
                });

        ApplicationManager.getInstance().getFrameManager().getCategoryFrame().addCategoryFormListener(
                new CategoryFormListener() {


                    /**
                     * Called when a new Category is added.
                     *
                     * @param category Category that was added
                     */
                    public void categoryAdded(final Category category) {

                        boolean addCategory = true;
                        Iterator iterator = model.getCategories().listIterator();
                        while (iterator.hasNext()) {

                            if (((Category) iterator.next()).getId().equals(category.getId())) {

                                addCategory = false;

                                break;
                            }
                        }
                        if (addCategory) {

                            // lock for writing
                            model.getCategories().getReadWriteLock().writeLock().lock();

                            try {

                                // remove the entry
                                model.getCategories().add(category);
                            } finally {

                                // release the lock
                                model.getCategories().getReadWriteLock().writeLock().unlock();
                            }
                        }
                    }

                    /**
                     * Called when a new Category is updated.
                     *
                     * @param category Category that was updated
                     */
                    public void categoryUpdated(Category category) {

                        EventList categories = model.getCategories();

                        Iterator iterator = categories.listIterator();
                        while (iterator.hasNext()) {

                            Category categoryThis = (Category) iterator.next();

                            if (categoryThis.getId().equals(category.getId())) {

                                int index = categories.indexOf(categoryThis);

                                // lock for writing
                                model.getCategories().getReadWriteLock().writeLock().lock();

                                try {

                                    // remove the old entry and add the new one
                                    categories.remove(index);
                                    categories.add(index, category);
                                } finally {

                                    // release the lock
                                    model.getCategories().getReadWriteLock().writeLock().unlock();
                                }

                                break;
                            }
                        }
                    }

                    /**
                     * Called when an error occurs.
                     *
                     * @param exception The exception that occurred
                     */
                    public void exceptionOccurred(GTDInboxException exception) {

                        handleErrorOccurred(exception);
                    }
                });

        ApplicationManager.getInstance().getFrameManager().getActionItemFrame().addActionItemFormListener(
                new ActionItemFormListener() {

                    /**
                     * Called when an ActionItem is added.
                     *
                     * @param actionItem ActionItem added
                     */
                    public void actionItemAdded(ActionItem actionItem) {

                        if (actionItem == null) {
                            return;
                        }

                        if (detailProjectPanel.getProject() != null && actionItem.getProject() != null
                                && actionItem.getProject().getId().equals(detailProjectPanel.getProject().getId())) {

                            try {
                                detailProjectPanel.addActionItem(actionItem);
                            } catch (DataBaseManagerException e) {
                                handleErrorOccurred(e);
                            }
                        } else if (detailContextPanel.getContext() != null && actionItem.getInboxContext() != null
                                && actionItem.getInboxContext().getId().equals(detailContextPanel.getContext().getId())) {

                            try {
                                detailContextPanel.addActionItem(actionItem);
                            } catch (DataBaseManagerException e) {
                                handleErrorOccurred(e);
                            }
                        }
                    }

                    /**
                     * Called when an ActionItem is updated.
                     *
                     * @param actionItem ActionItem updated
                     */
                    public void actionItemUpdated(ActionItem actionItem) {

                        if (actionItem == null) {
                            return;
                        }

                        logger.debug("ActionItem " + actionItem.getId() + " updated");
                        if (panelDetail instanceof DetailProjectPanel) {

                            DetailProjectPanel detailProjectPanelThis = (DetailProjectPanel) panelDetail;

                            if (actionItem.getProject().getId().equals(detailProjectPanelThis.getProject().getId())) {
                                detailProjectPanelThis.updateActionItem(actionItem);
                            } else {
                                detailProjectPanelThis.removeActionItem(actionItem);
                            }
                        } else if (panelDetail instanceof DetailContextPanel) {

                            DetailContextPanel detailContextPanelThis = (DetailContextPanel) panelDetail;

                            if (actionItem.getInboxContext().getId().equals(detailContextPanelThis.getContext().getId())) {
                                detailContextPanelThis.updateActionItem(actionItem);
                            } else {
                                detailContextPanelThis.removeActionItem(actionItem);
                            }
                        } else if (panelDetail instanceof DetailArchivePanel) {

                            detailArchivePanel.updateActionItem(actionItem);
                        }
                    }

                    /**
                     * Called when an error occurs.
                     *
                     * @param exception The exception that occurred
                     */
                    public void exceptionOccurred(GTDInboxException exception) {

                        handleException(exception);
                    }
                });

        model.addPropertyChangeListener("selectedTreeNode", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

                if (panelDetail instanceof SearchResultDetailPanel) {

                    textFieldSearch.setText(null);
                }

                GlazedTreeNode treeNode = (GlazedTreeNode) propertyChangeEvent.getNewValue();
                logger.debug(treeNode.getUserObject().getClass());

                if (treeNode.getUserObject() instanceof Project
                        || treeNode.getUserObject() instanceof Category
                        || treeNode.getUserObject() instanceof InboxContext) {

                    buttonDelete.setEnabled(true);
                } else {
                    buttonDelete.setEnabled(false);
                }

                if (treeNode.getUserObject() instanceof Project) {

                    buttonAddAction.setEnabled(true);
                } else {
                    buttonAddAction.setEnabled(false);
                }

                /*if( treeNode.getUserObject() instanceof Category ) {

                    buttonAddReference.setEnabled( true );
                } else {
                    buttonAddReference.setEnabled( false );
                }*/
            }
        });

        detailActionItemListListenerProject = new DetailActionItemListListener() {

            public void selectionChanged() {

                panelDetailSelectedItem = detailProjectPanel.getDetailActionItemList().getSelectedActionItem();

                fireSelectedDetailItemChanged();
            }

            public void selectedItemDoubleClicked() {

                if (panelDetailSelectedItem != null && (panelDetailSelectedItem instanceof ActionItem)) {

                    doUpdateActionItem((ActionItem) panelDetailSelectedItem);
                }
            }

            public void componentSizeChanged() {

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {

                        scrollPaneContent.invalidate();
                        revalidate();
                    }
                });
            }
        };

        detailActionItemListListenerContext = new DetailActionItemListListener() {

            public void selectionChanged() {

                panelDetailSelectedItem = detailContextPanel.getDetailActionItemList().getSelectedActionItem();

                fireSelectedDetailItemChanged();
            }

            public void selectedItemDoubleClicked() {

                if (panelDetailSelectedItem != null && (panelDetailSelectedItem instanceof ActionItem)) {

                    doUpdateActionItem((ActionItem) panelDetailSelectedItem);
                }
            }


            public void componentSizeChanged() {

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {

                        scrollPaneContent.invalidate();
                        revalidate();
                    }
                });
            }
        };

        detailActionItemListListenerArchive = new DetailActionItemListListener() {

            public void selectionChanged() {

                panelDetailSelectedItem = detailArchivePanel.getDetailActionItemList().getSelectedActionItem();

                fireSelectedDetailItemChanged();
            }

            public void selectedItemDoubleClicked() {

                if (panelDetailSelectedItem != null && (panelDetailSelectedItem instanceof ActionItem)) {

                    doUpdateActionItem((ActionItem) panelDetailSelectedItem);
                }
            }

            public void componentSizeChanged() {

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {

                        scrollPaneContent.invalidate();
                        revalidate();
                    }
                });
            }
        };

        textFieldSearch.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent caretEvent) {

                // update the model
                model.setSearchText(textFieldSearch.getText());

                // restart the timer
                timerSearch.restart();
            }
        });

        buttonClearSearch.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                textFieldSearch.setText(null);
                model.setSearchText(null);

                timerSearch.stop();

                doClearSearch();
            }
        });

        IndexManager.getInstance().addIndexManagerListener(new IndexManagerListener() {

            public void documentAdded(Document document) {

                handleIndexUpdate();
            }

            public void documentRemoved(Document document) {

                handleIndexUpdate();
            }

            private void handleIndexUpdate() {

                if (querySearch != null) {

                    try {
                        doSearch();
                    } catch (GTDInboxException e) {
                        handleException(e);
                    }
                }
            }
        });
    }

    private void clearDetailPanel() {

        scrollPaneContent.getViewport().setView(panelEmpty);
        scrollPaneContent.validate();

        // @todo: do something more sensible here

        panelDetail = panelEmpty;

        // clear the selected item
        panelDetailSelectedItem = null;

        // remove listeners form the panels
        detailProjectPanel.getDetailActionItemList().removeDetailActionItemListListener(
                detailActionItemListListenerProject);
        detailContextPanel.getDetailActionItemList().removeDetailActionItemListListener(
                detailActionItemListListenerContext);

        fireDetailPanelChanged();
    }

    private void handleErrorOccurred(GTDInboxException exception) {

        JOptionPane pane = new JOptionPane(
                "<html>" + UIManager.getString("OptionPane.css") +
                        "<b>There was a problem that I wasn't expecting.</b><p>" +
                        exception.getMessage(),
                JOptionPane.WARNING_MESSAGE
        );
        Object[] options = {"Okay"};
        pane.setOptions(options);
        pane.setInitialValue(options[0]);
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer(0)
        );
        JSheet.showSheet(pane, this, new SheetListener() {
            public void optionSelected(SheetEvent evt) {
                evt.getValue();
            }
        });
    }

    /**
     * Generates our view elements and backs them with the data model
     */
    private void generateViewFromModel() {

        // setup our root node
        GlazedTreeNode nodeRoot = new GlazedTreeNode();

        GlazedTreeNode nodeProjects = new GlazedTreeNode(TopLevelContainer.PROJECTS, model.getProjects());
        nodeRoot.insert(nodeProjects, 0);

        GlazedTreeNode nodeContexts = new GlazedTreeNode(TopLevelContainer.CONTEXTS, model.getContexts());
        nodeRoot.insert(nodeContexts, 1);

        /*GlazedTreeNode nodeCategories = new GlazedTreeNode( TopLevelContainer.CATEGORIES, model.getCategories() );
        nodeRoot.insert( nodeCategories, 2 );*/

        // we don't display a tree for the archive
        GlazedTreeNode nodeArchive = new GlazedTreeNode(TopLevelContainer.ARCHIVE, false);
        nodeRoot.insert(nodeArchive, 2);

        // we don't display a tree for the trash
        GlazedTreeNode nodeTrash = new GlazedTreeNode(TopLevelContainer.TRASH, false);
        nodeRoot.insert(nodeTrash, 3);

        // create our tree model
        treeModel = new GlazedTreeModel(nodeRoot);

        // pass the nodes to our tree
        treeItems.setModel(treeModel);

        // set our custom cell renderer on the tree
        treeItems.setCellRenderer(new BrowserTreeCellRenderer());

        // open all nodes
        treeItems.expandPath(new TreePath(new TreeNode[]{nodeRoot, nodeProjects}));
        treeItems.expandPath(new TreePath(new TreeNode[]{nodeRoot, nodeContexts}));
        //treeItems.expandPath( new TreePath( new TreeNode[]{ nodeRoot, nodeCategories } ) );
    }

    /**
     * Sets up the panel.
     */
    private void initializePanel() {

        // set default gui properties
        panelMain.setBorder(BorderFactory.createEmptyBorder());
        splitPaneMain.setBorder(BorderFactory.createEmptyBorder());
        splitPaneMain.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));
        scrollPaneItemTree.setBorder(BorderFactory.createEmptyBorder());
        splitPaneMain.putClientProperty("Quaqua.SplitPane.style", "bar");
        //splitPaneMain.setDividerSize( 1 );
        treeItems.setRootVisible(false);
        treeItems.setShowsRootHandles(true);

        // make the search panel look like one big textfield
        Border fieldBorder = textFieldSearch.getBorder();
        textFieldSearch.setBorder(BorderFactory.createEmptyBorder());
        panelSearchField.setBorder(fieldBorder);
        panelSearchField.setOpaque(true);
        panelSearchField.setBackground(textFieldSearch.getBackground());
        buttonClearSearch.setBackground(textFieldSearch.getBackground());
        buttonSearchOptions.setBackground(textFieldSearch.getBackground());

        // pass data to the frames
        ApplicationManager.getInstance().getFrameManager().getActionItemFrame().setListContexts(
                model.getContexts());
        ApplicationManager.getInstance().getFrameManager().getActionItemFrame().setListProjects(
                model.getProjects());

        // setup toolbar icons
        toolBar.setBorder(BorderFactory.createEmptyBorder());
        //buttonAddProject.putClientProperty( "Quaqua.Button.style", "toolBarTab" );

        // set initial button states
        buttonAddAction.setEnabled(false);
        //buttonAddReference.setEnabled( false );
        buttonDelete.setEnabled(false);

        // setup the detail panels
        detailProjectPanel = new DetailProjectPanel(this);
        detailContextPanel = new DetailContextPanel(this);
        detailArchivePanel = new DetailArchivePanel(this);
        trashDetailController = new TrashDetailController(this);
        detailSearchPanel = new SearchResultDetailPanel(this);

        // add listener to the trash panel
        trashDetailController.addTrashDetailControllerListener(new TrashDetailControllerListener() {

            public void putAwayTrashable(Trashable trashable) {

                if (trashable instanceof Project) {

                    // lock for writing
                    model.getProjects().getReadWriteLock().writeLock().lock();

                    try {

                        // add the item
                        model.getProjects().add(trashable);
                    } finally {

                        // release the lock
                        model.getProjects().getReadWriteLock().writeLock().unlock();
                    }
                } else if (trashable instanceof InboxContext) {

                    // lock for writing
                    model.getContexts().getReadWriteLock().writeLock().lock();

                    try {

                        // add the item
                        model.getContexts().add(trashable);
                    } finally {

                        // release the lock
                        model.getContexts().getReadWriteLock().writeLock().unlock();
                    }
                } else if (trashable instanceof Category) {

                    // lock for writing
                    model.getCategories().getReadWriteLock().writeLock().lock();

                    try {

                        // add the item
                        model.getCategories().add(trashable);
                    } finally {

                        // release the lock
                        model.getCategories().getReadWriteLock().writeLock().unlock();
                    }
                }
            }


            public void confirmEmptyTrash(String message) {

                fireConfirmEmptyTrash(message);
            }
        });

//        detailSearchPanel.addSearchResultDetailPanelListener(new SearchResultDetailPanelListener() {
//
//            public void projectDoubleClicked(Project project) {
//
//                doUpdateProject(project);
//            }
//
//            public void actionItemDoubleClicked(ActionItem actionItem) {
//
//                doUpdateActionItem(actionItem);
//            }
//
//            public void inboxContextDoubleClicked(InboxContext inboxContext) {
//
//                doUpdateInboxContext(inboxContext);
//            }
//
//            public void categoryDoubleClicked(Category category) {
//
//                doUpdateCategory(category);
//            }
//
//            public void referenceItemDoubleClicked(ReferenceItem referenceItem) {
//
//                // do nothing
//            }
//        });

        scrollPaneContent.setBorder(BorderFactory.createEmptyBorder());

        clearDetailPanel();

        splitPaneMain.setDividerLocation(200);

        ApplicationManager.getInstance().getEventBus().register(this);
    }

    @Subscribe
    public void projectDoubleClicked(MessageActionEvent<Project> event) {
        if (event.getAction().equals("DOUBLE_CLICKED")) {
            doUpdateProject(event.getInstance());
        }
    }

    @Subscribe
    public void actionItemDoubleClicked(MessageActionEvent<ActionItem> event) {
        if (event.getAction().equals("DOUBLE_CLICKED")) {
            doUpdateActionItem(event.getInstance());
        }
    }

    @Subscribe
    public void inboxContextDoubleClicked(MessageActionEvent<InboxContext> event) {
        if (event.getAction().equals("DOUBLE_CLICKED")) {
            doUpdateInboxContext(event.getInstance());
        }
    }

    @Subscribe
    public void categoryDoubleClicked(MessageActionEvent<Category> event) {
        if (event.getAction().equals("DOUBLE_CLICKED")) {
            doUpdateCategory(event.getInstance());
        }
    }

    @Subscribe
    public void referenceItemDoubleClicked(MessageActionEvent<ReferenceItem> event) {
        if (event.getAction().equals("DOUBLE_CLICKED")) {
            // do nothing
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 14, 0), -1, 0));
        panelMain.setMaximumSize(new Dimension(-1, -1));
        panelMain.setMinimumSize(new Dimension(640, 480));
        panelMain.setPreferredSize(new Dimension(800, 600));
        panelMiddle = new JPanel();
        panelMiddle.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panelMiddle, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        splitPaneMain = new JSplitPane();
        splitPaneMain.setContinuousLayout(true);
        splitPaneMain.setDividerLocation(200);
        panelMiddle.add(splitPaneMain, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scrollPaneItemTree = new JScrollPane();
        scrollPaneItemTree.setBackground(new Color(-1));
        scrollPaneItemTree.setHorizontalScrollBarPolicy(30);
        scrollPaneItemTree.setMaximumSize(new Dimension(-1, -1));
        scrollPaneItemTree.setMinimumSize(new Dimension(200, -1));
        scrollPaneItemTree.setOpaque(true);
        scrollPaneItemTree.setPreferredSize(new Dimension(-1, -1));
        scrollPaneItemTree.setVerticalScrollBarPolicy(20);
        splitPaneMain.setLeftComponent(scrollPaneItemTree);
        treeItems = new JTree();
        treeItems.setBackground(new Color(-1708553));
        treeItems.setEnabled(true);
        treeItems.setOpaque(true);
        treeItems.setRootVisible(true);
        treeItems.setShowsRootHandles(false);
        treeItems.putClientProperty("JTree.lineStyle", "");
        treeItems.putClientProperty("html.disable", Boolean.FALSE);
        scrollPaneItemTree.setViewportView(treeItems);
        scrollPaneContent = new JScrollPane();
        scrollPaneContent.setHorizontalScrollBarPolicy(30);
        scrollPaneContent.setMaximumSize(new Dimension(-1, -1));
        scrollPaneContent.setMinimumSize(new Dimension(400, -1));
        scrollPaneContent.setVerticalScrollBarPolicy(22);
        splitPaneMain.setRightComponent(scrollPaneContent);
        panelToolBar = new JPanel();
        panelToolBar.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(5, 5, 5, 5), -1, -1));
        panelMain.add(panelToolBar, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        toolBar = new JToolBar();
        toolBar.setBorderPainted(false);
        toolBar.setFloatable(false);
        toolBar.setMargin(new Insets(0, 0, 0, 0));
        toolBar.setOpaque(false);
        toolBar.setRollover(false);
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.FALSE);
        panelToolBar.add(toolBar, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelSearch = new JPanel();
        panelSearch.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        toolBar.add(panelSearch);
        panelSearchField = new JPanel();
        panelSearchField.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(3, 3, 3, 3), 3, 0));
        panelSearch.add(panelSearchField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_EAST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        textFieldSearch = new JTextField();
        panelSearchField.add(textFieldSearch, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        buttonClearSearch = new JButton();
        buttonClearSearch.setBorderPainted(false);
        buttonClearSearch.setContentAreaFilled(false);
        buttonClearSearch.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/clear-16.png")));
        buttonClearSearch.setText("");
        panelSearchField.add(buttonClearSearch, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), new Dimension(16, 16), 0, false));
        buttonSearchOptions = new JButton();
        buttonSearchOptions.setBorderPainted(false);
        buttonSearchOptions.setContentAreaFilled(false);
        buttonSearchOptions.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/magnifyingGlass.png")));
        buttonSearchOptions.setText("");
        panelSearchField.add(buttonSearchOptions, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), new Dimension(16, 16), 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 5, new Insets(0, 0, 0, 0), 15, -1));
        panelToolBar.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonAddContext = new JButton();
        buttonAddContext.setBorderPainted(false);
        buttonAddContext.setContentAreaFilled(false);
        Font buttonAddContextFont = this.$$$getFont$$$(null, -1, 11, buttonAddContext.getFont());
        if (buttonAddContextFont != null) buttonAddContext.setFont(buttonAddContextFont);
        buttonAddContext.setHorizontalAlignment(0);
        buttonAddContext.setHorizontalTextPosition(0);
        buttonAddContext.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/context-add-32.png")));
        buttonAddContext.setOpaque(false);
        buttonAddContext.setText("");
        buttonAddContext.setToolTipText("Add a Context");
        buttonAddContext.setVerticalAlignment(0);
        buttonAddContext.setVerticalTextPosition(3);
        panel1.add(buttonAddContext, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(32, 32), new Dimension(32, 32), new Dimension(32, 32), 0, false));
        buttonAddProject = new JButton();
        buttonAddProject.setBorderPainted(false);
        buttonAddProject.setContentAreaFilled(false);
        Font buttonAddProjectFont = this.$$$getFont$$$(null, -1, 11, buttonAddProject.getFont());
        if (buttonAddProjectFont != null) buttonAddProject.setFont(buttonAddProjectFont);
        buttonAddProject.setHorizontalAlignment(0);
        buttonAddProject.setHorizontalTextPosition(0);
        buttonAddProject.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/project-add-32.png")));
        buttonAddProject.setOpaque(false);
        buttonAddProject.setText("");
        buttonAddProject.setToolTipText("Add a Project");
        buttonAddProject.setVerticalAlignment(0);
        buttonAddProject.setVerticalTextPosition(3);
        panel1.add(buttonAddProject, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(32, 32), new Dimension(32, 32), new Dimension(32, 32), 0, false));
        seperator = new JSeparator();
        seperator.setOrientation(1);
        panel1.add(seperator, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 32), null, 0, false));
        buttonDelete = new JButton();
        buttonDelete.setBorderPainted(false);
        buttonDelete.setContentAreaFilled(false);
        buttonDelete.setDisabledIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/trash-disabled-32.png")));
        Font buttonDeleteFont = this.$$$getFont$$$(null, -1, 11, buttonDelete.getFont());
        if (buttonDeleteFont != null) buttonDelete.setFont(buttonDeleteFont);
        buttonDelete.setHorizontalAlignment(0);
        buttonDelete.setHorizontalTextPosition(0);
        buttonDelete.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/trash-32.png")));
        buttonDelete.setOpaque(false);
        buttonDelete.setText("");
        buttonDelete.setToolTipText("Move the Item to the Trash");
        buttonDelete.setVerticalAlignment(0);
        buttonDelete.setVerticalTextPosition(3);
        panel1.add(buttonDelete, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(32, 32), new Dimension(32, 32), new Dimension(32, 32), 0, false));
        buttonAddAction = new JButton();
        buttonAddAction.setBorderPainted(false);
        buttonAddAction.setContentAreaFilled(false);
        buttonAddAction.setDisabledIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/action-add-disabled-32.png")));
        Font buttonAddActionFont = this.$$$getFont$$$(null, -1, 11, buttonAddAction.getFont());
        if (buttonAddActionFont != null) buttonAddAction.setFont(buttonAddActionFont);
        buttonAddAction.setHorizontalAlignment(0);
        buttonAddAction.setHorizontalTextPosition(0);
        buttonAddAction.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/action-add-32.png")));
        buttonAddAction.setMargin(new Insets(0, 0, 0, 0));
        buttonAddAction.setOpaque(false);
        buttonAddAction.setText("");
        buttonAddAction.setToolTipText("Add an Action Item");
        buttonAddAction.setVerifyInputWhenFocusTarget(false);
        buttonAddAction.setVerticalAlignment(0);
        buttonAddAction.setVerticalTextPosition(3);
        panel1.add(buttonAddAction, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(32, 32), new Dimension(32, 32), new Dimension(32, 32), 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
