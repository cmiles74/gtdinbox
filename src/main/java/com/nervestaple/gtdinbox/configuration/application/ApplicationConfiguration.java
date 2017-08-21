package com.nervestaple.gtdinbox.configuration.application;

import com.nervestaple.gtdinbox.datastore.index.IndexManager;
import com.nervestaple.gtdinbox.datastore.index.IndexManagerException;
import com.nervestaple.utility.Platform;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * Provides an object for configuring the application.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ApplicationConfiguration {

    /**
     * Data model to store the configuration settings.
     */
    private ApplicationConfigurationModel model;

    /**
     * Configuration instance.
     */
    private PropertiesConfiguration configuration;

    /**
     * Data storage location configuration key.
     */
    private final static String DATA_STORAGE_LOCATION_KEY = "dataStorageLocation";

    /**
     * Default storage directory name.
     */
    private final static String DEFAULT_STORAGE_DIR = "data";

    /**
     * Default database storage name.
     */
    private final static String DEFAULT_DATABASE_STORAGE_DIR = "database";

    /**
     * Default index storage name.
     */
    private final static String DEFAUL_INDEX_STORAGE_DIR = "index";

    /**
     * Number of days to archive key.
     */
    private final static String ARCHIVE_DAYS_KEY = "archiveDays";

    /**
     * Default number of days to archive.
     */
    private final static Integer DEFAULT_ARCHIVE_DAYS = new Integer( 7 );

    /**
     * Configure for testing.
     */
    private boolean testingConfiguration = false;

    /**
     * Creates a new ApplicationConfiguration.
     */
    public ApplicationConfiguration() {

        model = new ApplicationConfigurationModel();
    }

    // property change support methods

    public void addPropertyChangeListener( PropertyChangeListener listener ) {
        model.addPropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener ) {
        model.removePropertyChangeListener( listener );
    }

    public void addPropertyChangeListener( String property,
                                           PropertyChangeListener listener ) {
        model.addPropertyChangeListener( property, listener );
    }

    public void removePropertyChangeListener( String property,
                                              PropertyChangeListener listener ) {
        model.removePropertyChangeListener( property, listener );
    }

    // other methods

    /**
     * Configures the application's configuration
     *
     * @throws ApplicationConfigurationException
     *                               Problem configuring the application
     * @throws IndexManagerException Problems setting up the index
     */
    public void configure() throws ApplicationConfigurationException, IndexManagerException {

        String configurationPath = null;
        if( isTestingConfiguration() ) {

            configurationPath = "com.nervestaple.gtdinbox.properties";
        } else if( Platform.checkMacintosh() ) {

            configurationPath = getUserHomeDirectory().getAbsolutePath()
                    + "/Library/Preferences/com.nervestaple.gtdinbox.properties";
        } else if( getUserHomeDirectory() != null ) {

            configurationPath = getUserHomeDirectory().getAbsolutePath() + "/com.nervestaple.gtdinbox.properties";
        } else {

            configurationPath = "com.nervestaple.gtdinbox.properties";
        }

        File configurationFile = new File( configurationPath );

        try {
            configuration = new PropertiesConfiguration( configurationFile );
        } catch( ConfigurationException e ) {

            throw new ApplicationConfigurationException( e );
        }

        model.setDataStorageLocation( getDataStorageLocation() );

        model.setDatabaseStorageLocation( getDatabaseStorageLocation() );

        model.setIndexStorageLocation( getIndexStorageLocation() );

        // configure the index manager
        IndexManager.getInstance().configure();
    }

    /**
     * Returns the configuration.
     *
     * @return
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Returns the data storage location
     *
     * @return File
     * @throws NoStorageLocationException
     */
    public File getDataStorageLocation() throws NoStorageLocationException {

        if( model.getDatabaseStorageLocation() != null ) {

            return ( model.getDatabaseStorageLocation() );
        }

        // get the data storage location path
        String dataStorageLocationPath = configuration.getString( DATA_STORAGE_LOCATION_KEY );

        if( dataStorageLocationPath == null ) {

            throw new NoStorageLocationException( "There is no storage location for this configuration" );
        }

        model.setDataStorageLocation( new File( dataStorageLocationPath ) );

        if( !model.getDataStorageLocation().exists() ) {

            throw new NoStorageLocationException( "The storage location does not exist" );
        }

        if( !model.getDataStorageLocation().isDirectory() ) {

            throw new NoStorageLocationException( "The storage location is not a directory" );
        }

        if( !model.getDataStorageLocation().canWrite() ) {

            throw new NoStorageLocationException( "Cannot read from the storage location" );
        }

        if( !model.getDataStorageLocation().canWrite() ) {

            throw new NoStorageLocationException( "Cannot write to the storage location" );
        }

        return ( model.getDataStorageLocation() );
    }

    /**
     * Sets the data storage location. If the subdirectories do not exist, they are created.
     *
     * @param dataStorageLocation New storage location
     * @throws CouldNotSaveConfigurationException
     *                                    if the configuration couldn't be saved
     * @throws NoStorageLocationException if the storage location is bad
     */
    public void setDataStorageLocation( File dataStorageLocation ) throws CouldNotSaveConfigurationException,
            NoStorageLocationException {

        model.setDataStorageLocation( dataStorageLocation );

        configuration.setProperty( DATA_STORAGE_LOCATION_KEY, dataStorageLocation.getAbsolutePath() );

        try {
            configuration.save();
        } catch( ConfigurationException e ) {
            throw new CouldNotSaveConfigurationException( e );
        }

        // create the storage locaiton path
        if( !dataStorageLocation.exists() ) {

            try {
                dataStorageLocation.mkdir();
            } catch( Exception e ) {
                throw new NoStorageLocationException( e );
            }
        }

        // create the database storage location path
        String defaultDatabaseStorageLocationPath = dataStorageLocation.getAbsolutePath() + "/"
                + DEFAULT_DATABASE_STORAGE_DIR;

        // create the data storage location directory
        model.setDatabaseStorageLocation( new File( defaultDatabaseStorageLocationPath ) );

        // create the index storage location path
        String defaultIndexStorageLocationPath = dataStorageLocation.getAbsolutePath() + "/"
                + DEFAUL_INDEX_STORAGE_DIR;

        // create the data storage location directory
        model.setIndexStorageLocation( new File( defaultIndexStorageLocationPath ) );
    }

    /**
     * Creates the default data storage location and it's subdirectories. If you want to use this for the actual storage
     * directory, remember to set it as the storage location.
     *
     * @return File default storage location
     * @throws NoDefaultStorageLocationException
     *          The default storage location couldn't be created
     * @throws CouldNotSaveConfigurationException
     *          Could not set the storage subdirectory
     */
    public File createDefaultDataStorageLocation() throws NoDefaultStorageLocationException,
            CouldNotSaveConfigurationException {

        // get the default storage name
        String storagePathName = configuration.getString( "storage.dir" );

        if( storagePathName == null ) {

            storagePathName = DEFAULT_STORAGE_DIR;
            configuration.setProperty( "storage.dir", storagePathName );

            try {
                configuration.save();
            } catch( ConfigurationException e ) {
                throw new CouldNotSaveConfigurationException( e );
            }
        }

        // get the user's home directory
        File userHomeDirectory = getUserHomeDirectory();

        // create the default data storage location path
        String defaultStorageLocationPath = null;

        if( isTestingConfiguration() ) {

            defaultStorageLocationPath = storagePathName;
        } else if( Platform.checkMacintosh() ) {

            defaultStorageLocationPath = getUserHomeDirectory().getAbsolutePath()
                    + "/Library/Application Support/GTDInbox/" + storagePathName;
        } else if( getUserHomeDirectory() != null ) {

            defaultStorageLocationPath = getUserHomeDirectory().getAbsolutePath() + "/gtdInboxData/" + storagePathName;
        } else {

            defaultStorageLocationPath = "gtdInboxData/" + storagePathName;
        }

        // create the defaut data storage location directory
        File defaultStorageLocation = new File( defaultStorageLocationPath );

        defaultStorageLocation.mkdirs();

        if( !defaultStorageLocation.exists() ) {

            throw new NoDefaultStorageLocationException( "The default storage location does not exist" );
        }

        if( !defaultStorageLocation.isDirectory() ) {

            throw new NoDefaultStorageLocationException( "The default storage location is not a directory" );
        }

        if( !defaultStorageLocation.canWrite() ) {

            throw new NoDefaultStorageLocationException( "Cannot read from the default storage location" );
        }

        if( !defaultStorageLocation.canWrite() ) {

            throw new NoDefaultStorageLocationException( "Cannot write to the default storage location" );
        }

        // create the default database storage location path
        String defaultDatabaseStorageLocationPath = userHomeDirectory.getAbsolutePath() + "/"
                + storagePathName + "/" + DEFAULT_DATABASE_STORAGE_DIR;

        // create the defaut data storage location directory
        File defaultDatabaseStorageLocation = new File( defaultDatabaseStorageLocationPath );

        defaultDatabaseStorageLocation.mkdir();

        return ( defaultStorageLocation );
    }

    /**
     * Returns the database storage location.
     *
     * @return File
     */
    public File getDatabaseStorageLocation() {

        if( model.getDatabaseStorageLocation() == null ) {

            // create the database storage location path
            model.setDatabaseStorageLocation( new File( model.getDataStorageLocation().getAbsolutePath() + "/"
                    + DEFAULT_DATABASE_STORAGE_DIR ) );
        }

        return ( model.getDatabaseStorageLocation() );
    }

    /**
     * Returns the index storage location.
     *
     * @return File
     */
    public File getIndexStorageLocation() {

        if( model.getIndexStorageLocation() == null ) {

            model.setIndexStorageLocation( new File( model.getDataStorageLocation().getAbsolutePath() + "/"
                    + DEFAUL_INDEX_STORAGE_DIR ) );
        }

        return ( model.getIndexStorageLocation() );
    }

    /**
     * Returns true if the application is in the testing configuration.
     *
     * @return boolean
     */
    public boolean isTestingConfiguration() {
        return testingConfiguration;
    }

    /**
     * Tell the factory if it should configure itself for running in test mode.
     *
     * @param testingConfiguration boolean
     * @throws ApplicationConfigurationException
     *          if the instance has already been configured
     */
    public void setTestingConfiguration( final boolean testingConfiguration ) throws ApplicationConfigurationException {

        if( configuration != null ) {

            throw new ApplicationConfigurationException(
                    "Cannot enter testing mode after configure() has been called" );
        }

        this.testingConfiguration = testingConfiguration;
    }

    /**
     * Returns a file handle on the current user's home directory.
     *
     * @return File
     * @throws NoDefaultStorageLocationException
     *          if the user's home directory doesn't exist or isn't read/writeable
     */
    public File getUserHomeDirectory() throws NoDefaultStorageLocationException {

        // set the user's home directory
        String userHomeDirectoryPath = System.getProperty( "user.home" );

        if( userHomeDirectoryPath == null ) {

            throw new NoDefaultStorageLocationException( "Noe user home path" );
        }

        File userHomeDirectory = new File( userHomeDirectoryPath );

        if( !userHomeDirectory.exists() ) {

            throw new NoDefaultStorageLocationException( "User home path doesn't exist" );
        }

        if( !userHomeDirectory.isDirectory() ) {

            throw new NoDefaultStorageLocationException( "User home path doesn't resolve to a directory" );
        }

        if( !userHomeDirectory.canWrite() ) {

            throw new NoDefaultStorageLocationException( "Cannot write to the user home directory" );
        }

        return ( userHomeDirectory );
    }

    public Integer getArchiveDays() {

        if( model.getArchiveDays() == null ) {
            model.setArchiveDays( configuration.getInteger( ARCHIVE_DAYS_KEY, DEFAULT_ARCHIVE_DAYS ) );
        }

        return ( model.getArchiveDays() );
    }

    public void setArchiveDays( Integer value ) throws CouldNotSaveConfigurationException,
            NoStorageLocationException {

        model.setArchiveDays( value );

        configuration.setProperty( ARCHIVE_DAYS_KEY, value );

        try {
            configuration.save();
        } catch( ConfigurationException e ) {
            throw new CouldNotSaveConfigurationException( e );
        }
    }
}
