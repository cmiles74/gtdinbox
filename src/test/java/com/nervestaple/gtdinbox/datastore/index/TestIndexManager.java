package com.nervestaple.gtdinbox.datastore.index;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.document.Document;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactoryException;
import com.nervestaple.gtdinbox.model.project.Project;

import java.io.File;

/**
 * Provides a test suite for the IndexManager object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestIndexManager extends TestCase {

    /** Logger instance. */
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

    public void testGetInstance() {

        IndexManager indexManager = IndexManager.getInstance();

        assertNotNull( indexManager );
    }

    public void testGetIndexWriter() throws Exception {

        IndexManager indexManager = IndexManager.getInstance();

        IndexWriter indexWriter = indexManager.getIndexWriter();

        assertNotNull( indexWriter );
    }

    public void testGetIndexReader() throws Exception {

        IndexManager indexManager = IndexManager.getInstance();

        IndexReader indexReader = indexManager.getIndexReader();

        assertNotNull( indexReader );
    }

    public void testGetIndexSearcher() throws Exception {

        IndexManager indexManager = IndexManager.getInstance();

        IndexSearcher indexSearcher = indexManager.getIndexSearcher();

        assertNotNull( indexSearcher );
    }

    public void testAddBeanMap() throws Exception {

        Project project = new Project();
        project.setId( Long.valueOf( 284262 ) );
        project.setName( "Oogedy Booegedy Boo" );

        IndexManager indexManager = IndexManager.getInstance();
        indexManager.addIndexable( project );

        // create a new query to find our project
        TermQuery queryId = new TermQuery( new Term( "id", project.getId().toString() ) );
        TermQuery queryClass = new TermQuery( new Term( "class", project.getClass().getName().toLowerCase() ) );

        BooleanQuery query = new BooleanQuery();
        query.add( new BooleanClause( queryId, BooleanClause.Occur.MUST ) );
        query.add( new BooleanClause( queryClass, BooleanClause.Occur.MUST ) );

        // verify the project is in the database
        IndexSearcher indexSearcher = indexManager.getIndexSearcher();
        Hits hits = indexSearcher.search( query );
        int results = hits.length();

        indexManager.removeIndexable( project );

        logger.info( "Hits: " + results );
        assertTrue( results == 1 );
    }

    public void testRemoveBeanMap() throws Exception {

        Project project = new Project();
        project.setId( Long.valueOf( 284262 ) );
        project.setName( "Oogedy Booegedy Boo" );

        IndexManager indexManager = IndexManager.getInstance();
        indexManager.addIndexable( project );

        // create a new query to find our project
        TermQuery queryId = new TermQuery( new Term( "id", project.getId().toString() ) );
        TermQuery queryClass = new TermQuery( new Term( "class", project.getClass().getName().toLowerCase() ) );

        BooleanQuery query = new BooleanQuery();
        query.add( new BooleanClause( queryId, BooleanClause.Occur.MUST ) );
        query.add( new BooleanClause( queryClass, BooleanClause.Occur.MUST ) );

        indexManager.removeIndexable( project );

        // verify the project is in the database
        IndexSearcher indexSearcher = indexManager.getIndexSearcher();
        Hits hits = indexSearcher.search( query );
        int results = hits.length();

        logger.info( "Hits: " + results );
        assertTrue( results == 0 );
    }

    public void testUpdateBeanMap() throws Exception {

        Project project = new Project();
        project.setId( Long.valueOf( 284262 ) );
        project.setName( "Oogedy Booegedy Boo" );

        IndexManager indexManager = IndexManager.getInstance();
        indexManager.addIndexable( project );

        project.setName( "Ooh La La" );
        indexManager.updateIndexable( project );

        TermQuery queryId = new TermQuery( new Term( "id", project.getId().toString() ) );
        TermQuery queryClass = new TermQuery( new Term( "class", project.getClass().getName().toLowerCase() ) );

        BooleanQuery query = new BooleanQuery();
        query.add( new BooleanClause( queryId, BooleanClause.Occur.MUST ) );
        query.add( new BooleanClause( queryClass, BooleanClause.Occur.MUST ) );

        // verify the project is in the database
        IndexSearcher indexSearcher = indexManager.getIndexSearcher();
        Hits hits = indexSearcher.search( query );
        String name = ( ( Hit ) hits.iterator().next() ).getDocument().get( "name" );

        indexManager.removeIndexable( project );

        logger.info( "Name: " + name );
        assertTrue( name.equals( project.getName() ) );
    }

    public void testRunSearch() throws Exception {

        Project project = new Project();
        project.setId( Long.valueOf( 284262 ) );
        project.setName( "Oogedy Booegedy Boo" );

        final IndexManager indexManager = IndexManager.getInstance();
        indexManager.addIndexable( project );

        // create a new query to find our project
        TermQuery queryId = new TermQuery( new Term( "id", project.getId().toString() ) );
        TermQuery queryClass = new TermQuery( new Term( "class", project.getClass().getName().toLowerCase() ) );

        final BooleanQuery query = new BooleanQuery();
        query.add( new BooleanClause( queryId, BooleanClause.Occur.MUST ) );
        query.add( new BooleanClause( queryClass, BooleanClause.Occur.MUST ) );

        final TestSearchResultHandler handler = new TestSearchResultHandler();

        Thread thread = new Thread( new Runnable() {

            public void run() {

                try {
                    indexManager.runSearch( query, handler );
                } catch( IndexManagerException e ) {
                    logger.warn( e );
                }
            }
        });
        thread.start();

        Thread.sleep( 1000 );

        indexManager.removeIndexable( project );

        logger.info( "SearchResultHanlder received " + handler.getResults() + " results" );
        assertTrue( handler.getResults() > 0 );
    }

    private class TestSearchResultHandler implements SearchResultHandler {

        private int results = 0;

        /**
         * Method called every time a search result is returned from the index.
         *
         * @param document The Lucene result Document
         */
        public void handleSearchResult( Document document ) {
            logger.info( "Returned document " + document );
        }

        /**
         * Sets the number of results the index is returning.
         *
         * @param results
         */
        public void setNumberOfResults( int results ) {
            this.results = results;
            logger.info( "Returned " + results + " documents" );
        }


        public int getResults() {
            return results;
        }
    }
}


