package com.nervestaple.gtdinbox.configuration.application;

import org.apache.log4j.Logger;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

/**
 * Provides a data model with the current application configuration.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ApplicationConfigurationModel {

    /**
     * Logger instance.
     */
    private transient Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Property change support object.
     */
    private transient PropertyChangeSupport propertychangesupport;

    /**
     * Data storage location.
     */
    private File dataStorageLocation;

    /**
     * Location for storing the database.
     */
    private File databaseStorageLocation;

    /**
     * Location for storing the index.
     */
    private File indexStorageLocation;

    /**
     * Number of days to archive.
     */
    private Integer archiveDays;

    public ApplicationConfigurationModel() {

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

    public File getDataStorageLocation() {
        return dataStorageLocation;
    }

    public void setDataStorageLocation( File dataStorageLocation ) {
        File valueOld = this.dataStorageLocation;
        this.dataStorageLocation = dataStorageLocation;
        propertychangesupport.firePropertyChange( "dataStorageLocation", valueOld, this.dataStorageLocation );
    }

    public File getDatabaseStorageLocation() {
        return databaseStorageLocation;
    }

    public void setDatabaseStorageLocation( File databaseStorageLocation ) {
        File valueOld = this.databaseStorageLocation;
        this.databaseStorageLocation = databaseStorageLocation;
        propertychangesupport.firePropertyChange( "databaseStorageLocation", valueOld, this.databaseStorageLocation );
    }

    public File getIndexStorageLocation() {
        return indexStorageLocation;
    }

    public void setIndexStorageLocation( File indexStorageLocation ) {
        File valueOld = this.indexStorageLocation;
        this.indexStorageLocation = indexStorageLocation;
        propertychangesupport.firePropertyChange( "indexStorageLocation", valueOld, this.indexStorageLocation );
    }

    public Integer getArchiveDays() {
        return archiveDays;
    }

    public void setArchiveDays( Integer archiveDays ) {
        Integer valueOld = this.archiveDays;
        this.archiveDays = archiveDays;
        propertychangesupport.firePropertyChange( "archiveDays", valueOld, this.archiveDays );
    }
}
