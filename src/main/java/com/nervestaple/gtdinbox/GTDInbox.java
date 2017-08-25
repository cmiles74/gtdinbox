package com.nervestaple.gtdinbox;

import com.nervestaple.gtdinbox.gui.GTDInboxGUI;
import org.apache.log4j.Logger;

/**
 * Provides the entry class for the GTDInbox application. This class is responsible for bootstrapping the rest of the
 * application.
 */
public class GTDInbox {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Bootstraps the application.
     *
     * @param args Command line arguments
     */
    public static void main(final String[] args) {

        try {
            new GTDInbox();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new GTDInbox.
     *
     * @throws Exception On any unforseen startup problem
     */
    public GTDInbox() throws Exception {

        GTDInboxGUI.getInstance();
    }
}