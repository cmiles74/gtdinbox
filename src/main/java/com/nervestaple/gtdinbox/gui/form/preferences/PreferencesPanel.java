package com.nervestaple.gtdinbox.gui.form.preferences;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Provides a panel with the preferences for the application.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class PreferencesPanel extends JPanel {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Data model for this panel.
     */
    private PreferencesModel model;

    /**
     * Format for whole numbers.
     */
    private NumberFormat formatWholeNumber = new DecimalFormat("###");

    // gui form objects
    private JFormattedTextField formattedTextFieldDaysArchive;
    private JButton buttonSave;
    private JButton buttonCancel;
    private JLabel labelTitle;
    private JPanel panelMain;

    public PreferencesPanel(PreferencesModel model) {

        super();

        this.model = model;

        formattedTextFieldDaysArchive.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(
                formatWholeNumber)));

        setupListeners();

        add(panelMain);
    }

    public void commitValues() {

        try {

            formattedTextFieldDaysArchive.commitEdit();
            model.setArchiveDays(new Integer(((Number) formattedTextFieldDaysArchive.getValue()).intValue()));
        } catch (ParseException e) {
            logger.warn(e);
        }
    }

    // private methods

    private void setupListeners() {

        model.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

                logger.debug(propertyChangeEvent.getPropertyName() + ": " + propertyChangeEvent.getOldValue() + " -> "
                        + propertyChangeEvent.getNewValue());
            }
        });

        model.addPropertyChangeListener("archiveDays", new PropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent propertyChangeEvent) {

                if (propertyChangeEvent.getNewValue() != null) {

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {

                            formattedTextFieldDaysArchive.setValue((Integer) propertyChangeEvent.getNewValue());
                        }
                    });
                } else {

                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {

                            formattedTextFieldDaysArchive.setValue(new Integer(0));
                        }
                    });
                }
            }
        });

        buttonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (model.getActionListenerCancel() != null) {

                    model.getActionListenerCancel().actionPerformed(actionEvent);
                }
            }
        });

        buttonSave.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (model.getActionListenerSave() != null) {

                    model.getActionListenerSave().actionPerformed(actionEvent);
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
        panelMain.setLayout(new GridLayoutManager(5, 1, new Insets(8, 5, 15, 5), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Completed items are moved to the Archive after...");
        panelMain.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panelMain.add(spacer1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        formattedTextFieldDaysArchive = new JFormattedTextField();
        formattedTextFieldDaysArchive.setColumns(3);
        panel1.add(formattedTextFieldDaysArchive, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 1, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("days");
        panel1.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSave = new JButton();
        buttonSave.setText("Save");
        panel2.add(buttonSave, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel2.add(spacer3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$(null, -1, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/images/preferences-32.png")));
        labelTitle.setText("Preferences");
        panelMain.add(labelTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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