package com.nervestaple.gtdinbox.configuration.application;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactoryException;

/**
 * Provides an exception object for the ApplicationConfiguration.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ApplicationConfigurationException extends ConfigurationFactoryException {

    /**
     * Returns a ApplicationConfigurationException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public ApplicationConfigurationException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a ApplicationConfigurationException with the given root throwable.
     *
     * @param exception Root exception
     */
    public ApplicationConfigurationException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an ApplicationConfigurationException with a custom message and a root exception that can then be thrown
     * up the chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public ApplicationConfigurationException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );

        // save the root exception
        this.throwableRoot = throwable;
    }
}
