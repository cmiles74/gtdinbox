package com.nervestaple.gtdinbox.configuration.hibernate;

import com.nervestaple.gtdinbox.configuration.application.ApplicationConfiguration;
import com.nervestaple.gtdinbox.configuration.application.ApplicationConfigurationException;
import org.hibernate.HibernateException;
import org.apache.log4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides an object for configuring Hibernate.
 */
public class HibernateConfiguration {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Application configuration.
     */
    private ApplicationConfiguration applicationConfiguration;

    /**
     * Hibernate Session Factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Flag to indicate if the configuration is for testing.
     */
    private boolean testingConfiguration = false;

    /**
     * Creates a new HibernateConfiguration.
     *
     * @param applicationConfiguration application configuration
     */
    public HibernateConfiguration(ApplicationConfiguration applicationConfiguration) {

        this.applicationConfiguration = applicationConfiguration;
    }

    public Map<String, Object> getConfigurationProperties() {

        File databaseStorageLocation = applicationConfiguration.getDatabaseStorageLocation();
        logger.debug("DatabaseStorageLocation: " + databaseStorageLocation);

        // setup out database connection
        Map<String, Object> properties = new HashMap<>();

        if (databaseStorageLocation.exists()) {

            properties.put("javax.persistence.jdbc.url", "jdbc:derby:" +
                    databaseStorageLocation.getAbsolutePath());
        } else {

            properties.put("javax.persistence.jdbc.url", "jdbc:derby:" +
                    databaseStorageLocation.getAbsolutePath()
                    + ";create=true");
        }

        return properties;
    }

    /**
     * Configures the Hibernate configuration.
     *
     * @throws HibernateException on problems configuring Hibernate
     */
    public void configure() throws HibernateConfigurationException {

        // setup out database connection
        Map<String, Object> properties = getConfigurationProperties();

        try {

            // create a new entity manager factory
            entityManagerFactory =
                    Persistence.createEntityManagerFactory("com.nervestaple.gtdinbox.jpa", properties);
        } catch (HibernateException e) {

            throw new HibernateConfigurationException(e);
        }
    }

    /**
     * Returns the Hibernate entity manager factory.
     *
     * @return EntityManagerFactory
     */
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    /**
     * Returns true if the application is in the testing configuration.
     *
     * @return boolean
     */
    public boolean isTestingConfiguration() {
        return testingConfiguration;
    }

    /**
     * Tell the factory if it should configure itself for running in test mode.
     *
     * @param testingConfiguration boolean
     * @throws ApplicationConfigurationException if the instance has already been configured
     */
    public void setTestingConfiguration(final boolean testingConfiguration) throws ApplicationConfigurationException {

        if (entityManagerFactory != null) {

            throw new ApplicationConfigurationException(
                    "Cannot enter testing mode after configure() has been called");
        }

        this.testingConfiguration = testingConfiguration;
    }
}
