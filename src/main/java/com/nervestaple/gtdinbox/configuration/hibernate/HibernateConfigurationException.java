package com.nervestaple.gtdinbox.configuration.hibernate;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactoryException;

/**
 * Provides an exception object for the HibernateConfiguration.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class HibernateConfigurationException extends ConfigurationFactoryException {

    /**
     * Returns a HibernateConfigurationException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public HibernateConfigurationException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a HibernateConfigurationException with the given root throwable.
     *
     * @param exception Root exception
     */
    public HibernateConfigurationException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an HibernateConfigurationException with a custom message and a root exception that can then be thrown up
     * the chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public HibernateConfigurationException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );

        // save the root exception
        this.throwableRoot = throwable;
    }
}
