package com.nervestaple.gtdinbox.configuration.hibernate;

import com.nervestaple.gtdinbox.configuration.application.ApplicationConfiguration;
import com.nervestaple.gtdinbox.configuration.application.ApplicationConfigurationException;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Properties;

/**
 * Provides an object for configuring Hibernate.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class HibernateConfiguration {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Hibernate configuration.
     */
    private Configuration configuration;

    /**
     * Application configuration.
     */
    private ApplicationConfiguration applicationConfiguration;

    /**
     * Flag to indicate if the configuration is for testing.
     */
    private boolean testingConfiguration = false;

    /**
     * Creates a new HibernateConfiguration.
     * @param applicationConfiguration application configuration
     */
    public HibernateConfiguration( ApplicationConfiguration applicationConfiguration ) {

        this.applicationConfiguration = applicationConfiguration;
    }

    /**
     * Configures the Hibernate configuration.
     * @throws HibernateException on problems configuring Hibernate
     */
    public void configure() throws HibernateConfigurationException {

        configuration = new Configuration();

        try {

            // let the configuration configure itself
            configuration.configure();
        } catch( HibernateException e ) {

            throw new HibernateConfigurationException( e );
        }

        // get the location for the database
        File databaseStorageLocation = applicationConfiguration.getDatabaseStorageLocation();
        logger.debug( "DatabaseStorageLocation: " + databaseStorageLocation );

        // setup runtime properties
        Properties properties = new Properties();
        if( databaseStorageLocation.exists() ) {

            properties.put( "hibernate.connection.url", "jdbc:derby:" + databaseStorageLocation.getAbsolutePath() );
        } else {

            properties.put( "hibernate.connection.url", "jdbc:derby:" + databaseStorageLocation.getAbsolutePath()
                    + ";create=true" );
        }

        // merge in our properties
        configuration.mergeProperties( properties );
        logger.debug( properties );
    }

    // accessor methods

    /**
     * Returns the Hibernate configuration.
     * @return
     */
    public Configuration getConfiguration() {
        return configuration;
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
     * @throws com.nervestaple.gtdinbox.configuration.application.ApplicationConfigurationException
     *          if the instance has already been configured
     */
    public void setTestingConfiguration( final boolean testingConfiguration ) throws ApplicationConfigurationException {

        if( configuration != null ) {

            throw new ApplicationConfigurationException(
                    "Cannot enter testing mode after configure() has been called" );
        }

        this.testingConfiguration = testingConfiguration;
    }
}
