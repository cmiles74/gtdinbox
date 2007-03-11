package com.nervestaple.gtdinbox.datastore.index.indexinterceptor;

import junit.framework.TestCase;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactoryException;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.index.IndexManager;
import org.apache.lucene.search.*;
import org.apache.lucene.index.Term;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Iterator;

/**
 * Provides a test suite for the IndexInterceptor.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestIndexInterceptor extends TestCase {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    public void setUp() throws ConfigurationFactoryException {

        ConfigurationFactory configurationFactory = ConfigurationFactory.getInstance();

        if( !configurationFactory.isTestingConfiguration() ) {
            configurationFactory.setTestingConfiguration( true );
        }

        try {
            configurationFactory.configure();
        } catch( ConfigurationFactoryException e ) {

            File file = configurationFactory.getApplicationConfiguration().createDefaultDataStorageLocation();
            configurationFactory.getApplicationConfiguration().setDataStorageLocation( file );
        }

        configurationFactory.configure();
    }

    public void testAddObject() throws Exception {

        // create a new project
        Project project = new Project();
        project.setName( "Test Project" );

        // perisist the project
        DataBaseManager databaseManager = DataBaseManager.getInstance();
        databaseManager.beginTransaction();
        databaseManager.getSession().save( project );
        databaseManager.commitTransaction();

        // create a new query to find our project
        TermQuery queryId = new TermQuery( new Term( "id", project.getId().toString() ) );
        TermQuery queryClass = new TermQuery( new Term( "class", project.getClass().getName().toLowerCase() ) );

        BooleanQuery query = new BooleanQuery();
        query.add( new BooleanClause( queryId, BooleanClause.Occur.MUST ) );
        query.add( new BooleanClause( queryClass, BooleanClause.Occur.MUST ) );

        // verify the project is in the database
        IndexManager indexManager = IndexManager.getInstance();
        IndexSearcher indexSearcher = indexManager.getIndexSearcher();
        Hits hits = indexSearcher.search( query );
        int results = hits.length();
      
        // remove the project
        databaseManager.beginTransaction();
        databaseManager.getSession().delete( project );
        databaseManager.commitTransaction();

        logger.info( query );
        logger.info( "Hits returned: " + results );

        assertTrue( results > 0 );
    }

    public void testRemoveObject() throws Exception {

        // create a new project
        Project project = new Project();
        project.setName( "Test Project" );

        // perisist the project
        DataBaseManager databaseManager = DataBaseManager.getInstance();
        databaseManager.beginTransaction();
        databaseManager.getSession().save( project );
        databaseManager.commitTransaction();

        // create a new query to find our project
        TermQuery queryId = new TermQuery( new Term( "id", project.getId().toString() ) );
        TermQuery queryClass = new TermQuery( new Term( "class", project.getClass().getName().toLowerCase() ) );

        BooleanQuery query = new BooleanQuery();
        query.add( new BooleanClause( queryId, BooleanClause.Occur.MUST ) );
        query.add( new BooleanClause( queryClass, BooleanClause.Occur.MUST ) );

        // remove the project
        databaseManager.beginTransaction();
        databaseManager.getSession().delete( project );
        databaseManager.commitTransaction();

        // verify the project has been removed
        IndexManager indexManager = IndexManager.getInstance();
        IndexSearcher indexSearcher = indexManager.getIndexSearcher();
        Hits hits = indexSearcher.search( query );
        int results = hits.length();
        logger.info( query );
        logger.info( "Hits returned: " + results );

        assertTrue( results == 0 );
    }
}
