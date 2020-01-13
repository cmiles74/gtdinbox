package com.nervestaple.gtdinbox.gui.browser.detail;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.datastore.DataStoreException;
import com.nervestaple.gtdinbox.datastore.DataStoreManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.gui.GTDInboxExceptionHandler;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import com.nervestaple.gtdinbox.utility.texttransformer.OutputType;
import com.nervestaple.gtdinbox.utility.texttransformer.UtilityTextTransformer;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;

/**
 * Provides a detail view of archived (completed) items.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class DetailArchivePanel extends JPanel implements GTDInboxExceptionHandler {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * ActionItem objects being displayed.
     */
    private EventList listItems;

    /**
     * Exception handler.
     */
    private GTDInboxExceptionHandler gtdInboxExceptionHandler;

    /**
     * Action item list for displaying the items.
     */
    private DetailActionItemList detailActionItemList;

    /**
     * Defaut padding between gui form objects.
     */
    private final int GUI_LAYOUT_PADDING = 10;

    // gui form objects
    private JPanel panelMain;
    private JViewport viewportDescription;
    private JViewport viewportTitle;
    private JLabel labelTypeIcon;
    private JPanel panelActionItems;
    private JPanel panelFiller;
    private JTextPane textPaneDescription;
    private JTextPane textPaneTitle;

    /**
     * Creates a new DetailArchivePanel.
     *
     * @param gtdInboxExceptionHandler Exception to handle
     */
    public DetailArchivePanel(GTDInboxExceptionHandler gtdInboxExceptionHandler) {

        super();

        this.gtdInboxExceptionHandler = gtdInboxExceptionHandler;

        detailActionItemList = new DetailActionItemList(gtdInboxExceptionHandler);

        setupPanel();
    }

    public void addActionItem(ActionItem actionItem) throws DataBaseManagerException {

        detailActionItemList.addActionItem(actionItem);
    }

    public void updateActionItem(final ActionItem actionItem) {

        detailActionItemList.updateActionItem(actionItem);
    }

    public void removeActionItem(ActionItem actionItem) {

        detailActionItemList.removeActionItem(actionItem);
    }

    public void clearSelection() {

        detailActionItemList.clearSelection();
    }

    // exception handler methods

    /**
     * Called when an exception from a child component occurs.
     *
     * @param exception Exception that occurred
     */
    public void handleException(GTDInboxException exception) {

        gtdInboxExceptionHandler.handleException(exception);
    }

    // accessor and mutator methods

    public EventList getActionItems() {
        return listItems;
    }

    /**
     * Returns the preferred size of this panelMain.
     *
     * @return Dimension with the size
     */
    public Dimension getPreferredSize() {

        // get the height of the whole panelMain
        double height = 0;
        height += viewportTitle.getSize().getHeight();
        height += GUI_LAYOUT_PADDING;
        height += viewportDescription.getSize().getHeight();
        height += GUI_LAYOUT_PADDING;
        height += panelActionItems.getSize().getHeight();
        double width = panelActionItems.getPreferredSize().getWidth();

        // subtract the height of the filler panelMain
        //height -= panelFiller.getSize().getHeight(); // this seems to be messing up (more) on fast resizing

        return (new Dimension((new Double(width)).intValue(), (new Double(height)).intValue()));
    }

    public DetailActionItemList getDetailActionItemList() {
        return detailActionItemList;
    }

    /**
     * Updates the view of archived items
     *
     * @throws DataBaseManagerException Problems loading item data
     */
    public void loadData() throws DataBaseManagerException {

        try {

            Collection collection = DataStoreManager.getArchive();
            listItems = new BasicEventList();
            listItems.addAll(collection);
        } catch (DataStoreException e) {
            handleException(e);
        }

        updateView();
    }

    // private methods

    private void clearPanel() {

        detailActionItemList.clearList();
    }

    /**
     * Updates the view.
     *
     * @throws DataBaseManagerException Problems loading item data
     */
    private void updateView() throws DataBaseManagerException {

        clearPanel();

        // update the action item panel
        detailActionItemList.setActionItems(listItems, false);
    }

    private void setupPanel() {

        textPaneTitle = new JTextPane();
        textPaneTitle.setEditable(false);
        viewportTitle.setLayout(new BorderLayout());
        viewportTitle.setView(textPaneTitle);

        textPaneDescription = new JTextPane();
        textPaneDescription.setEditable(false);
        viewportDescription.setLayout(new BorderLayout());
        viewportDescription.setView(textPaneDescription);

        textPaneTitle.setContentType("text/html");
        textPaneDescription.setContentType("text/html");

        textPaneTitle.setText(
                "<html>" + UtilityTextTransformer.STYLESHEET +
                        UtilityTextTransformer.transformTextForDisplay("Completed Items",
                                TextStyleType.MARKDOWN_TEXT, OutputType.TITLE) + "</html>");

        textPaneDescription.setText(UtilityTextTransformer.transformTextForDisplay(
                "These are items that have been completed and archived.",
                TextStyleType.MARKDOWN_TEXT, OutputType.DESCRIPTION));

        UtilitiesGui.addHyperLinkListener(textPaneTitle);
        UtilitiesGui.addHyperLinkListener(textPaneDescription);

        listItems = new BasicEventList();

        panelActionItems.setLayout(new GridLayout(1, 1));
        panelActionItems.add(detailActionItemList);

        detailActionItemList.addDetailActionItemListListener(new DetailActionItemListListener() {

            public void selectionChanged() {
                // do nothing
            }

            public void selectedItemDoubleClicked() {
                // do nothing
            }

            public void componentSizeChanged() {

                logger.debug("componentSizeChanged");
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {

                        panelMain.invalidate();
                        revalidate();
                    }
                });
            }
        });

        setLayout(new BorderLayout());
        add(panelMain);

        // listen for changes to the archive days value
        ConfigurationFactory.getInstance().getApplicationConfiguration().addPropertyChangeListener("archiveDays",
                new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

                        // reload our data
                        try {
                            loadData();
                        } catch (DataBaseManagerException e) {
                            handleException(e);
                        }
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
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.setBackground(new Color(-1));
        panelMain.setOpaque(true);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(5, 5, 0, 5), -1, -1));
        panel1.setOpaque(false);
        panelMain.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        viewportDescription = new JViewport();
        viewportDescription.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(viewportDescription, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        viewportTitle = new JViewport();
        viewportTitle.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(viewportTitle, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelTypeIcon = new JLabel();
        labelTypeIcon.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/archive-32.png")));
        labelTypeIcon.setText("");
        panel1.add(labelTypeIcon, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelActionItems = new JPanel();
        panelActionItems.setLayout(new BorderLayout(0, 0));
        panelActionItems.setAutoscrolls(false);
        panelActionItems.setOpaque(false);
        panelMain.add(panelActionItems, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelFiller = new JPanel();
        panelFiller.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelFiller.setOpaque(false);
        panelMain.add(panelFiller, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
