package com.nervestaple.gtdinbox.configuration;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.io.File;

import com.nervestaple.gtdinbox.configuration.application.NoStorageLocationException;
import com.nervestaple.gtdinbox.datastore.index.IndexManagerException;

/**
 * Provides a test suite for the ConfigurationFactory.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestConfigurationFactory extends TestCase {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    public void testGetConfigurationFactory() {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();

        try {
            configurationFactory.setTestingConfiguration( true );
        } catch( ConfigurationFactoryException e ) {
            logger.warn( e );
        }

        assertNotNull( configurationFactory );
    }

    public void testTestingConfiguration() {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();

        try {
            configurationFactory.setTestingConfiguration( true );
        } catch( ConfigurationFactoryException e ) {
            logger.info( e );
        }

        assertTrue( configurationFactory.isTestingConfiguration() );
    }

    public void testConfigure() throws ConfigurationFactoryException {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();

        try {
            configurationFactory.setTestingConfiguration( true );
        } catch( ConfigurationFactoryException e ) {
            logger.warn( e );
        }

        try {

            // configure the configuration
            configurationFactory.configure();
        } catch( ConfigurationFactoryException e ) {

            logger.info( e );

            if( e instanceof NoStorageLocationException ) {

                // set the default storage location
                logger.info( "Setting test data location" );

                File storage = configurationFactory.getApplicationConfiguration().createDefaultDataStorageLocation();

                configurationFactory.getApplicationConfiguration().setDataStorageLocation( storage );

                // try to configure the configuration again
                configurationFactory.configure();
            }
        }
    }

    public void testGetApplicationConfiguration() throws Exception {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();

        try {
            configurationFactory.setTestingConfiguration( true );
        } catch( ConfigurationFactoryException e ) {
            logger.warn( e );
        }

        try {

            // configure the configuration
            configurationFactory.configure();
        } catch( ConfigurationFactoryException e ) {

            logger.info( e );

            if( e instanceof NoStorageLocationException ) {

                // set the default storage location
                logger.info( "Setting test data location" );

                File storage = configurationFactory.getApplicationConfiguration().createDefaultDataStorageLocation();
                configurationFactory.getApplicationConfiguration().setDataStorageLocation( storage );

                // try to configure the configuration again
                configurationFactory.configure();
            }
        }

        assertNotNull( configurationFactory.getApplicationConfiguration() );
    }

    public void testGetHibernateConfiguration() throws Exception {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();

        try {
            configurationFactory.setTestingConfiguration( true );
        } catch( ConfigurationFactoryException e ) {
            logger.warn( e );
        }

        try {

            // configure the configuration
            configurationFactory.configure();
        } catch( ConfigurationFactoryException e ) {

            logger.info( e );

            if( e instanceof NoStorageLocationException ) {

                // set the default storage location
                logger.info( "Setting test data location" );

                File storage = configurationFactory.getApplicationConfiguration().createDefaultDataStorageLocation();
                configurationFactory.getApplicationConfiguration().setDataStorageLocation( storage );

                // try to configure the configuration again
                configurationFactory.configure();
            }
        }

        assertNotNull( configurationFactory.getHibernateConfiguration() );
    }
}
