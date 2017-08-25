package com.nervestaple.gtdinbox.configuration.application;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactoryException;
import junit.framework.TestCase;

/**
 * Provides a test suite for the application configuration.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestApplicationConfiguration extends TestCase {

    public void setUp() throws ConfigurationFactoryException {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();
        configurationFactory.setTestingConfiguration(true);
        configurationFactory.configure();
    }

    public void testConstructor() {

        ApplicationConfiguration configuration = new ApplicationConfiguration();

        assertNotNull(configuration);
    }

    public void testTestingConfiguration() throws Exception {

        ApplicationConfiguration configuration = new ApplicationConfiguration();

        configuration.setTestingConfiguration(true);

        assertTrue(configuration.isTestingConfiguration());
    }

    public void testGetUserHomeDirectory() throws Exception {

        ApplicationConfiguration configuration = new ApplicationConfiguration();

        assertNotNull(configuration.getUserHomeDirectory());
    }

    public void testConfigure() throws Exception {

        ApplicationConfiguration configuration = new ApplicationConfiguration();

        configuration.setTestingConfiguration(true);

        configuration.configure();

        assertNotNull(configuration.getConfiguration());
    }

    public void testGetDataStorageLocation() throws Exception {

        ApplicationConfiguration configuration = new ApplicationConfiguration();

        configuration.setTestingConfiguration(true);

        configuration.configure();

        assertNotNull(configuration.getDataStorageLocation());
    }

    public void testGetDataBaseStorageLocation() throws Exception {

        ApplicationConfiguration configuration = new ApplicationConfiguration();

        configuration.setTestingConfiguration(true);

        configuration.configure();

        assertNotNull(configuration.getDatabaseStorageLocation());
    }

    public void testGetIndexStorageLocation() throws Exception {

        ApplicationConfiguration configuration = new ApplicationConfiguration();

        configuration.setTestingConfiguration(true);

        configuration.configure();

        assertNotNull(configuration.getIndexStorageLocation());
    }
}
