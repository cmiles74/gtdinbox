package com.nervestaple.gtdinbox.utility.stoppable;

/**
 * Provides a thread that can be stopped before its task has completed.
 */
public class StoppableThread extends Thread {

    /**
     * Flag to indicate the thread should halt.
     */
    private boolean stop = false;

    /**
     * Running runnable.
     */
    private StoppableRunnable runnable;

    /**
     * Creates a new instance of StoppableThread
     */
    public StoppableThread(StoppableRunnable runnable) {

        super(runnable);

        this.runnable = runnable;
    }

    /**
     * Tells the thread to stop and exit.
     */
    public void stopThread() {

        setStop(true);

        runnable.stop();
    }

    // accessor and mutator methods

    public void setStop(boolean stop) {

        this.stop = stop;
    }

    public boolean isStop() {

        return (stop);
    }
}
