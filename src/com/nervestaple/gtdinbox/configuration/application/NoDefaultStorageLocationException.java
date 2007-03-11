package com.nervestaple.gtdinbox.configuration.application;

/**
 * Provides an exception object for the ApplicationConfiguration.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class NoDefaultStorageLocationException extends ApplicationConfigurationException {

    /**
     * Returns a NoDefaultStorageLocationException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public NoDefaultStorageLocationException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a NoDefaultStorageLocationException with the given root throwable.
     *
     * @param exception Root exception
     */
    public NoDefaultStorageLocationException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an NoDefaultStorageLocationException with a custom message and a root exception that can then be thrown
     * up the chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public NoDefaultStorageLocationException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );

        // save the root exception
        this.throwableRoot = throwable;
    }
}
