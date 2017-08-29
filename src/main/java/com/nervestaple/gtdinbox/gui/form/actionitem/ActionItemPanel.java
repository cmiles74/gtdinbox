package com.nervestaple.gtdinbox.gui.form.actionitem;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.swing.EventComboBoxModel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.gui.GTDInboxGUI;
import com.nervestaple.gtdinbox.gui.browser.BrowserFrame;
import com.nervestaple.gtdinbox.gui.form.FrameManager;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.project.Project;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a form for editing ActionItem data.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ActionItemPanel extends JPanel {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * The ActionItem being edited.
     */
    private ActionItem actionItem;

    /**
     * Set of listeners for this form.
     */
    private Set listeners;

    /**
     * Cancel button listener.
     */
    private ActionListener actionListenerCancel;

    /**
     * Date format.
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMMM d, yyyy");

    /**
     * Date completed.
     */
    private Date dateCompleted;

    /**
     * List of projects.
     */
    private EventList listProjects;

    /**
     * List of contexts.
     */
    private EventList listContexts;

    // gui form objects
    private JLabel labelTitle;
    private JComboBox comboBoxContext;
    private JCheckBox checkBoxComplete;
    private JButton buttonOkay;
    private JButton buttonCancel;
    private JTextArea textAreaDescription;
    private JPanel panelMain;
    private JComboBox comboBoxProject;
    private JButton buttonAddProject;
    private JButton buttonAddContext;

    /**
     * Creates a new ActionItemCheckBox.
     */
    public ActionItemPanel() {

        super();

        initializePanel();

        setupListeners();
    }

    public void setListContexts(final EventList listContexts) {

        this.listContexts = listContexts;

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                comboBoxContext.setModel(new EventComboBoxModel(listContexts));
            }
        });
    }

    public void setListProjects(final EventList listProjects) {

        this.listProjects = listProjects;

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                comboBoxProject.setModel(new EventComboBoxModel(listProjects));
            }
        });
    }

    public void setSelectedProject(Project project) {

        int indexSelected = -1;

        // loop through the projects
        for (int index = 0; index < comboBoxProject.getModel().getSize(); index++) {

            if (((Project) comboBoxProject.getModel().getElementAt(index)).getId().equals(project.getId())) {

                indexSelected = index;
                break;
            }
        }

        final int indexSelectedFinal = indexSelected;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                comboBoxProject.setSelectedIndex(indexSelectedFinal);
            }
        });
    }

    public void setSelectedInboxContext(InboxContext context) {

        int indexSelected = -1;

        // loop through the projects
        for (int index = 0; index < comboBoxContext.getModel().getSize(); index++) {

            if (((InboxContext) comboBoxContext.getModel().getElementAt(index)).getId().equals(
                    context.getId())) {

                indexSelected = index;
                break;
            }
        }

        final int indexSelectedFinal = indexSelected;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                comboBoxContext.setSelectedIndex(indexSelectedFinal);
            }
        });
    }

    public void setTitle(final String title) {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                labelTitle.setText(title);
            }
        });
    }

    /**
     * Sets the panel to add a new ActionItem.
     *
     * @param project Project to parent the new ActionItem.
     */
    public void addNewActionItem(Project project) {

        ActionItem actionItem = new ActionItem();
        actionItem.setProject(project);

        setTitle("Add a New Action Item...");
        setSelectedProject(project);

        updatePanel(actionItem);

        UtilitiesGui.removeActionListeners(buttonOkay);

        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getActionItem().setDescription(textAreaDescription.getText().trim());

                InboxContext inboxContext = null;
                if (comboBoxContext.getSelectedItem() != null) {
                    inboxContext = (InboxContext) comboBoxContext.getSelectedItem();
                }

                if (inboxContext != null) {
                    inboxContext.addActionItem(getActionItem());
                }

                Project projectSelected = null;
                if (comboBoxProject.getSelectedItem() != null) {
                    projectSelected = (Project) comboBoxProject.getSelectedItem();
                }

                if (projectSelected != null) {
                    projectSelected.addActionItem(getActionItem());
                }

                if (checkBoxComplete.isSelected()) {

                    getActionItem().setCompletedDate(dateCompleted);
                }

                try {

                    DataBaseManager.getInstance().beginTransaction();

                    DataBaseManager.getInstance().getEntityManager().persist(getActionItem());

                    if (inboxContext != null) {
                        DataBaseManager.getInstance().getEntityManager().persist(inboxContext);
                    }

                    if (projectSelected != null) {
                        DataBaseManager.getInstance().getEntityManager().persist(projectSelected);
                    }

                    DataBaseManager.getInstance().commitTransaction();

                    fireActionItemAdded(getActionItem());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }
            }
        });
    }

    public void updateActionItem(ActionItem actionItem) throws GTDInboxException {

        // make sure we have current data
        DataBaseManager.getInstance().getEntityManager().persist(actionItem);

        // save a reference to the project
        this.actionItem = actionItem;

        setTitle("Edit an Action Item...");

        // update the panel fields
        updatePanel(actionItem);

        UtilitiesGui.removeActionListeners(buttonOkay);
        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getActionItem().setDescription(textAreaDescription.getText().trim());

                InboxContext inboxContext = null;
                if (comboBoxContext.getSelectedItem() != null) {
                    inboxContext = (InboxContext) comboBoxContext.getSelectedItem();
                }

                if (getActionItem() != null) {
                    inboxContext.addActionItem(getActionItem());
                }

                Project projectSelected = null;
                if (comboBoxProject.getSelectedItem() != null) {
                    projectSelected = (Project) comboBoxProject.getSelectedItem();
                }

                if (projectSelected != null) {
                    projectSelected.addActionItem(getActionItem());
                }

                if (checkBoxComplete.isSelected() && getActionItem().getCompletedDate() == null) {

                    getActionItem().setCompletedDate(dateCompleted);
                } else if (!checkBoxComplete.isSelected() && getActionItem().getCompletedDate() != null) {

                    getActionItem().setCompletedDate(null);
                }

                try {

                    // get a session
                    EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();

                    // start a new transaction and save the project
                    DataBaseManager.getInstance().beginTransaction();
                    entityManager.persist(getActionItem());

                    if (inboxContext != null) {
                        entityManager.persist(inboxContext);
                    }

                    if (projectSelected != null) {
                        entityManager.persist(projectSelected);
                    }

                    // commit the transaction and close our session
                    DataBaseManager.getInstance().commitTransaction();

                    fireActionItemUpdated(getActionItem());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }

                buttonOkay.removeActionListener(this);
            }
        });
    }

    public void addActionItemFormListener(ActionItemFormListener listener) {

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeActionItemFormListener(ActionItemFormListener listener) {

        listeners.remove(listener);
    }

    public ActionListener getActionListenerCancel() {
        return actionListenerCancel;
    }

    public void setActionListenerCancel(ActionListener actionListenerCancel) {
        this.actionListenerCancel = actionListenerCancel;
    }

    public ActionItem getActionItem() {
        return actionItem;
    }

    // private methods

    private void fireActionItemAdded(final ActionItem actionItem) {

        ActionItemFormListener[] listenersArray =
                (ActionItemFormListener[]) listeners.toArray(new ActionItemFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].actionItemAdded(actionItem);
        }
    }

    private void fireActionItemUpdated(final ActionItem actionItem) {

        ActionItemFormListener[] listenersArray =
                (ActionItemFormListener[]) listeners.toArray(new ActionItemFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].actionItemUpdated(actionItem);
        }
    }

    private void fireErrorOccurred(final GTDInboxException exception) {

        ActionItemFormListener[] listenersArray =
                (ActionItemFormListener[]) listeners.toArray(new ActionItemFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].exceptionOccurred(exception);
        }
    }

    private void updatePanel(final ActionItem actionItem) {

        clearPanel();

        this.actionItem = actionItem;

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                textAreaDescription.setText(actionItem.getDescription());
                comboBoxProject.setSelectedItem(actionItem.getProject());

                if (actionItem.getInboxContext() != null) {
                    comboBoxContext.setSelectedItem(actionItem.getInboxContext());
                } else {
                    comboBoxContext.setSelectedIndex(0);
                }

                checkBoxComplete.setSelected(actionItem.getCompletedDate() != null);

                if (actionItem.getCompletedDate() != null) {

                    checkBoxComplete.setText("This action item is complete as of "
                            + dateFormat.format(actionItem.getCompletedDate()));
                }
            }
        });
    }

    private void clearPanel() {

        actionItem = null;

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                textAreaDescription.setText(null);
                checkBoxComplete.setText("This action item is complete");
                comboBoxContext.setSelectedItem(null);
                comboBoxProject.setSelectedItem(null);
            }
        });
    }

    private void setupListeners() {

        checkBoxComplete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                if (checkBoxComplete.isSelected()) {

                    dateCompleted = new Date();
                } else {

                    dateCompleted = null;
                }
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

        buttonAddProject.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                FrameManager.getInstance().getBrowserFrame().getBrowserPanel().doAddProject();
            }
        });

        buttonAddContext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                FrameManager.getInstance().getBrowserFrame().getBrowserPanel().doAddInboxContext();
            }
        });

        listeners = new HashSet();
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
        panelMain.setLayout(new GridLayoutManager(3, 1, new Insets(8, 5, 15, 5), -1, -1));
        panelMain.setPreferredSize(new Dimension(550, 325));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$(null, -1, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/action-32.png")));
        labelTitle.setText("Create/Edit an Action Item");
        panelMain.add(labelTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Context for this Item");
        panel2.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Project for this Item");
        panel2.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        comboBoxProject = new JComboBox();
        panel3.add(comboBoxProject, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAddProject = new JButton();
        buttonAddProject.setBorderPainted(false);
        buttonAddProject.setContentAreaFilled(false);
        buttonAddProject.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/images/add.png")));
        buttonAddProject.setText("");
        panel3.add(buttonAddProject, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), new Dimension(16, 16), 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel4, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        comboBoxContext = new JComboBox();
        panel4.add(comboBoxContext, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonAddContext = new JButton();
        buttonAddContext.setBorderPainted(false);
        buttonAddContext.setContentAreaFilled(false);
        buttonAddContext.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/images/add.png")));
        buttonAddContext.setText("");
        panel4.add(buttonAddContext, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(16, 16), new Dimension(16, 16), new Dimension(16, 16), 0, false));
        checkBoxComplete = new JCheckBox();
        checkBoxComplete.setText("This action item is complete");
        panel1.add(checkBoxComplete, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Description of the Action Item");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        scrollPane1.setVerticalScrollBarPolicy(22);
        panel1.add(scrollPane1, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 75), null, 0, false));
        textAreaDescription = new JTextArea();
        Font textAreaDescriptionFont = this.$$$getFont$$$(null, -1, 11, textAreaDescription.getFont());
        if (textAreaDescriptionFont != null) textAreaDescription.setFont(textAreaDescriptionFont);
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        scrollPane1.setViewportView(textAreaDescription);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        buttonOkay = new JButton();
        buttonOkay.setText("Okay");
        panel5.add(buttonOkay, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel5.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel5.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
