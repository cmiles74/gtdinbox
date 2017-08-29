package com.nervestaple.gtdinbox.gui;

import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import com.google.common.eventbus.EventBus;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.datastore.index.IndexManager;
import com.nervestaple.gtdinbox.datastore.index.IndexManagerException;
import com.nervestaple.gtdinbox.gui.browser.BrowserFrame;
import com.nervestaple.gtdinbox.gui.form.FrameManager;
import com.nervestaple.gtdinbox.gui.form.about.AboutFrame;
import com.nervestaple.gtdinbox.gui.form.actionitem.ActionItemFrame;
import com.nervestaple.gtdinbox.gui.form.category.CategoryFrame;
import com.nervestaple.gtdinbox.gui.form.context.ContextFrame;
import com.nervestaple.gtdinbox.gui.form.markdownsheet.MarkdownSheetFrame;
import com.nervestaple.gtdinbox.gui.form.preferences.PreferencesFrame;
import com.nervestaple.gtdinbox.gui.form.project.ProjectFrame;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.utility.swing.GuiSwing;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Provides the main object for the GTD Inbox Swing GUI.
 */
public class GTDInboxGUI {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Singleton instance of the GUI.
     */
    private static GTDInboxGUI gtdInboxGUI;

    /**
     * Warning application icon.
     */
    private final ImageIcon ICON_APPLICATION_CAUTION;

    private final EventBus eventBus;

    static {

        gtdInboxGUI = new GTDInboxGUI();
    }

    /**
     * Creates a new GTDInboxGUI. This creates and displays a splash window, then starts the rest of the application.
     */
    private GTDInboxGUI() {

        eventBus = new EventBus();

        UtilitiesGui.configureSwingUI();

        ICON_APPLICATION_CAUTION = new ImageIcon(getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/images/application-caution-64.png"));

        FrameManager.getInstance().getSplashFrame().setVisible(true);

        startApplication();

        FrameManager.getInstance().getSplashFrame().setVisible(false);
        FrameManager.getInstance().getSplashFrame().dispose();
    }

    /**
     * Handles the rest of the application startup, including loading in the user's data.
     */
    public void startApplication() {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();

        try {

            // configure the configuration
            configurationFactory.configure();
        } catch (Throwable e) {

            handleException(e);
        }

        FrameManager.getInstance().getBrowserFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        FrameManager.getInstance().getBrowserFrame().addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent event) {

                //doQuitWithWarning();
                doQuitApplication();
            }
        });

        GuiSwing.centerWindow(FrameManager.getInstance().getBrowserFrame());
        FrameManager.getInstance().getBrowserFrame().setVisible(true);
        FrameManager.getInstance().getBrowserFrame().toFront();
    }

    /**
     * Returns the active instance of the GTDInboxGUI.
     *
     * @return The main GUI object.
     */
    public static GTDInboxGUI getInstance() {

        return (gtdInboxGUI);
    }

    /**
     * Provides a default handler for action events coming form the menu bar.
     *
     * @param actionEvent ActionEvent from the menu bar.
     */
    public void handleMenuAction(ActionEvent actionEvent) {

        logger.info(actionEvent);

        if (actionEvent.getActionCommand().equals("Quit GTD Inbox")) {

            //doQuitWithWarning();
            doQuitApplication();
        } else if (actionEvent.getActionCommand().equals("About GTD Inbox")) {

            doShowAboutFrame();
        } else if (actionEvent.getActionCommand().equals("Preferences...")) {

            doShowPreferencesFrame();
        } else if (actionEvent.getActionCommand().equals("Markdown Cheat Sheet...")) {

            doShowMarkdownSheetFrame();
        }
    }

    /**
     * Verifies the the user does want to quit the application before quitting.
     */
    public void doQuitWithWarning() {

        final JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty("OptionPane.css") +
                        "<b>Are you sure that you want to quit?</b><p>" +
                        "All of your data will be saved when the program closes.",
                JOptionPane.WARNING_MESSAGE
        );
        final Object[] options = {"Quit", "Cancel"};
        pane.setOptions(options);
        pane.setInitialValue(options[1]);
        pane.setIcon(ICON_APPLICATION_CAUTION);
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer(1)
        );

        if (FrameManager.getInstance().getBrowserFrame().isVisible()) {
            FrameManager.getInstance(). getBrowserFrame().toFront();

            JSheet.showSheet(pane, FrameManager.getInstance().getBrowserFrame(), new SheetListener() {
                public void optionSelected(SheetEvent evt) {

                    if (evt.getValue().toString().equals(options[0])) {

                        doQuitApplication();
                    }
                }
            });
        } else {

            JDialog dialog = pane.createDialog(null, "Quitting GTD Inbox");
            dialog.setVisible(true);

            logger.debug(pane.getValue());
            if (pane.getValue().toString().equals(options[0])) {

                doQuitApplication();
            } else {

                dialog.dispose();
            }
        }
    }

    /**
     * Quits the application.
     */
    public void doQuitApplication() {

        try {

            // close the index
            IndexManager.getInstance().flushIndex();

            // close the database
            DataBaseManager.getInstance().closeEntityManager();

            // exit the VM
            System.exit(0);
        } catch (IndexManagerException e) {

            FrameManager.getInstance().getBrowserFrame().handleErrorOccurred(e);
        } catch (DataBaseManagerException e) {

            FrameManager.getInstance().getBrowserFrame().handleErrorOccurred(e);
        } finally {

            System.exit(0);
        }
    }

    /**
     * Displays the about window.
     */
    public void doShowAboutFrame() {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                FrameManager.getInstance().getAboutFrame().setVisible(true);
                FrameManager.getInstance().getAboutFrame().toFront();
            }
        });
    }

    /**
     * Displays the preferences window.
     */
    public void doShowPreferencesFrame() {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                GuiSwing.centerWindow(FrameManager.getInstance().getPreferencesFrame());
                FrameManager.getInstance().getPreferencesFrame().setVisible(true);
                FrameManager.getInstance().getPreferencesFrame().toFront();
            }
        });
    }

    /**
     * Displays the Markdown cheat sheet window.
     */
    public void doShowMarkdownSheetFrame() {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                GuiSwing.centerWindow(FrameManager.getInstance().getMarkdownSheetFrame());
                FrameManager.getInstance().getMarkdownSheetFrame().setVisible(true);
                FrameManager.getInstance().getMarkdownSheetFrame().toFront();
            }
        });
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    // private methods

    private void handleException(Throwable exception) {

        logger.warn(exception.getMessage(), exception);

        String message = exception.getMessage();

        if (message == null) {
            message = "An unknown application occured, I am very sorry!";
        }

        final JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty("OptionPane.css") +
                        "<b>GTD Inbox cannot startup.</b><p>" +
                        message,
                JOptionPane.ERROR_MESSAGE
        );
        Object[] options = {"Quit"};
        pane.setOptions(options);
        pane.setInitialValue(options[0]);
        pane.setIcon(ICON_APPLICATION_CAUTION);
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer(0)
        );

        JDialog dialog = pane.createDialog(null, "GTD Inbox Can't Startup");
        dialog.setVisible(true);

        doQuitApplication();
    }
}
