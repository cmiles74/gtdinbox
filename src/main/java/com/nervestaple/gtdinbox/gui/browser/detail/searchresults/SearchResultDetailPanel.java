package com.nervestaple.gtdinbox.gui.browser.detail.searchresults;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.gui.ApplicationManager;
import com.nervestaple.gtdinbox.gui.GTDInboxExceptionHandler;
import com.nervestaple.gtdinbox.gui.event.action.MessageAction;
import com.nervestaple.gtdinbox.gui.event.action.MessageActionEvent;
import com.nervestaple.gtdinbox.gui.utility.SimpleDateCellRenderer;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import com.nervestaple.gtdinbox.utility.comparator.LuceneComparator;
import com.nervestaple.gtdinbox.utility.texttransformer.OutputType;
import com.nervestaple.gtdinbox.utility.texttransformer.UtilityTextTransformer;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Provides the form for the search result detail view.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class SearchResultDetailPanel extends JPanel {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Data model for the form.
     */
    private SearchResultDetailModel model;

    /**
     * Exception handler.
     */
    private GTDInboxExceptionHandler gtdInboxExceptionHandler;

    /**
     * Date format for the results.
     */
    private final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    // gui form objects
    private JPanel panelMain;
    private JLabel labelIcon;
    private JLabel labelDescription;
    private JLabel labelTitle;
    private JScrollPane scrollPaneResults;
    private JTable tableResults;
    private JPanel panelHeader;

    /**
     * Creates a new SearchResultDetailPanel.
     *
     * @param gtdInboxExceptionHandler handler for exceptions
     */
    public SearchResultDetailPanel(GTDInboxExceptionHandler gtdInboxExceptionHandler) {

        super();

        this.gtdInboxExceptionHandler = gtdInboxExceptionHandler;

        this.model = new SearchResultDetailModel();

        setLayout(new BorderLayout());
        add(panelMain);

        initializeForm();
        initializeModelListeners();
        initializeFormListeners();
    }

    // accessor and mutator methods

    public SearchResultDetailModel getModel() {
        return model;
    }

    /**
     * Returns the preferred size of this panel.
     *
     * @return Dimension with the size
     */
    public Dimension getPreferredSize() {

        // get the height of the whole panel
        double height = 0;
        height += panelHeader.getSize().getHeight();
        height += tableResults.getTableHeader().getSize().getHeight();
        height += tableResults.getRowHeight() * tableResults.getModel().getRowCount();
        double width = panelMain.getPreferredSize().getWidth();

        return (new Dimension((new Double(width)).intValue(), (new Double(height)).intValue()));
    }

    // private methods

    private void fireProjectDoubleClicked(Project project) {
        ApplicationManager.getInstance().getEventBus().post(
                new MessageActionEvent<Project>(project, MessageAction.DOUBLE_CLICK));
    }

    private void fireActionItemDoubleClicked(ActionItem actionItem) {
        ApplicationManager.getInstance().getEventBus().post(
                new MessageActionEvent<ActionItem>(actionItem, MessageAction.DOUBLE_CLICK));
    }

    private void fireInboxContextDoubleClicked(InboxContext inboxContext) {
        ApplicationManager.getInstance().getEventBus().post(
                new MessageActionEvent<InboxContext>(inboxContext, MessageAction.DOUBLE_CLICK));
    }

    private void fireCategoryDoubleClicked(Category category) {
        ApplicationManager.getInstance().getEventBus().post(
                new MessageActionEvent<Category>(category, MessageAction.DOUBLE_CLICK));
    }

    private void fireReferenceItemDoubleClicked(ReferenceItem referenceItem) {
        ApplicationManager.getInstance().getEventBus().post(
                new MessageActionEvent<ReferenceItem>(referenceItem, MessageAction.DOUBLE_CLICK));
    }

    private void updateItem() {

        int selectedRow = tableResults.getSelectedRow();

        if (selectedRow < 0) {

            return;
        }

        Document document = (Document) ((EventTableModel) tableResults.getModel()).getElementAt(selectedRow);

        String className = document.get("class");
        logger.debug("Editing item of type " + className);

        if (className == null) {
            return;
        } else if (className.equals("com.nervestaple.gtdinbox.model.item.actionitem.actionitem")) {

            logger.debug("Double clicked an ActionItem");
            Object object = getObjectFromDatabase(ActionItem.class, document.get("id"));
            if (object != null) {
                fireActionItemDoubleClicked((ActionItem) object);
            }
        } else if (className.equals("com.nervestaple.gtdinbox.model.inboxcontext.inboxcontext")) {

            logger.debug("Double clicked an InboxContext");
            Object object = getObjectFromDatabase(InboxContext.class, document.get("id"));
            if (object != null) {
                fireInboxContextDoubleClicked((InboxContext) object);
            }
        } else if (className.equals("com.nervestaple.gtdinbox.model.project.project")) {

            logger.debug("Double clicked a Project");
            Object object = getObjectFromDatabase(Project.class, document.get("id"));
            if (object != null) {
                fireProjectDoubleClicked((Project) object);
            }
        } else if (className.equals("com.nervestaple.gtdinbox.model.item.reference.category.category")) {

            logger.debug("Double clicked a Category");
            Object object = getObjectFromDatabase(Category.class, document.get("id"));
            if (object != null) {
                fireCategoryDoubleClicked((Category) object);
            }
        } else if (className.equals("com.nervestaple.gtdinbox.model.item.referenceitem.referenceitem")) {

            logger.debug("Double clicked an ReferenceItem");
            Object object = getObjectFromDatabase(ReferenceItem.class, document.get("id"));
            if (object != null) {
                fireReferenceItemDoubleClicked((ReferenceItem) object);
            }
        }
    }

    private Object getObjectFromDatabase(Class clazz, String id) {

        if (clazz == null || id == null) {
            return (null);
        }

        // parse out the id
        Long longId = new Long(Long.parseLong(id));

        // the retrieved object
        Object object = null;

        try {

            // retrieve the object
            object = DataBaseManager.getInstance().getEntityManager().find(clazz, longId);
        } catch (DataBaseManagerException e) {
            gtdInboxExceptionHandler.handleException(e);
        }

        return (object);
    }

    private void updateTable(final EventList<Document> listItems) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                SortedList<Document> sortedList = new SortedList<>(listItems, new LuceneComparator());
                tableResults.setModel(new EventTableModel<Document>(sortedList, new TableFormatSearchResult()));

                // hang on to the comparator chooser in case we need it later
                TableComparatorChooser<Document> tableComparatorChooser =
                        new TableComparatorChooser<>(tableResults, sortedList, true);

                tableResults.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

                tableResults.getColumnModel().getColumn(0).setMinWidth(80);
                tableResults.getColumnModel().getColumn(0).setPreferredWidth(80);
                tableResults.getColumnModel().getColumn(0).setMaxWidth(80);

                tableResults.getColumnModel().getColumn(1).setMinWidth(125);
                tableResults.getColumnModel().getColumn(1).setPreferredWidth(125);
                tableResults.getColumnModel().getColumn(1).setMaxWidth(125);

                tableResults.getColumnModel().getColumn(2).setMinWidth(80);
                tableResults.getColumnModel().getColumn(2).setPreferredWidth(80);
                tableResults.getColumnModel().getColumn(2).setMaxWidth(80);

                tableResults.getColumnModel().getColumn(2).setCellRenderer(new SimpleDateCellRenderer(dateFormat));
            }
        });
    }

    private void initializeFormListeners() {

        tableResults.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent listSelectionEvent) {

                // clear the current selection
                model.getSelectedItems().clear();

                int[] selectedRows = tableResults.getSelectedRows();
                List selectedItems = new ArrayList();
                for (int index = 0; index < selectedRows.length; index++) {

                    selectedItems.add(model.getListItems().get(index));
                }

                model.setSelectedItems(selectedItems);
            }
        });
    }

    private void initializeModelListeners() {

        model.addPropertyChangeListener("listItems", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

                updateTable((EventList<Document>) propertyChangeEvent.getNewValue());
            }
        });

        model.addPropertyChangeListener("description", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent event) {

                labelDescription
                        .setText("<html>" + UtilityTextTransformer.STYLESHEET +
                                UtilityTextTransformer.transformTextForDisplay((String) event.getNewValue(),
                                        TextStyleType.PLAIN_TEXT, OutputType.SUBTITLE) + "</html>");
            }
        });

        tableResults.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent event) {

                if (event.getClickCount() == 2) {

                    updateItem();
                }
            }
        });
    }

    private void initializeForm() {

        labelTitle.setText("<html></html>");
        labelDescription.setText("<html></html>");
        scrollPaneResults.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                labelTitle.setText("<html>" + UtilityTextTransformer.STYLESHEET +
                        UtilityTextTransformer.transformTextForDisplay(model.getName(),
                                TextStyleType.MARKDOWN_TEXT, OutputType.TITLE) + "</html>");

                updateTable(model.getListItems());

                tableResults.putClientProperty("Quaqua.Table.style", "striped");
            }
        });
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
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.setBackground(new Color(-1));
        panelHeader = new JPanel();
        panelHeader.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(5, 5, 5, 5), -1, -1));
        panelHeader.setOpaque(false);
        panelMain.add(panelHeader, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelIcon = new JLabel();
        labelIcon.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/search-32.png")));
        labelIcon.setInheritsPopupMenu(false);
        labelIcon.setText("");
        panelHeader.add(labelIcon, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setOpaque(false);
        panelHeader.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, 0));
        panel2.setOpaque(false);
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelDescription = new JLabel();
        labelDescription.setBackground(new Color(-1));
        labelDescription.setFocusable(true);
        Font labelDescriptionFont = this.$$$getFont$$$(null, -1, 11, labelDescription.getFont());
        if (labelDescriptionFont != null) labelDescription.setFont(labelDescriptionFont);
        labelDescription.setForeground(new Color(-6710887));
        labelDescription.setText("<html><i>Looking for...</i></htmll>");
        panel2.add(labelDescription, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTitle = new JLabel();
        labelTitle.setText("Search Results");
        panel2.add(labelTitle, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPaneResults = new JScrollPane();
        Font scrollPaneResultsFont = this.$$$getFont$$$(null, -1, -1, scrollPaneResults.getFont());
        if (scrollPaneResultsFont != null) scrollPaneResults.setFont(scrollPaneResultsFont);
        scrollPaneResults.setHorizontalScrollBarPolicy(31);
        scrollPaneResults.setVerticalScrollBarPolicy(21);
        panelMain.add(scrollPaneResults, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableResults = new JTable();
        Font tableResultsFont = this.$$$getFont$$$(null, -1, 11, tableResults.getFont());
        if (tableResultsFont != null) tableResults.setFont(tableResultsFont);
        scrollPaneResults.setViewportView(tableResults);
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
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
