package com.nervestaple.gtdinbox.utility.stoppable;

/**
 * Provides an interface that a runnable can implement to indicate that it can be stopped before completing it's task.
 */
public abstract class StoppableRunnable implements Runnable {

    /** Flag to indicate the thread must stop. */
    protected boolean stop = false;

    public abstract void run();

    public void stop() {

        // flag the thread
        stop = true;
    }
}
