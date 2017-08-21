package com.nervestaple.gtdinbox.datastore.database;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.datastore.index.indexinterceptor.IndexInterceptor;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides an object for managing the data store. This is a singleton instance.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class DataBaseManager {

    /** Logger instances. */
    private Logger logger = Logger.getLogger( this.getClass() );

    /** Data store manager instance. */
    private final static DataBaseManager DATA_BASE_MANAGER;

    /** Hibernate configuration. */
    private Configuration configuration;

    /** Session factory. */
    private SessionFactory sessionFactory;

    /** Interceptor to index objects in the database. */
    private Interceptor indexInterceptor;

    /** Thread local instance for the session. */
    private Session session;

    /** Thread local instance for the transaction. */
    private Transaction transaction;

    static {

        DATA_BASE_MANAGER = new DataBaseManager();
    }

    /** Creates a new data store manager. */
    private DataBaseManager() {

        // create a new index interceptor
        indexInterceptor = new IndexInterceptor();

        // get a configuration from the application's configuration factory
        configuration = ConfigurationFactory.getInstance().getHibernateConfiguration().getConfiguration();

        // setup the session factory and pass it the interceptior
        try {
            sessionFactory = configuration.buildSessionFactory();
        } catch( HibernateException e ) {
            logger.warn( e );
        }

        // create the database schema, if it's missing
        try {
            createSchemaIfMissing();
        } catch( Exception e ) {
            logger.warn( e );
        }
    }

    /**
     * Returns the singleton instance of the data store manager.
     *
     * @return data store manager
     */
    public static DataBaseManager getInstance() {

        return ( DATA_BASE_MANAGER );
    }

    /**
     * Returns a hibernate session.
     *
     * @return session
     * @throws DataBaseManagerException on failure to open a new session
     */
    public Session getSession() throws DataBaseManagerException {

        if( session == null || !session.isConnected() ) {

            try {
                logger.debug( "Creating a new Session" );
                session = sessionFactory.openSession( indexInterceptor );
            } catch( HibernateException e ) {
                logger.warn( e );
                throw new DataBaseManagerException( e );
            }
        }

        return ( session );
    }

    /**
     * Closes the current session.
     * <p/>
     * WARNING: You probably don't want to call this, we keep a long running session open for the life of the
     * application. We would only change this if we're connecting to a remote machine for our data.
     *
     * @throws DataBaseManagerException on failure to close the session.
     */
    public void closeSession() throws DataBaseManagerException {

        if( session != null && session.isOpen() ) {

            try {
                session.close();
                session = null;
            } catch( HibernateException e ) {
                logger.warn( e );
                throw new DataBaseManagerException( e );
            }
        }
    }

    /**
     * Starts a new transaction for the current session.
     *
     * @throws DataBaseManagerException on failure to start a new transaction
     */
    public void beginTransaction() throws DataBaseManagerException {

        if( transaction == null || !transaction.isActive() ) {

            try {
                transaction = getSession().beginTransaction();
            } catch( HibernateException e ) {
                logger.warn( e );
                throw new DataBaseManagerException( e );
            }
        }
    }

    /**
     * Commits the current transaction. If the commit fails, the transaction will be rolled back.
     *
     * @throws DataBaseManagerException on failure to commit the transaction
     */
    public void commitTransaction() throws DataBaseManagerException {

        if( transaction != null && !transaction.wasCommitted() && !transaction.wasRolledBack() ) {

            try {
                transaction.commit();
                transaction = null;
            } catch( HibernateException e ) {

                logger.warn( e );

                try {
                    rollbackTransaction();
                } catch( DataBaseManagerException e1 ) {
                    logger.warn( e1 );
                }
            }
        }
    }

    /**
     * Rolls back the current transaction.
     *
     * @throws DataBaseManagerException on failure to roll back the transaction
     */
    public void rollbackTransaction() throws DataBaseManagerException {

        if( transaction != null && transaction.isActive() && !transaction.wasCommitted()
                && !transaction.wasRolledBack() ) {

            try {
                transaction.rollback();
                transaction = null;
            } catch( HibernateException e ) {

                logger.warn( e );
                throw new DataBaseManagerException( e );
            }
        }
    }

    /**
     * Creates the database schema if it is not present. This method is not destructive.
     *
     * @throws DataBaseManagerException on failure to create the schema or check for its existence
     */
    public final void createSchemaIfMissing() throws DataBaseManagerException {

        if( !schemaExists() ) {

            createSchema();
        }
    }

    /**
     * Creates the database schema if it is not already present.
     *
     * @throws DataBaseManagerException on failure to create the schema
     */
    public final void createSchema() throws DataBaseManagerException {

        // create a schema export instance for our configuration
        SchemaExport schemaExport = new SchemaExport( configuration );

        // create the schema
        try {
            schemaExport.create( true, true );
        } catch( Exception e ) {
            logger.info( e );
        }

        logger.info( "Created database schema" );
    }

    /** Drops the current database schema. */
    public final void dropSchema() {

        // create a schema export instance for our configuration
        SchemaExport schemaExport = new SchemaExport( configuration );

        // drop the schema
        schemaExport.drop( true, true );

        logger.info( "Removed database schema" );
    }

    /**
     * Drops the current database schema and then re-creates it.
     * <p/>
     * NOTE: this removes all data from the database.
     *
     * @throws DataBaseManagerException on problems dropping or creating the schema
     */
    public final void rebuildDatabaseSchema() throws DataBaseManagerException {

        dropSchema();

        createSchema();
    }

    /**
     * Returns true if the schema is present, false if not.
     *
     * @return boolean
     * @throws DataBaseManagerException on problems getting a session
     */
    public final boolean schemaExists() throws DataBaseManagerException {

        boolean booleanExists = false;

        // get a session and connection
        try {

            Session session = getSession();
            Connection connection = session.connection();

            // check for the tables
            try {

                // get meta data
                ResultSet resultset = connection.getMetaData().getTables( null, null, "PROJECTS", null );

                // loop through the result set for the actual results
                if( resultset.next() ) {
                    booleanExists = true;
                }
            } catch( SQLException e ) {
                logger.warn( e );
            }
        } catch( DataBaseManagerException e ) {
            throw new DataBaseManagerException( e );
        }

        return ( booleanExists );
    }
}
