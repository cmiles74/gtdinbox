package com.nervestaple.gtdinbox.gui.form.project;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.model.project.Project;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import javax.swing.text.html.parser.Entity;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Provides a for for editing Project data.
 */
public class ProjectPanel extends JPanel {

    /**
     * Project this form is manipulating.
     */
    private Project project;

    /**
     * Set of listeners for this form.
     */
    private Set listeners;

    /**
     * Cancel button listener.
     */
    private ActionListener actionListenerCancel;

    // gui field objects
    private JTextField textFieldName;
    private JTextArea textAreaDescription;
    private JButton buttonOkay;
    private JButton buttonCancel;
    private JLabel labelTitle;
    private JPanel panelMain;

    public ProjectPanel() {

        super();

        initializePanel();
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

    /**
     * Readies the form to add a new project.
     */
    public void addNewProject() {

        Project project = new Project();

        setTitle("Add a New Project...");

        setProject(project);

        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getProject().setName(textFieldName.getText().trim());
                getProject().setDescription(textAreaDescription.getText().trim());

                try {

                    DataBaseManager.getInstance().beginTransaction();
                    DataBaseManager.getInstance().getEntityManager().persist(getProject());
                    DataBaseManager.getInstance().commitTransaction();

                    fireProjectAdded(getProject());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }

                buttonOkay.removeActionListener(this);
            }
        });
    }

    /**
     * Prompts the user to update the data for a Project.
     *
     * @param project Project to update
     * @throws GTDInboxException On problems reading or writing from the data store
     */
    public void updateProject(Project project) throws GTDInboxException {

        // make sure we have current data
        DataBaseManager.getInstance().getEntityManager().persist(project);

        // save a reference to the project
        this.project = project;

        setTitle("Edit a Project...");

        // update the panel fields
        updatePanel(project);

        removeActionListenersFromButton(buttonOkay);
        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getProject().setName(textFieldName.getText().trim());
                getProject().setDescription(textAreaDescription.getText().trim());

                try {

                    // get a session
                    EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();

                    // start a new transaction and save the project
                    DataBaseManager.getInstance().beginTransaction();
                    entityManager.persist(getProject());

                    // commit the transaction and close our session
                    DataBaseManager.getInstance().commitTransaction();

                    fireProjectUpdated(getProject());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }

                buttonOkay.removeActionListener(this);
            }
        });
    }

    /**
     * Readies the form to edit an existing project.
     *
     * @param project The Project to edit
     */
    public void editProject(Project project) {

        setTitle("Edit a Project...");

        setProject(project);

        removeActionListenersFromButton(buttonOkay);
        buttonOkay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                getProject().setName(textFieldName.getText().trim());
                getProject().setDescription(textAreaDescription.getText().trim());

                try {

                    DataBaseManager.getInstance().beginTransaction();
                    DataBaseManager.getInstance().getEntityManager().persist(getProject());
                    DataBaseManager.getInstance().commitTransaction();

                    fireProjectAdded(getProject());
                } catch (DataBaseManagerException e) {

                    fireErrorOccurred(e);
                }

                buttonOkay.removeActionListener(this);
            }
        });
    }

    // accessor and mutator methods

    /**
     * Returns the project being operated upon.
     *
     * @return Project being modified
     */
    public Project getProject() {
        return project;
    }

    /**
     * Adds a new listener to the form.
     *
     * @param listener Listener to handle the events
     */
    public void addProjectFormListener(ProjectFormListener listener) {

        if (!listeners.contains(listener)) {

            listeners.add(listener);
        }
    }

    /**
     * Removes a listener from the form.
     *
     * @param listener The listener to remove.
     */
    public void removeProjectFormListener(ProjectFormListener listener) {

        listeners.remove(listener);
    }

    public ActionListener getActionListenerCancel() {
        return actionListenerCancel;
    }

    public void setActionListenerCancel(ActionListener actionListenerCancel) {
        this.actionListenerCancel = actionListenerCancel;
    }

    // private methods

    /**
     * Updates the fields to match the Project.
     *
     * @param project Project used to update the fields
     */
    private void updatePanel(final Project project) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                textFieldName.setText(project.getName());
                textAreaDescription.setText(project.getDescription());
            }
        });
    }

    /**
     * Removes all action listeners from the button.
     *
     * @param button Button to remove action listeners from.
     */
    private void removeActionListenersFromButton(JButton button) {

        ActionListener[] listeners = button.getActionListeners();
        for (int index = 0; index < listeners.length; index++) {
            button.removeActionListener(listeners[index]);
        }
    }

    private void fireProjectAdded(final Project project) {

        ProjectFormListener[] listenersArray =
                (ProjectFormListener[]) listeners.toArray(new ProjectFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].projectAdded(project);
        }
    }

    private void fireProjectUpdated(final Project project) {

        ProjectFormListener[] listenersArray =
                (ProjectFormListener[]) listeners.toArray(new ProjectFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].projectUpdated(project);
        }
    }

    private void fireErrorOccurred(final GTDInboxException exception) {

        ProjectFormListener[] listenersArray =
                (ProjectFormListener[]) listeners.toArray(new ProjectFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].exceptionOccurred(exception);
        }
    }

    private void setProject(Project project) {

        this.project = new Project();

        textFieldName.setText(project.getName());
        textAreaDescription.setText(project.getDescription());
    }

    private void clearPanel() {

        project = null;

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
        panelMain.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 1, new Insets(8, 5, 15, 5), -1, -1));
        labelTitle = new JLabel();
        Font labelTitleFont = this.$$$getFont$$$(null, -1, 18, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setIcon(new ImageIcon(getClass().getResource("/com/nervestaple/gtdinbox/gui/browser/images/project-32.png")));
        labelTitle.setText("Create/Edit a Project");
        panelMain.add(labelTitle, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTHWEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Name of the Project");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        textFieldName = new JTextField();
        panel1.add(textFieldName, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("A Brief Description of the Project");
        panel2.add(label2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy(31);
        scrollPane1.setVerticalScrollBarPolicy(22);
        panel2.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 75), null, 0, false));
        textAreaDescription = new JTextArea();
        Font textAreaDescriptionFont = this.$$$getFont$$$(null, -1, 11, textAreaDescription.getFont());
        if (textAreaDescriptionFont != null) textAreaDescription.setFont(textAreaDescriptionFont);
        textAreaDescription.setLineWrap(true);
        textAreaDescription.setWrapStyleWord(true);
        scrollPane1.setViewportView(textAreaDescription);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOkay = new JButton();
        buttonOkay.setText("Okay");
        panel3.add(buttonOkay, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel3.add(buttonCancel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
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
