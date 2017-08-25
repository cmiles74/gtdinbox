package com.nervestaple.gtdinbox.utility.stoppable;

import junit.framework.TestCase;
import org.apache.log4j.Logger;

/**
 * Provides a test suite for the stoppable thread.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestStoppableThread extends TestCase {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    public void testStoppableThread() throws Exception {

        StoppableThread thread = new StoppableThread(new StoppableRunnable() {
            public void run() {

                while (!stop) {

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        logger.warn(e);
                    }
                }
            }
        });
        thread.start();

        Thread.sleep(100);

        thread.stopThread();

        Thread.sleep(100);

        assertTrue(!thread.isAlive() && thread.isStop());
    }
}
