package com.nervestaple.gtdinbox.gui.form.context;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a panel for editing an InboxContext.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ContextPanel extends JPanel {

    /**
     * The InboxContext being edited.
     */
    private InboxContext inboxContext;

    /**
     * Set of listeners for this form.
     */
    private Set listeners;

    /**
     * Cancel button listener.
     */
    private ActionListener actionListenerCancel;

    // gui form objects
    private JPanel panelMain;
    private JLabel labelTitle;
    private JTextField textFieldName;
    private JTextArea textAreaDescription;
    private JButton buttonOkay;
    private JButton buttonCancel;

    /**
     * Creates a new ContextPanel.
     */
    public ContextPanel() {

        super();

        initializePanel();
    }

    public void addNewInboxContext() {

        InboxContext inboxContext = new InboxContext();

        setTitle("Add a New Context...");

        setInboxContext(inboxContext);

        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getInboxContext().setName(textFieldName.getText().trim());
                getInboxContext().setDescription(textAreaDescription.getText().trim());

                try {

                    DataBaseManager.getInstance().beginTransaction();
                    DataBaseManager.getInstance().getSession().save(getInboxContext());
                    DataBaseManager.getInstance().commitTransaction();

                    fireInboxContextAdded(getInboxContext());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }

                buttonOkay.removeActionListener(this);
            }
        });
    }

    public void updateInboxContext(InboxContext inboxContext) throws GTDInboxException {

        // make sure we have current data
        DataBaseManager.getInstance().getSession().update(inboxContext);

        // save a reference to the project
        this.inboxContext = inboxContext;

        setTitle("Edit a Context...");

        // update the panel fields
        updatePanel(inboxContext);

        removeActionListenersFromButton(buttonOkay);
        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getInboxContext().setName(textFieldName.getText().trim());
                getInboxContext().setDescription(textAreaDescription.getText().trim());

                try {

                    // get a session
                    Session session = DataBaseManager.getInstance().getSession();

                    // start a new transaction and save the project
                    Transaction transaction = session.beginTransaction();
                    session.saveOrUpdate(getInboxContext());

                    // commit the transaction and close our session
                    transaction.commit();

                    fireInboxContextUpdated(getInboxContext());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }

                buttonOkay.removeActionListener(this);
            }
        });
    }

    public void addInboxContextFormListener(ContextFormListener listener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeInboxContextFormListener(ContextFormListener listener) {

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

    public InboxContext getInboxContext() {
        return inboxContext;
    }

    // private methods

    private void removeActionListenersFromButton(JButton button) {

        ActionListener[] listeners = button.getActionListeners();
        for (int index = 0; index < listeners.length; index++) {
            button.removeActionListener(listeners[index]);
        }
    }

    private void updatePanel(final InboxContext inboxContext) throws DataBaseManagerException {

        DataBaseManager.getInstance().getSession().update(inboxContext);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                textFieldName.setText(inboxContext.getName());
                textAreaDescription.setText(inboxContext.getDescription());
            }
        });
    }

    private void fireInboxContextAdded(final InboxContext inboxContext) {

        ContextFormListener[] listenersArray =
                (ContextFormListener[]) listeners.toArray(new ContextFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].inboxContextAdded(inboxContext);
        }
    }

    private void fireInboxContextUpdated(final InboxContext inboxContext) {

        ContextFormListener[] listenersArray =
                (ContextFormListener[]) listeners.toArray(new ContextFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].inboxContextUpdated(inboxContext);
        }
    }

    private void fireErrorOccurred(final GTDInboxException exception) {

        ContextFormListener[] listenersArray =
                (ContextFormListener[]) listeners.toArray(new ContextFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].exceptionOccurred(exception);
        }
    }

    private void setInboxContext(InboxContext inboxContext) {

        this.inboxContext = inboxContext;

        textFieldName.setText(inboxContext.getName());
        textAreaDescription.setText(inboxContext.getDescription());
    }

    private void clearPanel() {

        inboxContext = null;

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                textFieldName.setText(null);
                textAreaDescription.setText(null);
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
        panelMain.setLayout(new GridLayoutManager(5, 1, new Insets(8, 5, 15, 5), -1, -1));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$(null, -1, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/context-32.png")));
        labelTitle.setText("Create/Edit a Context");
        panelMain.add(labelTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Name of the Context");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldName = new JTextField();
        panel1.add(textFieldName, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("A Brief Description of the Context");
        panel2.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        scrollPane1.setVerticalScrollBarPolicy(22);
        panel2.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 75), null, 0, false));
        textAreaDescription = new JTextArea();
        Font textAreaDescriptionFont = this.$$$getFont$$$(null, -1, 11, textAreaDescription.getFont());
        if (textAreaDescriptionFont != null) textAreaDescription.setFont(textAreaDescriptionFont);
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        scrollPane1.setViewportView(textAreaDescription);
        final Spacer spacer1 = new Spacer();
        panelMain.add(spacer1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, new Dimension(300, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOkay = new JButton();
        buttonOkay.setText("Okay");
        panel3.add(buttonOkay, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel3.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
