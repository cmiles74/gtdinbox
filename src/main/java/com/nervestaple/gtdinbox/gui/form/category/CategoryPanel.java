package com.nervestaple.gtdinbox.gui.form.category;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Provides a panel for editing a category.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class CategoryPanel extends JPanel {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * The Category being edited.
     */
    private Category category;

    /**
     * Set of listeners for this form.
     */
    private Set listeners;

    /**
     * Cancel button listener.
     */
    private ActionListener actionListenerCancel;

    // gui form objects
    private JLabel labelTitle;
    private JButton buttonOkay;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JTextArea textAreaDescription;
    private JLabel labelColor;
    private JButton buttonColorPicker;
    private JPanel panelColor;
    private JPanel panelMain;

    /**
     * Creates a new CategoryPanel.
     */
    public CategoryPanel() {

        super();

        initializePanel();
    }

    public void addNewCategory() {

        Category category = new Category();

        setTitle("Add a New Category...");

        updatePanel(category);

        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getCategory().setName(textFieldName.getText().trim());
                getCategory().setDescription(textAreaDescription.getText().trim());
                getCategory().setColor(panelColor.getBackground());

                try {

                    DataBaseManager.getInstance().beginTransaction();
                    DataBaseManager.getInstance().getEntityManager().persist(getCategory());
                    DataBaseManager.getInstance().commitTransaction();

                    fireCategoryAdded(getCategory());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }

                buttonOkay.removeActionListener(this);
            }
        });
    }

    public void updateCategory(Category category) throws GTDInboxException {

        // make sure we have current data
        DataBaseManager.getInstance().getEntityManager().persist(category);

        // save a reference to the project
        this.category = category;

        setTitle("Edit a Category...");

        // update the panel fields
        updatePanel(category);

        removeActionListenersFromButton(buttonOkay);
        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getCategory().setName(textFieldName.getText().trim());
                getCategory().setDescription(textAreaDescription.getText().trim());
                getCategory().setColor(panelColor.getBackground());

                try {

                    // get a session
                    EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();

                    // start a new transaction and save the project
                    DataBaseManager.getInstance().beginTransaction();
                    entityManager.persist(getCategory());

                    // commit the transaction and close our session
                    DataBaseManager.getInstance().commitTransaction();

                    fireCategoryUpdated(getCategory());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }

                buttonOkay.removeActionListener(this);
            }
        });
    }

    public void addCategoryFormListener(CategoryFormListener listener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeCategoryContextFormListener(CategoryFormListener listener) {

        listeners.remove(listener);
    }

    public ActionListener getActionListenerCancel() {
        return actionListenerCancel;
    }

    public void setActionListenerCancel(ActionListener actionListenerCancel) {
        this.actionListenerCancel = actionListenerCancel;
    }

    /**
     * Sets the text for the title label.
     *
     * @param title Title for the label
     */
    public void setTitle(final String title) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                labelTitle.setText(title);
            }
        });
    }

    public Category getCategory() {
        return category;
    }

    // private void

    private void removeActionListenersFromButton(JButton button) {

        ActionListener[] listeners = button.getActionListeners();
        for (int index = 0; index < listeners.length; index++) {
            button.removeActionListener(listeners[index]);
        }
    }

    private void updatePanel(final Category category) {

        clearPanel();

        this.category = category;

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                textFieldName.setText(category.getName());
                textAreaDescription.setText(category.getDescription());
                panelColor.setBackground(category.getColor());
            }
        });
    }

    private void fireCategoryAdded(final Category category) {

        CategoryFormListener[] listenersArray =
                (CategoryFormListener[]) listeners.toArray(new CategoryFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].categoryAdded(category);
        }
    }

    private void fireCategoryUpdated(final Category category) {

        CategoryFormListener[] listenersArray =
                (CategoryFormListener[]) listeners.toArray(new CategoryFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].categoryUpdated(category);
        }
    }

    private void fireErrorOccurred(final GTDInboxException exception) {

        CategoryFormListener[] listenersArray =
                (CategoryFormListener[]) listeners.toArray(new CategoryFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].exceptionOccurred(exception);
        }
    }

    private void setupListeners() {

        buttonColorPicker.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                Color color = JColorChooser.showDialog(panelMain, "Choose a Color", Color.WHITE);
                logger.debug("User chose color: " + color);

                panelColor.setBackground(color);
            }
        });
    }

    private void clearPanel() {

        category = null;

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                textFieldName.setText(null);
                textAreaDescription.setText(null);
                labelColor.setBackground(Color.WHITE);
            }
        });
    }

    private void initializePanel() {

        setLayout(new GridLayout(1, 1));
        add(panelMain);

        buttonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                actionListenerCancel.actionPerformed(actionEvent);

                clearPanel();
            }
        });

        setupListeners();

        listeners = new HashSet();
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
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(8, 5, 15, 5), -1, -1));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$(null, -1, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/category-32.png")));
        labelTitle.setText("Create/Edit a Category");
        panelMain.add(labelTitle, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("A Brief Description of the Category");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        scrollPane1.setVerticalScrollBarPolicy(22);
        panel1.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 75), null, 0, false));
        textAreaDescription = new JTextArea();
        Font textAreaDescriptionFont = this.$$$getFont$$$(null, -1, 11, textAreaDescription.getFont());
        if (textAreaDescriptionFont != null) textAreaDescription.setFont(textAreaDescriptionFont);
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        scrollPane1.setViewportView(textAreaDescription);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Name of the Category");
        panel2.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldName = new JTextField();
        panel2.add(textFieldName, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        labelColor = new JLabel();
        labelColor.setText("Color");
        panel2.add(labelColor, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), 3, -1));
        panel2.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panelColor = new JPanel();
        panelColor.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelColor.setBackground(new Color(-1));
        panel3.add(panelColor, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(32, -1), null, 0, false));
        panelColor.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        buttonColorPicker = new JButton();
        buttonColorPicker.setBorderPainted(false);
        buttonColorPicker.setContentAreaFilled(false);
        buttonColorPicker.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/form/context/images/palette-16.png")));
        buttonColorPicker.setText("");
        panel3.add(buttonColorPicker, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(18, 18), new Dimension(18, 18), new Dimension(18, 18), 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOkay = new JButton();
        buttonOkay.setText("Okay");
        panel4.add(buttonOkay, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel4.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panelMain.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(300, -1), null, 0, false));
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
