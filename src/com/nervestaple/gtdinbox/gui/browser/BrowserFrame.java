package com.nervestaple.gtdinbox.gui.browser;

import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.gui.ApplicationMenuBar;
import com.nervestaple.gtdinbox.gui.GTDInboxGUI;
import com.nervestaple.gtdinbox.gui.browser.detail.DetailArchivePanel;
import com.nervestaple.gtdinbox.gui.browser.detail.DetailProjectPanel;
import com.nervestaple.gtdinbox.gui.browser.detail.searchresults.SearchResultDetailPanel;
import com.nervestaple.gtdinbox.gui.browser.detail.trash.TrashDetailForm;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesPrint;
import com.nervestaple.gtdinbox.gui.utility.glazedtreemodel.GlazedTreeNode;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.utility.swing.GuiSwing;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Provides a Frame for the BrowserPanel.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class BrowserFrame extends JFrame {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * The panel for this frame.
     */
    public BrowserPanel browserPanel;

    /**
     * The default title for this frame.
     */
    public final static String DEFAULT_TITLE = "GTD Inbox";

    /**
     * Application icon.
     */
    private final ImageIcon ICON_APPLICATION;

    /**
     * Application icon small.
     */
    private final ImageIcon ICON_APPLICATION_SMALL;

    /**
     * Warning application icon.
     */
    private final ImageIcon ICON_APPLICATION_CAUTION;

    /**
     * Menu bar.
     */
    private ApplicationMenuBar applicationMenuBar;

    /**
     * ActionListener to handle the menu bar events.
     */
    private ActionListener actionListenerMenuBar;

    /**
     * Singleton isntance of the BrowserFrame.
     */
    private static BrowserFrame browserFrame;

    static {
        browserFrame = new BrowserFrame();
    }

    /**
     * Creates a new BrowserFrame.
     */
    private BrowserFrame() {

        super( DEFAULT_TITLE );

        // load icons
        ICON_APPLICATION = new ImageIcon( getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/images/application-32.png" ) );
        ICON_APPLICATION_SMALL = new ImageIcon( getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/images/application-16.png" ) );
        ICON_APPLICATION_CAUTION = new ImageIcon( getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/images/application-caution-64.png" ) );

        setIconImage( ICON_APPLICATION_SMALL.getImage() );

        // get a browser panel
        browserPanel = new BrowserPanel();

        // add the panel to this frame
        getContentPane().add( browserPanel );

        // set default gui properties
        browserPanel.setBorder( BorderFactory.createEmptyBorder() );

        setupMenuBar();

        setupListeners();

        // enforce the minimum frame size
        UtilitiesGui.addEnforceMinimumSizeComponentListener( this );

        // pack the frame
        pack();
    }

    public static BrowserFrame getInstance() {

        return ( browserFrame );
    }


    public void setVisible( boolean isVisible ) {

        super.setVisible( isVisible );
    }

    /**
     * Bootstraps the application.
     *
     * @param args Command line arguments
     */
    public static void main( final String[] args ) {

        UtilitiesGui.configureSwingUI();

        BrowserFrame frame = new BrowserFrame();

        GuiSwing.centerWindow( frame );

        frame.setVisible( true );
    }

    /**
     * Returns the BrowserPanel instance that fills this frame.
     *
     * @return
     */
    public BrowserPanel getBrowserPanel() {
        return browserPanel;
    }

    /**
     * Presents the user with a window indicating that an error has occurred. All errors that don't occur within the
     * context of a specific frame end up here.
     *
     * @param exception The exception that interrupted the application.
     */
    public void handleErrorOccurred( GTDInboxException exception ) {

        final JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty( "OptionPane.css" ) +
                        "<b>There was a problem that I wasn't expecting.</b><p>" +
                        exception.getMessage(),
                JOptionPane.WARNING_MESSAGE
        );
        Object[] options = { "Okay" };
        pane.setOptions( options );
        pane.setInitialValue( options[ 0 ] );
        pane.setIcon( ICON_APPLICATION_CAUTION );
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer( 0 )
        );

        JSheet.showSheet( pane, this, new SheetListener() {
            public void optionSelected( SheetEvent evt ) {
                evt.getValue();
            }
        } );
    }

    /**
     * Prompts the user to empty the trash.
     */
    public void handleConfirmEmptyTrash( String message ) {

        final JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty( "OptionPane.css" ) +
                        "<b>Are you sure you want to empty the trash?</b><p>" +
                        message,
                JOptionPane.WARNING_MESSAGE
        );
        final Object[] options = { "Cancel", "Okay" };
        pane.setOptions( options );
        pane.setInitialValue( options[ 0 ] );
        pane.setIcon( ICON_APPLICATION_CAUTION );
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer( 0 )
        );

        JSheet.showSheet( pane, this, new SheetListener() {
            public void optionSelected( SheetEvent evt ) {

                evt.getValue();

                if( evt.getValue().equals( options[ 1 ] ) ) {

                    browserPanel.doEmptyTrash();
                }
            }
        } );
    }

    // private methods

    /**
     * Sets up the listeners for the frame.
     */
    private void setupListeners() {

        // nothing is selected, disable options
        applicationMenuBar.getMenuItemEditContext().setEnabled( false );
        applicationMenuBar.getMenuItemRemoveContext().setEnabled( false );
        applicationMenuBar.getMenuItemEditProject().setEnabled( false );
        applicationMenuBar.getMenuItemRemoveProject().setEnabled( false );
        applicationMenuBar.getMenuItemAddActionItem().setEnabled( false );
        applicationMenuBar.getMenuItemEditActionItem().setEnabled( false );
        applicationMenuBar.getMenuItemRemoveActionItem().setEnabled( false );

        browserPanel.getModel().addPropertyChangeListener( "selectedTreeNode", new PropertyChangeListener() {

            public void propertyChange( PropertyChangeEvent propertyChangeEvent ) {

                GlazedTreeNode treeNode = ( GlazedTreeNode ) propertyChangeEvent.getNewValue();
                logger.debug( treeNode.getUserObject().getClass() );

                if( treeNode.getUserObject() instanceof Project ) {

                    applicationMenuBar.getMenuItemEditProject().setEnabled( true );
                    applicationMenuBar.getMenuItemRemoveProject().setEnabled( true );
                    applicationMenuBar.getMenuItemAddActionItem().setEnabled( true );
                } else {

                    applicationMenuBar.getMenuItemEditProject().setEnabled( false );
                    applicationMenuBar.getMenuItemRemoveProject().setEnabled( false );
                    applicationMenuBar.getMenuItemAddActionItem().setEnabled( false );
                }

                if( treeNode.getUserObject() instanceof Category ) {

                    //applicationMenuBar.getMenuItemEditCategory().setEnabled( true );
                    //applicationMenuBar.getMenuItemRemoveCategory().setEnabled( true );
                    //applicationMenuBar.getMenuItemAddReferenceItem().setEnabled( true );
                } else {

                    //applicationMenuBar.getMenuItemEditCategory().setEnabled( false );
                    //applicationMenuBar.getMenuItemRemoveCategory().setEnabled( false );
                    //applicationMenuBar.getMenuItemAddReferenceItem().setEnabled( false );
                }

                if( treeNode.getUserObject() instanceof InboxContext ) {

                    applicationMenuBar.getMenuItemEditContext().setEnabled( true );
                    applicationMenuBar.getMenuItemRemoveContext().setEnabled( true );
                } else {

                    applicationMenuBar.getMenuItemEditContext().setEnabled( false );
                    applicationMenuBar.getMenuItemRemoveContext().setEnabled( false );
                }
            }
        } );

        browserPanel.addBrowserPanelListener( new BrowserPanelListener() {

            public void detailPanelChanged() {

                JPanel panel = browserPanel.getPanelDetail();

                if( !( panel instanceof DetailProjectPanel ) ) {

                    applicationMenuBar.getMenuItemEditActionItem().setEnabled( false );
                    applicationMenuBar.getMenuItemRemoveActionItem().setEnabled( false );

                    if( ( panel instanceof SearchResultDetailPanel ) || ( panel instanceof TrashDetailForm )
                            || ( panel instanceof DetailArchivePanel ) ) {

                        applicationMenuBar.getMenuItemAddActionItem().setEnabled( false );
                    }
                }

                selectedDetailItemChanged();
            }

            public void selectedDetailItemChanged() {

                Object object = browserPanel.getPanelDetailSelectedItem();

                if( object != null && object instanceof ActionItem ) {
                    applicationMenuBar.getMenuItemEditActionItem().setEnabled( true );
                    applicationMenuBar.getMenuItemRemoveActionItem().setEnabled( true );
                } else {
                    applicationMenuBar.getMenuItemEditActionItem().setEnabled( false );
                    applicationMenuBar.getMenuItemRemoveActionItem().setEnabled( false );
                }
            }

            public void confirmEmptyTrash( String message ) {

                handleConfirmEmptyTrash( message );
            }
        } );
    }

    /**
     * Sets up the menu bar.
     */
    private void setupMenuBar() {

        actionListenerMenuBar = new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                if( actionEvent.getActionCommand().equals( "Add a Project..." ) ) {

                    browserPanel.doAddProject();
                } else if( actionEvent.getActionCommand().equals( "Remove Project..." ) ) {

                    if( browserPanel.getModel().getSelectedTreeNode() != null
                            && browserPanel.getModel().getSelectedTreeNode().getUserObject() instanceof Project ) {

                        browserPanel.doRemoveProject(
                                ( Project ) browserPanel.getModel().getSelectedTreeNode().getUserObject() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Edit Project..." ) ) {

                    if( browserPanel.getModel().getSelectedTreeNode() != null
                            && browserPanel.getModel().getSelectedTreeNode().getUserObject() instanceof Project ) {

                        browserPanel.doUpdateProject(
                                ( Project ) browserPanel.getModel().getSelectedTreeNode().getUserObject() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Add a Context..." ) ) {

                    browserPanel.doAddInboxContext();
                } else if( actionEvent.getActionCommand().equals( "Remove Context..." ) ) {

                    if( browserPanel.getModel().getSelectedTreeNode() != null
                            && browserPanel.getModel().getSelectedTreeNode().getUserObject() instanceof InboxContext ) {

                        browserPanel.doRemoveInboxContext(
                                ( InboxContext ) browserPanel.getModel().getSelectedTreeNode().getUserObject() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Edit Context..." ) ) {

                    if( browserPanel.getModel().getSelectedTreeNode() != null
                            && browserPanel.getModel().getSelectedTreeNode().getUserObject() instanceof InboxContext ) {

                        browserPanel.doUpdateInboxContext(
                                ( InboxContext ) browserPanel.getModel().getSelectedTreeNode().getUserObject() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Add a Category..." ) ) {

                    browserPanel.doAddCategory();
                } else if( actionEvent.getActionCommand().equals( "Remove Category..." ) ) {

                    if( browserPanel.getModel().getSelectedTreeNode() != null
                            && browserPanel.getModel().getSelectedTreeNode().getUserObject() instanceof Category ) {

                        browserPanel.doRemoveCategory(
                                ( Category ) browserPanel.getModel().getSelectedTreeNode().getUserObject() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Edit Category..." ) ) {

                    if( browserPanel.getModel().getSelectedTreeNode() != null
                            && browserPanel.getModel().getSelectedTreeNode().getUserObject() instanceof Category ) {

                        browserPanel.doUpdateCategory(
                                ( Category ) browserPanel.getModel().getSelectedTreeNode().getUserObject() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Add an Action Item..." ) ) {

                    if( browserPanel.getModel().getSelectedTreeNode() != null
                            && browserPanel.getModel().getSelectedTreeNode().getUserObject() instanceof Project ) {

                        browserPanel.doAddActionItem(
                                ( Project ) browserPanel.getModel().getSelectedTreeNode().getUserObject() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Edit Action Item..." ) ) {

                    if( browserPanel.getPanelDetail() instanceof DetailProjectPanel
                            && browserPanel.getPanelDetailSelectedItem() != null
                            && ( browserPanel.getPanelDetailSelectedItem() instanceof ActionItem ) ) {

                        browserPanel.doUpdateActionItem( ( ActionItem ) browserPanel.getPanelDetailSelectedItem() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Remove Action Item..." ) ) {

                    if( browserPanel.getPanelDetail() instanceof DetailProjectPanel
                            && browserPanel.getPanelDetailSelectedItem() != null
                            && ( browserPanel.getPanelDetailSelectedItem() instanceof ActionItem ) ) {

                        browserPanel.doRemoveActionItem( ( ActionItem ) browserPanel.getPanelDetailSelectedItem() );
                    }
                } else if( actionEvent.getActionCommand().equals( "Print..." ) ) {

                    // verify there's a panel being viewed
                    if( browserPanel.getPanelDetail() != null ) {

                        // print the panel
                        UtilitiesPrint.printComponent( browserPanel.getPanelDetail() );
                    }
                } else {

                    // pass other events out to the parent
                    GTDInboxGUI.getInstance().handleMenuAction( actionEvent );
                }
            }
        };

        // get a menu bar
        applicationMenuBar = new ApplicationMenuBar( actionListenerMenuBar );
        setJMenuBar( applicationMenuBar );
    }
}
