package com.nervestaple.gtdinbox.datastore.index;

import com.nervestaple.gtdinbox.model.Indexable;
import org.apache.lucene.document.Document;

/**
 * Defines an interface that an object can implement if it wants to receive search results.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface SearchResultHandler {

    /**
     * Method called every time a search result is returned from the index.
     *
     * @param document The Lucene result Document
     */
    public void handleSearchResult(Document document);

    /**
     * Sets the number of results the index is returning.
     *
     * @param results
     */
    public void setNumberOfResults(int results);
}
