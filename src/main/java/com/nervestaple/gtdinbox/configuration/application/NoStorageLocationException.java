package com.nervestaple.gtdinbox.configuration.application;

/**
 * Provides an exception object for the Application Configuration.
 *
 * @author Christopher Miles
 * @vesion 1.0
 */
public class NoStorageLocationException extends ApplicationConfigurationException {

    /**
     * Returns a NoStorageLocationException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public NoStorageLocationException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a NoStorageLocationException with the given root throwable.
     *
     * @param exception Root exception
     */
    public NoStorageLocationException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an NoStorageLocationException with a custom message and a root exception that can then be thrown
     * up the chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public NoStorageLocationException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );

        // save the root exception
        this.throwableRoot = throwable;
    }
}
