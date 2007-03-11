package com.nervestaple.gtdinbox.gui.form.preferences;

import org.apache.log4j.Logger;

import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Provides a data model for the Preferences form.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class PreferencesModel {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Number of days before a completed item is moved to the archive.
     */
    private Integer archiveDays;

    /**
     * Property change support object.
     */
    private transient PropertyChangeSupport propertychangesupport;

    /**
     * Action listener for the save button.
     */
    private transient ActionListener actionListenerSave;

    /**
     * Action listener for the cancel button.
     */
    private transient ActionListener actionListenerCancel;

    /**
     * Creates a new PreferencesModel.
     */
    public PreferencesModel() {

        propertychangesupport = new PropertyChangeSupport( this );
    }

    // property change support methods

    public void addPropertyChangeListener( PropertyChangeListener listener ) {
        propertychangesupport.addPropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener ) {
        propertychangesupport.removePropertyChangeListener( listener );
    }

    public void addPropertyChangeListener( String property,
                                           PropertyChangeListener listener ) {
        propertychangesupport.addPropertyChangeListener( property, listener );
    }

    public void removePropertyChangeListener( String property,
                                              PropertyChangeListener listener ) {
        propertychangesupport.removePropertyChangeListener( property, listener );
    }

    // accessor and mutator methods

    public Integer getArchiveDays() {
        return archiveDays;
    }

    public void setArchiveDays( Integer archiveDays ) {

        Integer valueOld = this.archiveDays;
        this.archiveDays = archiveDays;
        propertychangesupport.firePropertyChange( "archiveDays", valueOld, this.archiveDays );
    }

    public ActionListener getActionListenerSave() {
        return actionListenerSave;
    }

    public void setActionListenerSave( ActionListener actionListenerSave ) {
        this.actionListenerSave = actionListenerSave;
    }

    public ActionListener getActionListenerCancel() {
        return actionListenerCancel;
    }

    public void setActionListenerCancel( ActionListener actionListenerCancel ) {
        this.actionListenerCancel = actionListenerCancel;
    }
}
