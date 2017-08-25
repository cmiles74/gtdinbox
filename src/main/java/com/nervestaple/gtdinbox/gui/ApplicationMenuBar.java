package com.nervestaple.gtdinbox.gui;

import com.nervestaple.gtdinbox.gui.browser.BrowserFrame;
import com.nervestaple.gtdinbox.gui.form.about.AboutFrame;
import com.nervestaple.utility.Platform;
import net.roydesign.app.AboutJMenuItem;
import net.roydesign.app.PreferencesJMenuItem;
import net.roydesign.app.QuitJMenuItem;
import net.roydesign.mac.MRJAdapter;
import net.roydesign.ui.JScreenMenu;
import net.roydesign.ui.JScreenMenuBar;
import net.roydesign.ui.JScreenMenuItem;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

/**
 * Provides a menu bar for the application.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ApplicationMenuBar extends JScreenMenuBar {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * About frame.
     */
    private AboutFrame aboutFrame;

    /**
     * Action listener to handle menu bar events.
     */
    private ActionListener actionListener;

    /**
     * Application icon.
     */
    private ImageIcon ICON_APPLICATION;

    JScreenMenu menuFile;
    JScreenMenuItem menuPrintDetail;

    JScreenMenu menuEdit;
    JScreenMenuItem menuItemCut;
    JScreenMenuItem menuItemCopy;
    JScreenMenuItem menuItemPaste;

    JScreenMenu menuOrganize;
    JScreenMenuItem menuItemAddProject;
    JScreenMenuItem menuItemAddContext;
    //JScreenMenuItem menuItemAddCategory;
    JScreenMenuItem menuItemEditProject;
    JScreenMenuItem menuItemEditContext;
    //JScreenMenuItem menuItemEditCategory;
    JScreenMenuItem menuItemRemoveProject;
    JScreenMenuItem menuItemRemoveContext;
    //JScreenMenuItem menuItemRemoveCategory;

    JScreenMenu menuProcess;
    JScreenMenuItem menuItemAddActionItem;
    JScreenMenuItem menuItemEditActionItem;
    JScreenMenuItem menuItemRemoveActionItem;
    /*JScreenMenuItem menuItemAddReferenceItem;
    JScreenMenuItem menuItemEditReferenceItem;
    JScreenMenuItem menuItemRemoveReferenceItem;*/

    JScreenMenu menuHelp;
    JScreenMenuItem menuItemMarkdownCheatSheetItem;

    /**
     * Flag to indicate if we're running on Macintosh.
     */
    private static boolean isMacintosh = false;

    static {

        isMacintosh = Platform.checkMacintosh();
    }

    /**
     * Creates a new ApplicationMenuBar instance.
     */
    public ApplicationMenuBar(final ActionListener actionListener) {

        // File menu
        menuFile = new JScreenMenu("File");
        add(menuFile);

        // About menu item
        if (!AboutJMenuItem.isAutomaticallyPresent()) {

            JScreenMenuItem menuItemAbout = new JScreenMenuItem("About GTD Inbox");
            menuItemAbout.addActionListener(actionListener);
            menuFile.add(menuItemAbout);
        } else {

            MRJAdapter.addAboutListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {

                    ActionEvent actionEventThis = new ActionEvent(actionEvent.getSource(), 0, "About GTD Inbox");
                    actionListener.actionPerformed(actionEventThis);
                }
            });
        }

        // Preferences menu item
        if (!PreferencesJMenuItem.isAutomaticallyPresent()) {

            JScreenMenuItem menuItemPreferences = new JScreenMenuItem("Preferences...");
            menuItemPreferences.addActionListener(actionListener);
            menuFile.add(menuItemPreferences);
        } else {

            MRJAdapter.addPreferencesListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {

                    ActionEvent actionEventThis = new ActionEvent(actionEvent.getSource(), 0, "Preferences...");
                    actionListener.actionPerformed(actionEventThis);
                }
            });
        }

        // Print menu item
        menuPrintDetail = new JScreenMenuItem("Print...");
        menuPrintDetail.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.META_MASK));
        menuPrintDetail.addActionListener(actionListener);
        menuFile.add(menuPrintDetail);

        if (isMacintosh) {
            menuPrintDetail.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.META_MASK));
        } else {
            menuPrintDetail.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_MASK));
        }

        //  Quit menu item
        if (!QuitJMenuItem.isAutomaticallyPresent()) {

            JScreenMenuItem menuItemQuit = new JScreenMenuItem("Quit GTD Inbox");
            menuItemQuit.addActionListener(actionListener);

            // the shortcut isn't present on platforms other than Macintosh
            menuPrintDetail.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_MASK));

            menuFile.add(menuItemQuit);
        } else {

            MRJAdapter.addQuitApplicationListener(new ActionListener() {
                public void actionPerformed(final ActionEvent event) {

                    actionListener.actionPerformed(
                            new ActionEvent(this, event.getID(), "Quit GTD Inbox"));
                }
            });
        }

        // Edit menu
        menuEdit = new JScreenMenu("Edit");
        add(menuEdit);

        // Cut menu item
        menuItemCut = new JScreenMenuItem("Cut");
        menuItemCut.addActionListener(actionListener);
        menuEdit.add(menuItemCut);

        if (isMacintosh) {
            menuItemCut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.META_MASK));
        } else {
            menuItemCut.setAccelerator(KeyStroke.getKeyStroke('X', InputEvent.CTRL_MASK));
        }

        // Copy menu item
        menuItemCopy = new JScreenMenuItem("Copy");
        menuItemCopy.addActionListener(actionListener);
        menuEdit.add(menuItemCopy);

        if (isMacintosh) {
            menuItemCopy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.META_MASK));
        } else {
            menuItemCopy.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_MASK));
        }

        // Paste menu item
        menuItemPaste = new JScreenMenuItem("Paste");
        menuItemPaste.addActionListener(actionListener);
        menuEdit.add(menuItemPaste);

        if (isMacintosh) {
            menuItemPaste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.META_MASK));
        } else {
            menuItemPaste.setAccelerator(KeyStroke.getKeyStroke('V', InputEvent.CTRL_MASK));
        }

        // Organize menu
        menuOrganize = new JScreenMenu("Organize");
        menuOrganize.addUserFrame(BrowserFrame.class);
        add(menuOrganize);

        // New project item
        menuItemAddProject = new JScreenMenuItem("Add a Project...");
        menuItemAddProject.addActionListener(actionListener);
        menuItemAddProject.addUserFrame(BrowserFrame.class);
        menuOrganize.add(menuItemAddProject);

        // New context item
        menuItemAddContext = new JScreenMenuItem("Add a Context...");
        menuItemAddContext.addActionListener(actionListener);
        menuItemAddContext.addUserFrame(BrowserFrame.class);
        menuOrganize.add(menuItemAddContext);

        // New project item
        /*menuItemAddCategory = new JScreenMenuItem( "Add a Category..." );
        menuItemAddCategory.addActionListener( actionListener );
        menuItemAddCategory.addUserFrame( BrowserFrame.class );
        menuOrganize.add( menuItemAddCategory );*/

        menuOrganize.addSeparator();

        // Edit category item
        menuItemEditProject = new JScreenMenuItem("Edit Project...");
        menuItemEditProject.addActionListener(actionListener);
        menuItemEditProject.addUserFrame(BrowserFrame.class);
        menuOrganize.add(menuItemEditProject);

        // Edit context item
        menuItemEditContext = new JScreenMenuItem("Edit Context...");
        menuItemEditContext.addActionListener(actionListener);
        menuItemEditContext.addUserFrame(BrowserFrame.class);
        menuOrganize.add(menuItemEditContext);

        // Edit category item
        /*menuItemEditCategory = new JScreenMenuItem( "Edit Category..." );
        menuItemEditCategory.addActionListener( actionListener );
        menuItemEditCategory.addUserFrame( BrowserFrame.class );
        menuOrganize.add( menuItemEditCategory );*/

        menuOrganize.addSeparator();

        // Remove category item
        menuItemRemoveProject = new JScreenMenuItem("Remove Project...");
        menuItemRemoveProject.addActionListener(actionListener);
        menuItemRemoveProject.addUserFrame(BrowserFrame.class);
        menuOrganize.add(menuItemRemoveProject);

        // Remove context item
        menuItemRemoveContext = new JScreenMenuItem("Remove Context...");
        menuItemRemoveContext.addActionListener(actionListener);
        menuItemRemoveContext.addUserFrame(BrowserFrame.class);
        menuOrganize.add(menuItemRemoveContext);

        // Remove category item
        /*menuItemRemoveCategory = new JScreenMenuItem( "Remove Category..." );
        menuItemRemoveCategory.addActionListener( actionListener );
        menuItemRemoveCategory.addUserFrame( BrowserFrame.class );
        menuOrganize.add( menuItemRemoveCategory );*/

        // Process menu
        menuProcess = new JScreenMenu("Process");
        menuProcess.addUserFrame(BrowserFrame.class);
        add(menuProcess);

        // Add action
        menuItemAddActionItem = new JScreenMenuItem("Add an Action Item...");
        menuItemAddActionItem.addActionListener(actionListener);
        menuItemAddActionItem.addUserFrame(BrowserFrame.class);
        menuProcess.add(menuItemAddActionItem);

        // Add reference Item
        /*menuItemAddReferenceItem = new JScreenMenuItem( "Add an Reference Item..." );
        menuItemAddReferenceItem.addActionListener( actionListener );
        menuItemAddReferenceItem.addUserFrame( BrowserFrame.class );
        menuProcess.add( menuItemAddReferenceItem );*/

        //menuProcess.addSeparator();

        // Edit action
        menuItemEditActionItem = new JScreenMenuItem("Edit Action Item...");
        menuItemEditActionItem.addActionListener(actionListener);
        menuItemEditActionItem.addUserFrame(BrowserFrame.class);
        menuProcess.add(menuItemEditActionItem);

        // Edit reference Item
        /*menuItemEditReferenceItem = new JScreenMenuItem( "Edit Reference Item..." );
        menuItemEditReferenceItem.addActionListener( actionListener );
        menuItemEditReferenceItem.addUserFrame( BrowserFrame.class );
        menuProcess.add( menuItemEditReferenceItem );*/

        //menuProcess.addSeparator();

        // Remove action
        menuItemRemoveActionItem = new JScreenMenuItem("Remove Action Item...");
        menuItemRemoveActionItem.addActionListener(actionListener);
        menuItemRemoveActionItem.addUserFrame(BrowserFrame.class);
        menuProcess.add(menuItemRemoveActionItem);

        // Remove reference Item
        /*menuItemRemoveReferenceItem = new JScreenMenuItem( "Remove Reference Item..." );
        menuItemRemoveReferenceItem.addActionListener( actionListener );
        menuItemRemoveReferenceItem.addUserFrame( BrowserFrame.class );
        menuProcess.add( menuItemRemoveReferenceItem );*/

        // Help menu
        menuHelp = new JScreenMenu("Help");
        add(menuHelp);

        // New project item
        menuItemMarkdownCheatSheetItem = new JScreenMenuItem("Markdown Cheat Sheet...");
        menuItemMarkdownCheatSheetItem.addActionListener(actionListener);
        menuHelp.add(menuItemMarkdownCheatSheetItem);

        // disable menu items that depend on something being selected
        menuItemEditProject.setEnabled(false);
        menuItemEditContext.setEnabled(false);
        //menuItemEditCategory.setEnabled( false );
        menuItemRemoveProject.setEnabled(false);
        menuItemRemoveContext.setEnabled(false);
        //menuItemRemoveCategory.setEnabled( false );
        menuItemEditActionItem.setEnabled(false);
        menuItemRemoveActionItem.setEnabled(false);
        //menuItemEditReferenceItem.setEnabled( false );
        //menuItemRemoveReferenceItem.setEnabled( false );
    }

    // accessor and mutator methods

    public JScreenMenu getMenuFile() {
        return menuFile;
    }

    public JScreenMenuItem getMenuPrintDetail() {
        return menuPrintDetail;
    }

    public JScreenMenu getMenuEdit() {
        return menuEdit;
    }

    public JScreenMenuItem getMenuItemCut() {
        return menuItemCut;
    }

    public JScreenMenuItem getMenuItemCopy() {
        return menuItemCopy;
    }

    public JScreenMenuItem getMenuItemPaste() {
        return menuItemPaste;
    }

    public JScreenMenu getMenuOrganize() {
        return menuOrganize;
    }

    public JScreenMenuItem getMenuItemAddProject() {
        return menuItemAddProject;
    }

    public JScreenMenuItem getMenuItemAddContext() {
        return menuItemAddContext;
    }

    /*public JScreenMenuItem getMenuItemAddCategory() {
        return menuItemAddCategory;
    }*/

    public JScreenMenuItem getMenuItemEditProject() {
        return menuItemEditProject;
    }

    public JScreenMenuItem getMenuItemEditContext() {
        return menuItemEditContext;
    }

    /*public JScreenMenuItem getMenuItemEditCategory() {
        return menuItemEditCategory;
    }*/

    public JScreenMenu getMenuProcess() {
        return menuProcess;
    }

    public JScreenMenuItem getMenuItemAddActionItem() {
        return menuItemAddActionItem;
    }

    /*public JScreenMenuItem getMenuItemAddReferenceItem() {
        return menuItemAddReferenceItem;
    }*/

    public JScreenMenuItem getMenuItemEditActionItem() {
        return menuItemEditActionItem;
    }

    /*public JScreenMenuItem getMenuItemEditReferenceItem() {
        return menuItemEditReferenceItem;
    }*/

    public JScreenMenuItem getMenuItemRemoveActionItem() {
        return menuItemRemoveActionItem;
    }

    /*public JScreenMenuItem getMenuItemRemoveReferenceItem() {
        return menuItemRemoveReferenceItem;
    }*/

    public JScreenMenuItem getMenuItemRemoveProject() {
        return menuItemRemoveProject;
    }

    public JScreenMenuItem getMenuItemRemoveContext() {
        return menuItemRemoveContext;
    }

    /*public JScreenMenuItem getMenuItemRemoveCategory() {
        return menuItemRemoveCategory;
    }*/
}
