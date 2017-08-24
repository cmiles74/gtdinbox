package com.nervestaple.gtdinbox.datastore.database;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.internal.SessionImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

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

    /** Hibernate entity manager. */
    private EntityManager entityManager;

    /** Entity manager factory. */
    private EntityManagerFactory entityManagerFactory;

    static {

        DATA_BASE_MANAGER = new DataBaseManager();
    }

    /** Creates a new data store manager. */
    private DataBaseManager() {

        // get a configuration from the application's configuration factory
        entityManagerFactory = ConfigurationFactory.getInstance().getHibernateConfiguration().getEntityManagerFactory();

        // create the database schema, if it's missing
        if(!ConfigurationFactory.getInstance().isTestingConfiguration()) {
            try {
                createSchemaIfMissing();
            } catch (Exception e) {
                logger.warn(e);
            }
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
     * Returns a hibernate entity manager.
     *
     * @return entityManager
     * @throws DataBaseManagerException on failure to open a new session
     */
    public EntityManager getEntityManager() throws DataBaseManagerException {

        if( entityManager == null || !entityManager.isOpen() ) {

            try {
                logger.debug( "Creating a new entity manager" );
                entityManager = entityManagerFactory.createEntityManager();
            } catch( HibernateException e ) {
                logger.warn( e, e );
                throw new DataBaseManagerException( e );
            }
        }

        return ( entityManager );
    }

    /**
     * Closes the current entity manager.
     * <p/>
     * WARNING: You probably don't want to call this, we keep a long running session open for the life of the
     * application. We would only change this if we're connecting to a remote machine for our data.
     *
     * @throws DataBaseManagerException on failure to close the session.
     */
    public void closeEntityManager() throws DataBaseManagerException {

        if( entityManager != null && entityManager.isOpen() ) {

            try {
                entityManager.close();
                entityManager = null;
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

        if( getEntityManager().getTransaction() == null ||
                !getEntityManager().getTransaction().isActive() ) {

            try {
                getEntityManager().getTransaction().begin();
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

        if(getEntityManager().getTransaction() != null && getEntityManager().getTransaction().isActive()) {

            try {
                getEntityManager().getTransaction().commit();
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

        if(getEntityManager().getTransaction() != null && getEntityManager().getTransaction().isActive()) {

            try {
                getEntityManager().getTransaction().rollback();
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

    private Metadata getHibernateMetadata() throws DataBaseManagerException {

        Set<Class> entityClasses = new HashSet<>();

        SessionFactory sessionFactory = getEntityManager().getEntityManagerFactory().unwrap(SessionFactory.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().configure()
                .applySettings(sessionFactory.getProperties()).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        for(EntityType entityType : getEntityManager().getMetamodel().getEntities()) {

            if(!entityClasses.contains(entityType.getBindableJavaType())) {
                logger.info("Found entity: " + entityType.getBindableJavaType());
                entityClasses.add(entityType.getBindableJavaType());
                metadataSources.addAnnotatedClass(entityType.getBindableJavaType());
            }
        }

        return metadataSources.getMetadataBuilder().build();
    }

    /**
     * Creates the database schema if it is not already present.
     *
     * @throws DataBaseManagerException on failure to create the schema
     */
    public final void createSchema() throws DataBaseManagerException {

        // create a schema export instance for our configuration
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setHaltOnError(true);

        try {
            beginTransaction();
            schemaExport.createOnly(EnumSet.of(TargetType.DATABASE, TargetType.STDOUT), getHibernateMetadata());
            commitTransaction();
        } catch( Exception e ) {
            logger.warn( e, e );
        }

        logger.info( "Created database schema" );
    }

    /** Drops the current database schema. */
    public final void dropSchema() throws DataBaseManagerException {

        // create a schema export instance for our configuration
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setHaltOnError(true);

        beginTransaction();
        schemaExport.drop(EnumSet.of(TargetType.DATABASE, TargetType.STDOUT), getHibernateMetadata());
        commitTransaction();

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

            Connection connection = ((SessionImpl) getEntityManager()).connection();

            // check for the tables
            try {

                // get meta data
                String[] types = {"TABLE"};
                    ResultSet resultset = connection.getMetaData().
                            getTables( connection.getCatalog(), "%", "PROJECT", types );

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
