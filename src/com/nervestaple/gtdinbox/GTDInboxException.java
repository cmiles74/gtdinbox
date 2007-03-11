package com.nervestaple.gtdinbox;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Provides an exception object for the application.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class GTDInboxException extends Exception {

    /** The root exception. */
    protected Throwable throwableRoot;

    /**
     * Returns a GTDInboxException with a custom message.
     *
     * @param message A String with the text of the exception message
     */
    public GTDInboxException( final String message ) {

        // call the parent constructor
        super( message );
    }

    /**
     * Returns a GTDInboxException with the given root throwable.
     *
     * @param exception Root exception
     */
    public GTDInboxException( final Exception exception ) {

        // call the parent constructor
        super( exception );
    }

    /**
     * Returns an GTDInboxException with a custom message and a root exception that can then be thrown up the chain.
     *
     * @param message   A String with the text of the exception message
     * @param throwable The root exception
     */
    public GTDInboxException( final String message, final Throwable throwable ) {

        // call the parent constructor
        super( message );

        // save the root exception
        this.throwableRoot = throwable;
    }

    /**
     * Returns the message.
     *
     * @return A String with the message
     */
    public final String getMessage() {

        if( throwableRoot != null ) {

            // return our message and the root exception's message
            return ( super.getMessage() + ": " + throwableRoot.getMessage() );
        } else {

            // return our message
            return ( super.getMessage() );
        }
    }

    /**
     * Prints a stack trace to standard error out.
     */
    public final void printStackTrace() {

        // print the parent's stack trace
        super.printStackTrace();

        if( throwableRoot != null ) {

            // print the root exception's stack trace
            System.err.print( "Root exception: " );
            throwableRoot.printStackTrace();
        }
    }

    /**
     * Prints a stack trace to an output stream.
     *
     * @param printstream The PrintStream to handle the stack trace output
     */
    public final void printStackTrace( final PrintStream printstream ) {

        // print the parent's stack trace
        super.printStackTrace( printstream );

        if( throwableRoot != null ) {

            // print the root exception's stack trace
            printstream.print( "Root exception: " );
            throwableRoot.printStackTrace( printstream );
        }
    }

    /**
     * Prints a stack trace to an output writer.
     *
     * @param printwriter The PrintWriter to handle the stack trace output
     */
    public final void printStackTrace( final PrintWriter printwriter ) {

        // print the parent's stack trace
        super.printStackTrace( printwriter );

        if( throwableRoot != null ) {

            // print the root exception's stack trace
            printwriter.print( "Root exception: " );
            throwableRoot.printStackTrace( printwriter );
        }
    }
}
