package com.nervestaple.gtdinbox.configuration.hibernate;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactoryException;
import junit.framework.TestCase;
import com.nervestaple.gtdinbox.configuration.application.ApplicationConfiguration;

/**
 * Provides a test suite for the HibernateConfiguration object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestHiberateConfiguration extends TestCase {

    public void setUp() throws ConfigurationFactoryException {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();
        configurationFactory.setTestingConfiguration(true);
        configurationFactory.configure();
    }

    public void testConstructor() throws Exception {

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();

        applicationConfiguration.setTestingConfiguration( true );

        applicationConfiguration.configure();

        HibernateConfiguration configuration = new HibernateConfiguration( applicationConfiguration );

        assertNotNull( configuration );
    }

    public void testTestingConfiguration() throws Exception {

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();

        applicationConfiguration.setTestingConfiguration( true );

        applicationConfiguration.configure();

        HibernateConfiguration configuration = new HibernateConfiguration( applicationConfiguration );

        configuration.setTestingConfiguration( true );

        assertTrue( configuration.isTestingConfiguration() );
    }

    public void testConfigure() throws Exception {

        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();

        applicationConfiguration.setTestingConfiguration( true );

        applicationConfiguration.configure();

        HibernateConfiguration configuration = new HibernateConfiguration( applicationConfiguration );

        configuration.setTestingConfiguration( true );

        configuration.configure();

        assertNotNull( configuration.getEntityManagerFactory() );
    }

    
}
