package com.nervestaple.gtdinbox.datastore;

import com.nervestaple.gtdinbox.GTDInboxException;

/**
 * Provides an exception object for the DataStore.
 */
public class DataStoreException extends GTDInboxException {

    /**
     * Returns a DataStoreException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public DataStoreException(final String message) {

        // call the parent constructor
        super(message);
    }

    /**
     * Returns a DataStoreException with the given root throwable.
     *
     * @param exception Root exception
     */
    public DataStoreException(final Exception exception) {

        // call the parent constructor
        super(exception);
    }

    /**
     * Returns an DataStoreException with a custom message and a root exception that can then be thrown up the chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public DataStoreException(final String message, final Throwable throwable) {

        // call the parent constructor
        super(message);

        // save the root exception
        this.throwableRoot = throwable;
    }
}
