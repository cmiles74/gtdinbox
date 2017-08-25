package com.nervestaple.gtdinbox.datastore.index;

import org.apache.lucene.document.Document;

/**
 * Provides an interface objects may implement if they want to monitor IndexManager events.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface IndexManagerListener {

    public void documentAdded(Document document);

    public void documentRemoved(Document document);
}
