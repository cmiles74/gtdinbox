package com.nervestaple.gtdinbox.configuration.application;

/**
 * Provides an exception object for the ApplicationConfiguration.
 */
public class CouldNotSaveConfigurationException extends ApplicationConfigurationException {

    /**
     * Returns a CouldNotSaveConfigurationException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public CouldNotSaveConfigurationException(final String message) {

        // call the parent constructor
        super(message);
    }

    /**
     * Returns a CouldNotSaveConfigurationException with the given root throwable.
     *
     * @param exception Root exception
     */
    public CouldNotSaveConfigurationException(final Exception exception) {

        // call the parent constructor
        super(exception);
    }

    /**
     * Returns an CouldNotSaveConfigurationException with a custom message and a root exception that can then be thrown
     * up the chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public CouldNotSaveConfigurationException(final String message, final Throwable throwable) {

        // call the parent constructor
        super(message);

        // save the root exception
        this.throwableRoot = throwable;
    }
}
