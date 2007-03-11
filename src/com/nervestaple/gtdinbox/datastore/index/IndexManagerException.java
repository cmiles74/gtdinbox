package com.nervestaple.gtdinbox.datastore.index;

import com.nervestaple.gtdinbox.datastore.DataStoreException;

/**
 * Provides an exception object for the IndexManager.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class IndexManagerException extends DataStoreException {

    /**
     * Returns a IndexManagerException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public IndexManagerException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a IndexManagerException with the given root throwable.
     *
     * @param exception Root exception
     */
    public IndexManagerException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an IndexManagerException with a custom message and a root exception that can then be thrown up the
     * chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public IndexManagerException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );
    }
}
