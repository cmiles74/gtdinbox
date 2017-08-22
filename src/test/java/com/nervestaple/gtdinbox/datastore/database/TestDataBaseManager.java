package com.nervestaple.gtdinbox.datastore.database;

import junit.framework.TestCase;
import org.hibernate.Session;
import org.apache.log4j.Logger;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactoryException;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.configuration.application.NoStorageLocationException;

import java.io.File;

/**
 * Provides a test suite for the DataBaseManager object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestDataBaseManager extends TestCase {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    public void setUp() throws Exception {

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

            logger.info( e, e );

            if( e instanceof NoStorageLocationException ) {

                // create the default storage location
                logger.info( "Setting data storage location" );
                File storage = configurationFactory.getApplicationConfiguration().createDefaultDataStorageLocation();
                configurationFactory.getApplicationConfiguration().setDataStorageLocation( storage );

                // try to configure the configuration again
                configurationFactory.configure();
            }
        }
    }

    public void tearDown() {

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        try {
            if( dataBaseManager.schemaExists() ) {
                dataBaseManager.dropSchema();
            }
        } catch( Exception e ) {
            logger.info( e );
        }
    }

    public void testGetInstance() {

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        assertNotNull( dataBaseManager );
    }

    public void testGetSession() {

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        Session session = null;
        try {
            session = dataBaseManager.getSession();
        } catch( DataBaseManagerException e ) {

            logger.info( e );
        }

        assertNotNull( session );
    }

    public void testCloseSession() throws Exception{

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        Session session = session = dataBaseManager.getSession();

        dataBaseManager.closeSession();

        assertFalse( session.isOpen() );

        assertFalse( session.isOpen() );
    }

    public void testBeginTransaction() throws Exception {

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        Session session = dataBaseManager.getSession();

        dataBaseManager.beginTransaction();

        assertNotNull( session.getTransaction() );
    }

    public void testEndTransaction() throws DataBaseManagerException {

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        try {
            logger.info( "Opening session..." );
            dataBaseManager.getSession();
        } catch( DataBaseManagerException e ) {
            logger.info( e );
        }

        logger.info( "Starting transaction..." );
        dataBaseManager.beginTransaction();

        logger.info( dataBaseManager.getSession() );
        logger.info( dataBaseManager.getSession().getTransaction() );
        logger.info( "Session open? " + dataBaseManager.getSession().isOpen() );

        logger.debug( "Committing transaction..." );
        dataBaseManager.commitTransaction();
    }

    public void testRollbackTransaction() throws DataBaseManagerException {

        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        try {
            dataBaseManager.getSession();
        } catch( DataBaseManagerException e ) {

            logger.info( e );
        }

        try {
            dataBaseManager.beginTransaction();
        } catch( DataBaseManagerException e ) {
            logger.info( e );
        }

        dataBaseManager.rollbackTransaction();
    }

    public void testSchemaExists() throws DataBaseManagerException {

        // get a usermanager
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        dataBaseManager.schemaExists();

        assertTrue( true );
    }

    public void testCreateSchemaIfNeeded() {

        // get a usermanager
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        try {
            dataBaseManager.createSchemaIfMissing();
        } catch( DataBaseManagerException e ) {
            logger.info( e );
        }

        boolean exists = false;
        try {
            exists = dataBaseManager.schemaExists();
        } catch( DataBaseManagerException e ) {
            logger.info( e );
        }

        assertTrue( exists );
    }

    public void testCreateSchema() throws Exception {

        // get a usermanager
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        // create the schema
        dataBaseManager.createSchema();

        boolean exists = false;
        try {
            exists = dataBaseManager.schemaExists();
        } catch( DataBaseManagerException e ) {
            logger.info( e );
        }

        assertTrue( exists );
    }

    public void testDropSchema() throws Exception {

        // get a usermanager
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        // drop the schema
        dataBaseManager.dropSchema();

        boolean exists = false;
        try {
            exists = dataBaseManager.schemaExists();
        } catch( DataBaseManagerException e ) {
            logger.info( e );
        }

        assertFalse( exists );
    }

    public void testRebuildSchema() throws Exception {

        // get a usermanager
        DataBaseManager dataBaseManager = DataBaseManager.getInstance();

        dataBaseManager.rebuildDatabaseSchema();

        boolean exists = false;
        try {
            exists = dataBaseManager.schemaExists();
        } catch( DataBaseManagerException e ) {
            logger.info( e );
        }

        assertTrue( exists );
    }
}
