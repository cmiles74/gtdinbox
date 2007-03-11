package com.nervestaple.gtdinbox.datastore;

import com.nervestaple.gtdinbox.GTDInboxException;

/**
 * Provides an exception object for the data store manager.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class DataStoreManagerException extends GTDInboxException {

    /**
     * Returns a DataStoreManagerException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public DataStoreManagerException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a DataStoreManagerException with the given root throwable.
     *
     * @param exception Root exception
     */
    public DataStoreManagerException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an DataStoreManagerException with a custom message and a root exception that can then be thrown up the
     * chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public DataStoreManagerException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );
    }
}
