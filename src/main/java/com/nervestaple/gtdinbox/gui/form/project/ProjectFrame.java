package com.nervestaple.gtdinbox.gui.form.project;

import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.utility.swing.GuiSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a frame for the ProjectPanel form.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ProjectFrame extends JFrame {

    /**
     * The panel for this frame.
     */
    private ProjectPanel panel;

    /**
     * Application icon small.
     */
    private final ImageIcon ICON_APPLICATION_SMALL;

    /**
     * Listeners for the frame.
     */
    private Set listeners;

    /**
     * Creates a new ProjectFrame.
     */
    public ProjectFrame() {

        super();

        panel = new ProjectPanel();

        ICON_APPLICATION_SMALL = new ImageIcon(getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/browser/images/project-16.png"));

        setIconImage(ICON_APPLICATION_SMALL.getImage());

        initializeFrame();

        setupListeners();

        setResizable(false);
    }

    /**
     * Bootstraps the application.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {

        UtilitiesGui.configureSwingUI();

        ProjectFrame frame = new ProjectFrame();

        GuiSwing.centerWindow(frame);

        frame.setVisible(true);
    }

    /**
     * Adds a new Project to the data store.
     */
    public void addNewProject() {

        setTitle("Add a New Project");
        panel.addNewProject();
    }

    /**
     * Prompts the user to update the data for a Project.
     *
     * @param project Project to be updated
     */
    public void updateProject(Project project) {

        setTitle("Edit a Project");

        try {
            panel.updateProject(project);
        } catch (GTDInboxException e) {

            handleErrorOccurred(e);
        }
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

    // accessor methods

    public ProjectPanel getPanel() {
        return panel;
    }

    // private methods

    private void fireProjectAdded(final Project project) {

        ProjectFormListener[] listenersArray =
                (ProjectFormListener[]) listeners.toArray(new ProjectFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].projectAdded(project);
        }

        if (isVisible()) {

            setVisible(false);
        }
    }

    private void fireProjectUpdated(final Project project) {

        ProjectFormListener[] listenersArray =
                (ProjectFormListener[]) listeners.toArray(new ProjectFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].projectUpdated(project);
        }

        if (isVisible()) {

            setVisible(false);
        }
    }

    private void fireErrorOccurred(final GTDInboxException exception) {

        ProjectFormListener[] listenersArray =
                (ProjectFormListener[]) listeners.toArray(new ProjectFormListener[listeners.size()]);

        for (int index = 0; index < listenersArray.length; index++) {

            listenersArray[index].exceptionOccurred(exception);
        }
    }

    private void handleErrorOccurred(GTDInboxException exception) {

        JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty("OptionPane.css") +
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

    private void setupListeners() {

        panel.addProjectFormListener(new ProjectFormListener() {

            /**
             * Called when a new project is added.
             *
             * @param project Project that was added
             */
            public void projectAdded(Project project) {

                fireProjectAdded(project);
            }

            /**
             * Called when a new project is updated.
             *
             * @param project Project that was updated
             */
            public void projectUpdated(Project project) {

                fireProjectUpdated(project);
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

        panel.setActionListenerCancel(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {

                setVisible(false);
            }
        });
    }

    /**
     * Sets up the frame
     */
    private void initializeFrame() {

        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(panel);

        pack();

        listeners = new HashSet();
    }
}
