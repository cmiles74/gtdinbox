package com.nervestaple.gtdinbox.datastore.index;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.utility.stoppable.StoppableRunnable;
import com.nervestaple.gtdinbox.utility.stoppable.StoppableThread;
import org.apache.commons.beanutils.BeanMap;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * Provides an object for managing the application's indexes. This is a singleton class, there can only be one per
 * running instance of the application.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class IndexManager {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Singleton instance.
     */
    private final static IndexManager indexManager;

    /**
     * Index storage location
     */
    private File indexStorageLocation;

    /**
     * Index directory
     */
    private Directory directory;

    /**
     * Index writer
     */
    private IndexWriter indexWriter;

    /**
     * Index reader
     */
    private IndexReader indexReader;

    /**
     * Index Searcher
     */
    private IndexSearcher indexSearcher;

    /**
     * Index manager listeners.
     */
    private List listeners;

    /**
     * Thread for running searches
     */
    private StoppableThread threadSearch;

    static {

        indexManager = new IndexManager();
    }

    /**
     * Creates a new IndexManager.
     */
    private IndexManager() {

        listeners = new ArrayList();
    }

    /**
     * Configures the IndexManager.
     *
     * @throws IndexManagerException on problems setting up the index
     */
    public void configure() throws IndexManagerException {

        if( indexStorageLocation == null ) {

            // get the location for the index
            setupIndex(
                    ConfigurationFactory.getInstance().getApplicationConfiguration().getIndexStorageLocation() );
        }
    }

    public void addIndexManagerListener( IndexManagerListener listener ) {

        if( !listeners.contains( listener ) ) {
            listeners.add( listener );
        }
    }

    public void removeIndexManagerListener( IndexManagerListener listener ) {

        listeners.remove( listener );
    }

    /**
     * Optimizes and then closes the index.
     *
     * @throws IndexManagerException on problems optimizing or closing the index.
     */
    public void flushIndex() throws IndexManagerException {

        if( indexWriter == null ) {
            return;
        }

        indexWriter = getIndexWriter();

        try {
            indexWriter.optimize();
        } catch( IOException e ) {
            throw new IndexManagerException( e );
        }

        try {
            indexWriter.close();
        } catch( IOException e ) {
            throw new IndexManagerException( e );
        }

        indexWriter = null;

        if( indexSearcher != null ) {

            try {
                indexSearcher.close();
            } catch( IOException e ) {
                throw new IndexManagerException( e );
            }
            indexSearcher = null;
        }

        if( indexReader != null ) {

            try {
                indexReader.close();
            } catch( IOException e ) {
                logger.warn( e );
            }
            indexReader = null;
        }
    }

    /**
     * Returns the singleton instance of the IndexManager.
     *
     * @return IndexManager
     */
    public static IndexManager getInstance() {

        return ( indexManager );
    }

    /**
     * Returns a IndexWriter.
     *
     * @return a new IndexWriter
     * @throws IndexManagerException
     */
    public IndexWriter getIndexWriter() throws IndexManagerException {

        if( indexReader != null ) {
            try {
                indexReader.close();
            } catch( IOException e ) {
                logger.warn( e );
            }
        }

        if( indexSearcher != null ) {
            try {
                indexSearcher.close();
            } catch( IOException e ) {
                logger.warn( e );
            }
        }

        if( indexWriter != null ) {
            return ( indexWriter );
        }

        // create a new index writer
        try {
            indexWriter = new IndexWriter( directory, getAnalyzer(), false );
        } catch( IOException e ) {
            throw new IndexManagerException( e );
        }

        return indexWriter;
    }

    /**
     * Returns the IndexReader.
     *
     * @return a new IndexReader
     * @throws IndexManagerException
     */
    public IndexReader getIndexReader() throws IndexManagerException {

        if( indexWriter != null ) {
            flushIndex();
        }

        if( indexReader != null ) {
            return ( indexReader );
        }

        try {
            indexReader = IndexReader.open( directory );
        } catch( IOException e ) {
            throw new IndexManagerException( e );
        }

        return indexReader;
    }

    /**
     * Returns a IndexSearcher.
     *
     * @return a new IndexSearcher
     * @throws IndexManagerException
     */
    public IndexSearcher getIndexSearcher() throws IndexManagerException {

        if( indexWriter != null ) {
            flushIndex();
        }

        if( indexSearcher != null ) {
            return ( indexSearcher );
        }

        indexSearcher = new IndexSearcher( getIndexReader() );

        return indexSearcher;
    }

    /**
     * Adds an indexable object to the index.
     * <p/>
     * This method is called by the IndexInterceptor when Hibernate manipulates the database, you most likely do not
     * want to call this method directly.
     *
     * @param indexable
     * @throws IndexManagerException
     */
    public void addIndexable( Indexable indexable ) throws IndexManagerException {

        if( indexable.getId() == null ) {

            throw new IndexManagerException( "Cannot index an object without an id!" );
        }

        BeanMap beanMap = new BeanMap( indexable );

        updateBeanMap( beanMap );
    }

    /**
     * Updates the index with the new values for the indexable object.
     * <p/>
     * This method is called by the IndexInterceptor when Hibernate manipulates the database, you most likely do not
     * want to call this method directly.
     *
     * @param indexable
     * @throws IndexManagerException
     */
    public void updateIndexable( Indexable indexable ) throws IndexManagerException {

        if( indexable.getId() == null ) {

            throw new IndexManagerException( "Cannot index an object without an id!" );
        }

        BeanMap beanMap = new BeanMap( indexable );

        updateBeanMap( beanMap );
    }

    /**
     * Removes entries for the indexable object from the index.
     * <p/>
     * This method is called by the IndexInterceptor when Hibernate manipulates the database, you most likely do not
     * want to call this method directly.
     *
     * @param indexable
     * @throws IndexManagerException
     */
    public void removeIndexable( Indexable indexable ) throws IndexManagerException {

        if( indexable.getId() == null ) {

            throw new IndexManagerException( "Cannot index an object without an id!" );
        }

        BeanMap beanMap = new BeanMap( indexable );

        removeBeanMap( beanMap );
    }

    /**
     * Starts another search on the index. If there was another search running,
     * that search will be told to stop and the new search will start once it
     * has exited.
     * <p/>
     * This is probably the way you want to search, you can always get a searcher
     * and run a search by hand, but this method will work better if the search
     * and results are tied to a GUI (searches won't pile up in the background and
     * the user can interrupt a running searcher with a new search, similar to the
     * way the iTunes search box works).
     *
     * @param query   Query to run
     * @param handler Handler for returned results
     * @throws IndexManagerException On problems running the search
     */
    public void runSearch( final Query query, final SearchResultHandler handler ) throws IndexManagerException {

        if( threadSearch != null ) {

            // stop the current running search
            threadSearch.stopThread();

            // loop until the thread exits
            while( threadSearch.isAlive() ) {

                try {

                    Thread.sleep( 50 );
                } catch( InterruptedException ex ) {

                    logger.warn( ex );
                }
            }
        }

        indexSearcher = getIndexSearcher();

        // setup a new thread to handle the new search
        threadSearch = new StoppableThread(
                new StoppableRunnable() {

                    public void run() {

                        try {

                            // run the search
                            logger.debug( "Query: " + query );
                            Hits hits = indexSearcher.search( query );

                            handler.setNumberOfResults( hits.length() );

                            // loop through the hits
                            for( int index = 0; index < hits.length() && !stop; index++ ) {

                                // get the next document
                                Document document = hits.doc( index );
                                document.add( new Field( "rank", ( new Float( hits.score( index ) ) ).toString(),
                                        Field.Store.YES, Field.Index.NO ) );

                                handler.handleSearchResult( document );
                            }
                        } catch( IOException ex ) {

                            logger.warn( "Couldn't query the index" );
                        }
                    }
                } );

        // start the search
        threadSearch.start();
    }

    // private methods

    private void fireDocumentAdded( Document document ) {

        IndexManagerListener[] listenerArray =
                ( IndexManagerListener[] ) listeners.toArray( new IndexManagerListener[listeners.size()] );

        for( int index = 0; index < listenerArray.length; index++ ) {

            listenerArray[ index ].documentAdded( document );
        }
    }

    private void fireDocumentRemoved( Document document ) {

        IndexManagerListener[] listenerArray =
                ( IndexManagerListener[] ) listeners.toArray( new IndexManagerListener[listeners.size()] );

        for( int index = 0; index < listenerArray.length; index++ ) {

            listenerArray[ index ].documentRemoved( document );
        }
    }

    /**
     * Adds a bean map to the index.
     *
     * @param beanMap
     * @throws IndexManagerException on problems removing the bean or searching the index
     */
    private void addBeanMap( BeanMap beanMap ) throws IndexManagerException {

        // create a document for the bean
        Document document = new Document();

        // loop through the bean's keys
        Iterator iteratorKeys = beanMap.keyIterator();
        while( iteratorKeys.hasNext() ) {

            Object key = iteratorKeys.next();
            Object value = beanMap.get( key );

            //logger.debug( "     key: " + key.toString() + ", value: " + value );

            if( value != null && value instanceof Date ) {
                document.add(
                        new Field( key.toString(),
                                DateTools.dateToString( ( Date ) value, DateTools.Resolution.MINUTE ),
                                Field.Store.YES, Field.Index.TOKENIZED ) );
            } else if( value != null && value instanceof Class ) {
                document.add(
                        new Field( key.toString(), ( ( Class ) value ).getName().toLowerCase(),
                                Field.Store.YES, Field.Index.TOKENIZED ) );
            } else if( value != null ) {
                document.add(
                        new Field( key.toString(), value.toString(),
                                Field.Store.YES, Field.Index.TOKENIZED ) );
            }
        }

        IndexWriter indexWriter = getIndexWriter();

        // add the document to the index
        try {
            indexWriter.addDocument( document, getAnalyzer() );
        } catch( IOException e ) {
            throw new IndexManagerException( e );
        }

        fireDocumentAdded( document );

        flushIndex();
    }

    /**
     * Removes a bean map from the index.
     *
     * @param beanMap
     * @throws IndexManagerException on problems removing the bean or searching the index
     */
    private void removeBeanMap( BeanMap beanMap ) throws IndexManagerException {

        logger.debug( "Removing beanMap " + beanMap );

        // query for the id
        TermQuery queryId = new TermQuery( new Term( "id", beanMap.get( "id" ).toString() ) );

        // query for the class
        TermQuery queryClass = new TermQuery(
                new Term( "class", ( ( Class ) beanMap.get( "class" ) ).getName().toLowerCase() ) );

        // create a new query
        BooleanQuery query = new BooleanQuery();
        query.add( new BooleanClause( queryId, BooleanClause.Occur.MUST ) );
        query.add( new BooleanClause( queryClass, BooleanClause.Occur.MUST ) );

        IndexSearcher indexSearcher = getIndexSearcher();

        try {

            // run the query
            Hits hits = indexSearcher.search( query );
            logger.debug( "Query: " + query );
            logger.debug( "Will remove " + hits.length() + " documents" );

            // loop through the hits aand delte them
            Iterator hitIterator = hits.iterator();
            while( hitIterator.hasNext() ) {

                Hit hit = ( Hit ) hitIterator.next();
                logger.debug( "Removing document: " + hit.getDocument() );
                fireDocumentRemoved( hit.getDocument() );
                getIndexReader().deleteDocument( hit.getId() );
            }
        } catch( IOException e ) {
            logger.warn( e );
            throw new IndexManagerException( e );
        }

        flushIndex();
    }

    /**
     * Updates a bean map that's in the index.
     *
     * @param beanMap
     * @throws IndexManagerException on problems removing the bean or searching the index
     */
    private void updateBeanMap( BeanMap beanMap ) throws IndexManagerException {

        removeBeanMap( beanMap );

        addBeanMap( beanMap );
    }

    /**
     * Sets up the index using the storage location provided.
     *
     * @param indexStorageLocation
     * @throws IndexManagerException
     */
    private void setupIndex( File indexStorageLocation ) throws IndexManagerException {

        // save the file location
        this.indexStorageLocation = indexStorageLocation;

        // flag to indicate the index is being created
        boolean indexCreated = false;

        if( indexStorageLocation.exists() ) {

            // setup the directory instance
            try {
                directory = FSDirectory.getDirectory( indexStorageLocation, false );
            } catch( IOException e ) {
                throw new IndexManagerException( e );
            }
        } else {

            // we are creating the index
            indexCreated = true;

            // setup the directory instance
            try {
                directory = FSDirectory.getDirectory( indexStorageLocation, true );
            } catch( IOException e ) {
                throw new IndexManagerException( e );
            }
        }

        if( indexCreated ) {

            // create a new index writer to instantiate the index
            IndexWriter indexWriter = null;
            try {
                indexWriter = new IndexWriter( directory, getAnalyzer(), true );
                indexWriter.optimize();
                indexWriter.close();
            } catch( IOException e ) {

                throw new IndexManagerException( e );
            }
        } else {

            // make sure the index is unlocked
            try {
                if( IndexReader.isLocked( directory ) ) {

                    IndexReader.unlock( directory );
                }
            } catch( IOException e ) {
                throw new IndexManagerException( e );
            }
        }
    }

    /**
     * Returns the analyzer for this index.
     *
     * @return Analyzer
     */
    private Analyzer getAnalyzer() {

        return ( new StandardAnalyzer() );
    }
}