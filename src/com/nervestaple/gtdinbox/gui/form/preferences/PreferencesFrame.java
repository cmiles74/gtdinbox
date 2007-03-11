package com.nervestaple.gtdinbox.gui.form.preferences;

import ch.randelshofer.quaqua.JSheet;
import ch.randelshofer.quaqua.SheetEvent;
import ch.randelshofer.quaqua.SheetListener;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.configuration.application.CouldNotSaveConfigurationException;
import com.nervestaple.gtdinbox.configuration.application.NoStorageLocationException;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides a frame for editing the application preferences.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class PreferencesFrame extends JFrame {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Application icon small.
     */
    private final ImageIcon ICON_APPLICATION_SMALL;

    /**
     * Preferences Model.
     */
    private PreferencesModel model;

    /**
     * Preferences Panel.
     */
    private PreferencesPanel panel;

    public PreferencesFrame() {

        super( "Preferences" );

        ICON_APPLICATION_SMALL = new ImageIcon( getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/browser/images/preferences-16.png" ) );

        setIconImage( ICON_APPLICATION_SMALL.getImage() );

        model = new PreferencesModel();
        panel = new PreferencesPanel( model );

        getContentPane().add( panel );

        setupListeners();

        loadPreferences();

        setSize( 340, 170 );
    }

    // private methods

    private void setupListeners() {

        model.setActionListenerCancel( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                cancel();
            }
        } );

        model.setActionListenerSave( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                panel.commitValues();

                savePreferences();
            }
        } );
    }

    private void loadPreferences() {

        model.setArchiveDays( ConfigurationFactory.getInstance().getApplicationConfiguration().getArchiveDays() );
    }

    private void savePreferences() {

        try {

            ConfigurationFactory.getInstance().getApplicationConfiguration().setArchiveDays( model.getArchiveDays() );
            setVisible( false );
        } catch( CouldNotSaveConfigurationException e ) {
            handleErrorOccurred( e );
        } catch( NoStorageLocationException e ) {
            handleErrorOccurred( e );
        }
    }

    private void cancel() {

        setVisible( false );
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
}
