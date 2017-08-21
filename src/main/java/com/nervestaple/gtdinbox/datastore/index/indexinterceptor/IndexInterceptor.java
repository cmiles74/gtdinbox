package com.nervestaple.gtdinbox.datastore.index.indexinterceptor;

import org.hibernate.Interceptor;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.apache.log4j.Logger;
import org.apache.commons.beanutils.BeanMap;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.datastore.index.IndexManager;
import com.nervestaple.gtdinbox.datastore.index.IndexManagerException;

/**
 * Provides an object that will hook into calls to Hibernate and maintain the Lucene index.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class IndexInterceptor implements Interceptor {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Set of insert operations.
     */
    private Set inserts = new HashSet();

    /**
     * Set of delete operations.
     */
    private Set deletes = new HashSet();

    /**
     * Set of update operations.
     */
    private Set updates = new HashSet();

    /**
     * Creates a new IndexInterceptor.
     */
    public IndexInterceptor() {

        logger.debug( "IndexInterceptor created" );
    }

    // interceptor methods

    /**
     * Called just before an object is initialized. The interceptor may change the <tt>state</tt>, which will be propagated
     * to the persistent object. Note that when this method is called, <tt>entity</tt> will be an empty uninitialized
     * instance of the class.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
    public boolean onLoad( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types )
            throws CallbackException {
        return false;
    }

    /**
     * Called when an object is detected to be dirty, during a flush. The interceptor may modify the detected
     * <tt>currentState</tt>, which will be propagated to both the database and the persistent object. Note that not all
     * flushes end in actual synchronization with the database, in which case the new <tt>currentState</tt> will be
     * propagated to the object, but not necessarily (immediately) to the database. It is strongly recommended that the
     * interceptor <b>not</b> modify the <tt>previousState</tt>.
     *
     * @return <tt>true</tt> if the user modified the <tt>currentState</tt> in any way.
     */
    public boolean onFlushDirty( Object entity, Serializable id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types ) throws CallbackException {

        if( entity instanceof Indexable ) {

            logger.debug( "Adding " + entity.getClass() + " to updates list" );
            updates.add( entity );
        }

        return false;
    }

    /**
     * Called before an object is saved. The interceptor may modify the <tt>state</tt>, which will be used for the SQL
     * <tt>INSERT</tt> and propagated to the persistent object.
     *
     * @return <tt>true</tt> if the user modified the <tt>state</tt> in any way.
     */
    public boolean onSave( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types )
            throws CallbackException {

        if( entity instanceof Indexable ) {

            logger.debug( "Adding " + entity.getClass() + " to inserts list" );
            inserts.add( entity );
        }

        return false;
    }

    /** Called before an object is deleted. It is not recommended that the interceptor modify the <tt>state</tt>. */
    public void onDelete( Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types )
            throws CallbackException {

        if( entity instanceof Indexable ) {

            logger.debug( "Adding " + entity.getClass() + " to delete list" );
            deletes.add( entity );
        }
    }

    /** Called before a collection is (re)created. */
    public void onCollectionRecreate( Object collection, Serializable key ) throws CallbackException {

        // do nothing
    }

    /** Called before a collection is deleted. */
    public void onCollectionRemove( Object collection, Serializable key ) throws CallbackException {

        // do nothing
    }

    /** Called before a collection is updated. */
    public void onCollectionUpdate( Object collection, Serializable key ) throws CallbackException {

        // do nothing
    }

    /** Called before a flush */
    public void preFlush( Iterator entities ) throws CallbackException {

        // do nothing
    }

    /**
     * Called after a flush that actually ends in execution of the SQL statements required to synchronize in-memory state
     * with the database.
     */
    public void postFlush( Iterator entities ) throws CallbackException {

        logger.debug( "postFlush() handler called" );
        
        Iterator iteratorUpdates = updates.iterator();
        while( iteratorUpdates.hasNext() ) {

            Object object = iteratorUpdates.next();

            logger.debug( "Updating index for " + object.getClass() );

            try {
                IndexManager.getInstance().updateIndexable( ( Indexable ) object );
            } catch( IndexManagerException e ) {
                logger.warn( e );
            }
        }
        updates.clear();

        Iterator iteratorInserts = inserts.iterator();
        while( iteratorInserts.hasNext() ) {

            Object object = iteratorInserts.next();

            logger.debug( "Adding to index for " + object.getClass() );

            try {
                IndexManager.getInstance().addIndexable( ( Indexable ) object );
            } catch( IndexManagerException e ) {
                logger.warn( e );
            }
        }
        inserts.clear();

        Iterator iteratorDeletes = deletes.iterator();
        while( iteratorDeletes.hasNext() ) {

            Object object = iteratorDeletes.next();

            logger.debug( "Removing from index for " + object.getClass() );

            try {
                IndexManager.getInstance().removeIndexable( ( Indexable ) object );
            } catch( IndexManagerException e ) {
                logger.warn( e );
            }
        }
        deletes.clear();
    }

    /**
     * Called to distinguish between transient and detached entities. The return value determines the state of the entity
     * with respect to the current session. <ul> <li><tt>Boolean.TRUE</tt> - the entity is transient
     * <li><tt>Boolean.FALSE</tt> - the entity is detached <li><tt>null</tt> - Hibernate uses the <tt>unsaved-value</tt>
     * mapping and other heuristics to determine if the object is unsaved </ul>
     *
     * @param entity a transient or detached entity
     * @return Boolean or <tt>null</tt> to choose default behaviour
     */
    public Boolean isTransient( Object entity ) {
        return null;
    }

    /**
     * Called from <tt>flush()</tt>. The return value determines whether the entity is updated <ul> <li>an array of
     * property indices - the entity is dirty <li>an empty array - the entity is not dirty <li><tt>null</tt> - use
     * Hibernate's default dirty-checking algorithm </ul>
     *
     * @param entity a persistent entity
     * @return array of dirty property indices or <tt>null</tt> to choose default behaviour
     */
    public int[] findDirty( Object entity, Serializable id, Object[] currentState, Object[] previousState,
            String[] propertyNames, Type[] types ) {
        return null;
    }

    /**
     * Instantiate the entity class. Return <tt>null</tt> to indicate that Hibernate should use the default constructor of
     * the class. The identifier property of the returned instance should be initialized with the given identifier.
     *
     * @param entityName the name of the entity
     * @param entityMode The type of entity instance to be returned.
     * @param id         the identifier of the new instance
     * @return an instance of the class, or <tt>null</tt> to choose default behaviour
     */
    public Object instantiate( String entityName, EntityMode entityMode, Serializable id ) throws CallbackException {
        return null;
    }

    /**
     * Get the entity name for a persistent or transient instance
     *
     * @param object an entity instance
     * @return the name of the entity
     */
    public String getEntityName( Object object ) throws CallbackException {
        return null;
    }

    /**
     * Get a fully loaded entity instance that is cached externally
     *
     * @param entityName the name of the entity
     * @param id         the instance identifier
     * @return a fully initialized entity
     * @throws org.hibernate.CallbackException
     *
     */
    public Object getEntity( String entityName, Serializable id ) throws CallbackException {
        return null;
    }

    /**
     * Called when a Hibernate transaction is begun via the Hibernate <tt>Transaction</tt> API. Will not be called if
     * transactions are being controlled via some other mechanism (CMT, for example).
     */
    public void afterTransactionBegin( Transaction tx ) {

        // do nothing
    }

    /** Called before a transaction is committed (but not before rollback). */
    public void beforeTransactionCompletion( Transaction tx ) {

        // do nothing
    }

    /** Called after a transaction is committed or rolled back. */
    public void afterTransactionCompletion( Transaction tx ) {

        // do nothing
    }

    /**
     * Called when sql string is being prepared.
     *
     * @param sql sql to be prepared
     * @return original or modified sql
     */
    public String onPrepareStatement( String sql ) {
        return sql;
    }
}
