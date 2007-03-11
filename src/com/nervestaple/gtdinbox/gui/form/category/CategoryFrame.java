package com.nervestaple.gtdinbox.gui.form.category;

import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.utility.swing.GuiSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a frame for editing a category.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class CategoryFrame extends JFrame {

    /**
     * The panel for this frame.
     */
    private CategoryPanel panel;

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
    public CategoryFrame() {

        super();

        panel = new CategoryPanel();

        ICON_APPLICATION_SMALL = new ImageIcon( getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/browser/images/category-16.png" ) );

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

        CategoryFrame frame = new CategoryFrame();

        GuiSwing.centerWindow( frame );

        frame.setVisible( true );
    }

    /**
     * Adds a new Category to the data store.
     */
    public void addNewCategory() {

        setTitle( "Add a New Category" );
        panel.addNewCategory();
    }

    /**
     * Prompts the user to update the data for a Category.
     *
     * @param category Category to be updated
     */
    public void updateCategory( Category category ) {

        setTitle( "Edit a Category" );

        try {
            panel.updateCategory( category );
        } catch( GTDInboxException e ) {

            handleErrorOccurred( e );
        }
    }

    /**
     * Adds a new listener to the form.
     *
     * @param listener Listener to handle the events
     */
    public void addCategoryFormListener( CategoryFormListener listener ) {

        if( !listeners.contains( listener ) ) {

            listeners.add( listener );
        }
    }

    /**
     * Removes a listener from the form.
     *
     * @param listener The listener to remove.
     */
    public void removeCategoryListener( CategoryFormListener listener ) {

        listeners.remove( listener );
    }

    // accessor methods

    public CategoryPanel getPanel() {
        return panel;
    }

    // private methods

    private void fireCategoryAdded( final Category category ) {

        CategoryFormListener[] listenersArray =
                ( CategoryFormListener[] ) listeners.toArray( new CategoryFormListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].categoryAdded( category );
        }

        if( isVisible() ) {

            setVisible( false );
        }
    }

    private void fireCategoryUpdated( final Category category ) {

        CategoryFormListener[] listenersArray =
                ( CategoryFormListener[] ) listeners.toArray( new CategoryFormListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].categoryUpdated( category );
        }

        if( isVisible() ) {

            setVisible( false );
        }
    }

    private void fireErrorOccurred( final GTDInboxException exception ) {

        CategoryFormListener[] listenersArray =
                ( CategoryFormListener[] ) listeners.toArray( new CategoryFormListener[listeners.size()] );

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

        panel.addCategoryFormListener( new CategoryFormListener() {


            /**
             * Called when a new Category is added.
             *
             * @param category Category that was added
             */
            public void categoryAdded( Category category ) {

                fireCategoryAdded( category );
            }

            /**
             * Called when a new Category is updated.
             *
             * @param category Category that was updated
             */
            public void categoryUpdated( Category category ) {

                fireCategoryUpdated( category );
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
