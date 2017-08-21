package com.nervestaple.gtdinbox.gui.form.actionitem;

import ca.odell.glazedlists.EventList;
import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.utility.swing.GuiSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a frame for editing ActionItem object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ActionItemFrame extends JFrame {

    /**
     * The panel for this frame.
     */
    private ActionItemPanel panel;

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
    public ActionItemFrame() {

        super();

        ICON_APPLICATION_SMALL = new ImageIcon( getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/browser/images/action-16.png" ) );

        setIconImage( ICON_APPLICATION_SMALL.getImage() );

        panel = new ActionItemPanel();

        initializeFrame();

        setupListeners();

        setResizable( false );
    }

    /**
     * Bootstraps the application.
     *
     * @param args Command line arguments
     */
    public static void main( final String[] args ) {

        UtilitiesGui.configureSwingUI();

        ActionItemFrame frame = new ActionItemFrame();

        GuiSwing.centerWindow( frame );

        frame.setVisible( true );
    }

    /**
     * Adds a new ActionItem to the data store.
     *
     * @param project Project to be the parnent of the ActionItem
     */
    public void addNewActionItem( Project project ) {

        setTitle( "Add a New Action Item" );
        panel.addNewActionItem( project );
    }

    /**
     * Prompts the user to update the data for a ActionItem.
     *
     * @param actionItem ActionItem to be updated
     */
    public void updateActionItem( ActionItem actionItem ) {

        setTitle( "Edit an Action Item" );

        try {
            panel.updateActionItem( actionItem );
        } catch( GTDInboxException e ) {

            handleErrorOccurred( e );
        }
    }

    /**
     * Adds a new listener to the form.
     *
     * @param listener Listener to handle the events
     */
    public void addActionItemFormListener( ActionItemFormListener listener ) {

        if( !listeners.contains( listener ) ) {

            listeners.add( listener );
        }
    }

    /**
     * Removes a listener from the form.
     *
     * @param listener The listener to remove.
     */
    public void removeActionItemListener( ActionItemFormListener listener ) {

        listeners.remove( listener );
    }

    // accessor methods

    public void setListContexts( EventList listContexts ) {

        panel.setListContexts( listContexts );
    }

    public void setListProjects( EventList listProjects ) {

        panel.setListProjects( listProjects );
    }

    public ActionItemPanel getPanel() {
        return panel;
    }

    // private methods

    private void fireActionItemAdded( final ActionItem actionItem ) {

        ActionItemFormListener[] listenersArray =
                ( ActionItemFormListener[] ) listeners.toArray( new ActionItemFormListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].actionItemAdded( actionItem );
        }

        if( isVisible() ) {

            setVisible( false );
        }
    }

    private void fireActionItemUpdated( final ActionItem actionItem ) {

        ActionItemFormListener[] listenersArray =
                ( ActionItemFormListener[] ) listeners.toArray( new ActionItemFormListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].actionItemUpdated( actionItem );
        }

        if( isVisible() ) {

            setVisible( false );
        }
    }

    private void fireErrorOccurred( final GTDInboxException exception ) {

        ActionItemFormListener[] listenersArray =
                ( ActionItemFormListener[] ) listeners.toArray( new ActionItemFormListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].exceptionOccurred( exception );
        }
    }

    private void handleErrorOccurred( GTDInboxException exception ) {

        JOptionPane pane = new JOptionPane(
                "<html>" + System.getProperty( "OptionPane.css" ) +
                        "<b>There was a problem that I wasn't expecting.</b><p>" +
                        exception.getMessage(),
                JOptionPane.WARNING_MESSAGE
        );
        Object[] options = { "Okay" };
        pane.setOptions( options );
        pane.setInitialValue( options[ 0 ] );
        pane.putClientProperty(
                "Quaqua.OptionPane.destructiveOption", new Integer( 0 )
        );
        JSheet.showSheet( pane, this, new SheetListener() {
            public void optionSelected( SheetEvent evt ) {
                evt.getValue();
            }
        } );
    }

    private void setupListeners() {

        panel.addActionItemFormListener( new ActionItemFormListener() {


            /**
             * Called when a new ActionItem is added.
             *
             * @param actionItem ActionItem that was added
             */
            public void actionItemAdded( ActionItem actionItem ) {

                fireActionItemAdded( actionItem );
            }

            /**
             * Called when a new ActionItem is updated.
             *
             * @param actionItem ActionItem that was updated
             */
            public void actionItemUpdated( ActionItem actionItem ) {

                fireActionItemUpdated( actionItem );
            }

            /**
             * Called when an error occurs.
             *
             * @param exception The exception that occurred
             */
            public void exceptionOccurred( GTDInboxException exception ) {

                fireErrorOccurred( exception );
            }
        } );

        panel.setActionListenerCancel( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                setVisible( false );
            }
        } );
    }

    /**
     * Sets up the frame
     */
    private void initializeFrame() {

        getContentPane().setLayout( new GridLayout( 1, 1 ) );
        getContentPane().add( panel );

        pack();

        listeners = new HashSet();
    }
}
