package com.nervestaple.gtdinbox.configuration;

import com.nervestaple.gtdinbox.GTDInboxException;

/**
 * Provides an exceptionobject for the ConfigurationFactory.
 *
 * @author Christopher Miles
 * @verison 1.0
 */
public class ConfigurationFactoryException extends GTDInboxException {

    /**
     * Returns a ConfigurationFactoryException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public ConfigurationFactoryException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a ConfigurationFactoryException with the given root throwable.
     *
     * @param exception Root exception
     */
    public ConfigurationFactoryException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an ConfigurationFactoryException with a custom message and a root exception that can then be thrown up the chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public ConfigurationFactoryException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );

        // save the root exception
        this.throwableRoot = throwable;
    }
}
