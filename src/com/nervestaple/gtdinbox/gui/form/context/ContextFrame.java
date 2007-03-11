package com.nervestaple.gtdinbox.gui.form.context;

import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.utility.swing.GuiSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a frame for editing an InboxContext.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ContextFrame extends JFrame {

    /**
     * The panel for this frame.
     */
    private ContextPanel panel;

    /**
     * Application icon small.
     */
    private final ImageIcon ICON_APPLICATION_SMALL;

    /**
     * Listeners for the frame.
     */
    private Set listeners;

    /**
     * Creates a new ContextFrame.
     */
    public ContextFrame() {

        super();

        panel = new ContextPanel();

        ICON_APPLICATION_SMALL = new ImageIcon( getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/browser/images/context-16.png" ) );

        setIconImage( ICON_APPLICATION_SMALL.getImage() );

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

        ContextFrame frame = new ContextFrame();

        GuiSwing.centerWindow( frame );

        frame.setVisible( true );
    }

    /**
     * Adds a new InboxContext to the data store.
     */
    public void addNewInboxContext() {

        setTitle( "Add a New Context" );
        panel.addNewInboxContext();
    }

    /**
     * Prompts the user to update the data for a InboxContext.
     *
     * @param inboxContext InboxContext to be updated
     */
    public void updateInboxContext( InboxContext inboxContext ) {

        setTitle( "Edit a Context" );

        try {
            panel.updateInboxContext( inboxContext );
        } catch( GTDInboxException e ) {

            handleErrorOccurred( e );
        }
    }

    /**
     * Adds a new listener to the form.
     *
     * @param listener Listener to handle the events
     */
    public void addInboxContextFormListener( ContextFormListener listener ) {

        if( !listeners.contains( listener ) ) {

            listeners.add( listener );
        }
    }

    /**
     * Removes a listener from the form.
     *
     * @param listener The listener to remove.
     */
    public void removeInboxContextFormListener( ContextFormListener listener ) {

        listeners.remove( listener );
    }

    // accessor methods

    public ContextPanel getPanel() {
        return panel;
    }

    // private methods

    private void fireInboxContextAdded( final InboxContext inboxContext ) {

        ContextFormListener[] listenersArray =
                ( ContextFormListener[] ) listeners.toArray( new ContextFormListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].inboxContextAdded( inboxContext );
        }

        if( isVisible() ) {

            setVisible( false );
        }
    }

    private void fireInboxContextUpdated( final InboxContext inboxContext ) {

        ContextFormListener[] listenersArray =
                ( ContextFormListener[] ) listeners.toArray( new ContextFormListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].inboxContextUpdated( inboxContext );
        }

        if( isVisible() ) {

            setVisible( false );
        }
    }

    private void fireErrorOccurred( final GTDInboxException exception ) {

        ContextFormListener[] listenersArray =
                ( ContextFormListener[] ) listeners.toArray( new ContextFormListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].exceptionOccurred( exception );
        }
    }

    private void handleErrorOccurred( GTDInboxException exception ) {

        JOptionPane pane = new JOptionPane(
                "<html>" + UIManager.getString( "OptionPane.css" ) +
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

        panel.addInboxContextFormListener( new ContextFormListener() {

            /**
             * Called when a new inboxContext is added.
             *
             * @param inboxContext InboxContext that was added
             */
            public void inboxContextAdded( InboxContext inboxContext ) {

                fireInboxContextAdded( inboxContext );
            }

            /**
             * Called when a new inboxContext is updated.
             *
             * @param inboxContext InboxContext that was updated
             */
            public void inboxContextUpdated( InboxContext inboxContext ) {

                fireInboxContextUpdated( inboxContext );
            }

            /**
             * Called when an error occurs.
             *
             * @param exception The exception that occurred
             */
            public void exceptionOccurred( GTDInboxException exception ) {

                handleErrorOccurred( exception );
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
