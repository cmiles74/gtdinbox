package com.nervestaple.gtdinbox.configuration;

import com.nervestaple.gtdinbox.configuration.application.ApplicationConfiguration;
import com.nervestaple.gtdinbox.configuration.hibernate.HibernateConfiguration;
import com.nervestaple.gtdinbox.datastore.index.IndexManagerException;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;

/**
 * Provides an object for managing the application's configuration. This class is a singleton, there can be only
 * one per application instance.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ConfigurationFactory {

    /**
     * Application configuration.
     */
    private ApplicationConfiguration CONFIGURATION_APPLICATION;

    /**
     * Hibernate configuration.
     */
    private HibernateConfiguration CONFIGURATION_HIBERNATE;

    /**
     * Singleton instance.
     */
    private final static ConfigurationFactory CONFIGURATION_FACTORY;

    /** Configure for testing. */
    private boolean testingConfiguration = false;

    static {
        CONFIGURATION_FACTORY = new ConfigurationFactory();
    }

    /**
     * Creates a new ConfigurationFactory.
     */
    private ConfigurationFactory() {

    }

    /**
     * Returns true if the application is in the testing configuration.
     * @return boolean
     */
    public boolean isTestingConfiguration() {
        return testingConfiguration;
    }

    /**
     * Tell the factory if it should configure itself for running in test mode.
     * @param testingConfiguration boolean
     * @throws ConfigurationFactoryException if the instance has already been configured
     */
    public void setTestingConfiguration( final boolean testingConfiguration ) throws ConfigurationFactoryException {

        if( CONFIGURATION_APPLICATION != null ) {

            throw new ConfigurationFactoryException( "Cannot enter testing mode after configure() has been called" );
        }

        this.testingConfiguration = testingConfiguration;
    }

    /**
     * Returns the singleton instance.
     * @return ConfigurationFactory
     */
    public static ConfigurationFactory getInstance() {

        return( CONFIGURATION_FACTORY );
    }

    /**
     * Configures this configuration.
     * @throws ConfigurationFactoryException problems loading or building the configuration
     */
    public void configure() throws ConfigurationFactoryException {

        // create an application configuration
        CONFIGURATION_APPLICATION = new ApplicationConfiguration();
        CONFIGURATION_APPLICATION.setTestingConfiguration( testingConfiguration );

        try {

            // configure the application configuration
            CONFIGURATION_APPLICATION.configure();
        } catch( IndexManagerException e ) {

            throw new ConfigurationFactoryException( "Failed to setup the index", e );
        }

        // create a hibernate configuration
        CONFIGURATION_HIBERNATE = new HibernateConfiguration( CONFIGURATION_APPLICATION );
        CONFIGURATION_HIBERNATE.setTestingConfiguration( testingConfiguration );

        // configure the hibernate configuration
        CONFIGURATION_HIBERNATE.configure();

        // get the data store manager ready
        DataBaseManager.getInstance();
    }

    // accessor methods

    /**
     * Returns the application configuration.
     * @return
     */
    public ApplicationConfiguration getApplicationConfiguration() {

        return CONFIGURATION_APPLICATION;
    }

    /**
     * Returns the Hibernate configuration.
     * @return
     */
    public HibernateConfiguration getHibernateConfiguration() {
        
        return CONFIGURATION_HIBERNATE;
    }
}
