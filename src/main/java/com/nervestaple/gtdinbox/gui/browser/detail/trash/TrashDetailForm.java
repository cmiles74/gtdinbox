package com.nervestaple.gtdinbox.gui.browser.detail.trash;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventTableModel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import com.nervestaple.gtdinbox.utility.texttransformer.OutputType;
import com.nervestaple.gtdinbox.utility.texttransformer.UtilityTextTransformer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the form for the TrashDetail form.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TrashDetailForm extends JPanel {

    /**
     * Model for the form.
     */
    private TrashDetailModel model;

    // gui form objects
    private JPanel panelMain;
    private JTable tableTrash;
    private JLabel labelIcon;
    private JButton buttonEmptyTrash;
    private JButton buttonPutAwayItems;
    private JScrollPane scrollPaneTrash;
    private JLabel labelDescription;
    private JLabel labelTitle;
    private JPanel panelHeader;

    /**
     * Creates a new TrashDetailForm.
     *
     * @param model
     */
    public TrashDetailForm(TrashDetailModel model) {

        super();

        this.model = model;

        setLayout(new BorderLayout());
        add(panelMain);

        initializeForm();
        initializeModelListeners();
        initializeFormListeners();
    }

    // accessor and mutator methods

    public TrashDetailModel getModel() {
        return model;
    }

    // other methods

    /**
     * Returns the preferred size of this panel.
     *
     * @return Dimension with the size
     */
    public Dimension getPreferredSize() {

        // get the height of the whole panel
        double height = 0;
        height += panelHeader.getSize().getHeight();
        height += tableTrash.getTableHeader().getSize().getHeight();
        height += tableTrash.getRowHeight() * tableTrash.getModel().getRowCount();
        double width = panelMain.getPreferredSize().getWidth();

        return (new Dimension((new Double(width)).intValue(), (new Double(height)).intValue()));
    }

    // private methods

    private void updateTable(final EventList listItems) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                tableTrash.setModel(new EventTableModel(listItems, new TableFormatTrashable()));

                tableTrash.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

                tableTrash.getColumnModel().getColumn(0).setMinWidth(80);
                tableTrash.getColumnModel().getColumn(0).setPreferredWidth(80);
                tableTrash.getColumnModel().getColumn(0).setMaxWidth(80);

                tableTrash.getColumnModel().getColumn(1).setMinWidth(125);
                tableTrash.getColumnModel().getColumn(1).setPreferredWidth(125);
                tableTrash.getColumnModel().getColumn(1).setMaxWidth(125);
            }
        });
    }

    private void initializeFormListeners() {

        buttonEmptyTrash.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (model.getActionListenerEmptyTrash() != null) {

                    model.getActionListenerEmptyTrash().actionPerformed(actionEvent);
                }
            }
        });

        buttonPutAwayItems.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (model.getActionListenerPutAway() != null) {

                    model.getActionListenerPutAway().actionPerformed(actionEvent);
                }
            }
        });

        tableTrash.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent listSelectionEvent) {

                // clear the current selection
                model.getSelectedItems().clear();

                int[] selectedRows = tableTrash.getSelectedRows();
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

                updateTable((EventList) propertyChangeEvent.getNewValue());
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

        model.addPropertyChangeListener("actionListenerEmptyTrash", new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent event) {

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {

                        buttonEmptyTrash.setEnabled(event.getNewValue() != null);
                    }
                });
            }
        });

        model.addPropertyChangeListener("actionListenerPutAway", new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent event) {

                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {

                        buttonPutAwayItems.setEnabled(event.getNewValue() != null);
                    }
                });
            }
        });
    }

    private void initializeForm() {

        labelTitle.setText("<html></html>");
        labelDescription.setText("<html></html>");
        scrollPaneTrash.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                labelTitle.setText("<html>" + UtilityTextTransformer.STYLESHEET +
                        UtilityTextTransformer.transformTextForDisplay(model.getName(),
                                TextStyleType.MARKDOWN_TEXT, OutputType.TITLE) + "</html>");

                updateTable(model.getListItems());

                tableTrash.putClientProperty("Quaqua.Table.style", "striped");
            }
        });
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
        panelMain.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.setBackground(new Color(-1));
        panelHeader = new JPanel();
        panelHeader.setLayout(new GridLayoutManager(1, 2, new Insets(5, 5, 5, 5), -1, -1));
        panelHeader.setOpaque(false);
        panelMain.add(panelHeader, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        labelIcon = new JLabel();
        labelIcon.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/trash-32.png")));
        labelIcon.setText("");
        panelHeader.add(labelIcon, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.setOpaque(false);
        panelHeader.add(panel1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(5, 0, 0, 0), -1, -1));
        panel2.setOpaque(false);
        panel1.add(panel2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonEmptyTrash = new JButton();
        buttonEmptyTrash.setEnabled(false);
        Font buttonEmptyTrashFont = this.$$$getFont$$$(null, -1, 11, buttonEmptyTrash.getFont());
        if (buttonEmptyTrashFont != null) buttonEmptyTrash.setFont(buttonEmptyTrashFont);
        buttonEmptyTrash.setOpaque(false);
        buttonEmptyTrash.setText("Empty the Trash...");
        panel2.add(buttonEmptyTrash, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonPutAwayItems = new JButton();
        buttonPutAwayItems.setEnabled(false);
        Font buttonPutAwayItemsFont = this.$$$getFont$$$(null, -1, 11, buttonPutAwayItems.getFont());
        if (buttonPutAwayItemsFont != null) buttonPutAwayItems.setFont(buttonPutAwayItemsFont);
        buttonPutAwayItems.setOpaque(false);
        buttonPutAwayItems.setText("Put Away Items");
        panel2.add(buttonPutAwayItems, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, 0));
        panel3.setOpaque(false);
        panel1.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelDescription = new JLabel();
        labelDescription.setBackground(new Color(-1));
        labelDescription.setFocusable(true);
        Font labelDescriptionFont = this.$$$getFont$$$(null, -1, 11, labelDescription.getFont());
        if (labelDescriptionFont != null) labelDescription.setFont(labelDescriptionFont);
        labelDescription.setForeground(new Color(-6710887));
        labelDescription.setText("<html><i>The trash is empty</i></htmll>");
        panel3.add(labelDescription, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTitle = new JLabel();
        labelTitle.setText("Trash");
        panel3.add(labelTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        scrollPaneTrash = new JScrollPane();
        Font scrollPaneTrashFont = this.$$$getFont$$$(null, -1, -1, scrollPaneTrash.getFont());
        if (scrollPaneTrashFont != null) scrollPaneTrash.setFont(scrollPaneTrashFont);
        scrollPaneTrash.setHorizontalScrollBarPolicy(31);
        scrollPaneTrash.setVerticalScrollBarPolicy(21);
        panelMain.add(scrollPaneTrash, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tableTrash = new JTable();
        Font tableTrashFont = this.$$$getFont$$$(null, -1, 11, tableTrash.getFont());
        if (tableTrashFont != null) tableTrash.setFont(tableTrashFont);
        scrollPaneTrash.setViewportView(tableTrash);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
