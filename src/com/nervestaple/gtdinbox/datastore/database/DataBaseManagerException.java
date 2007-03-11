package com.nervestaple.gtdinbox.datastore.database;

import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.datastore.DataStoreException;

/**
 * Provides an exception object for the data store manager.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class DataBaseManagerException extends DataStoreException {

    /**
     * Returns a DataBaseManagerException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public DataBaseManagerException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a DataBaseManagerException with the given root throwable.
     *
     * @param exception Root exception
     */
    public DataBaseManagerException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an DataBaseManagerException with a custom message and a root exception that can then be thrown up the
     * chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public DataBaseManagerException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );
    }
}
