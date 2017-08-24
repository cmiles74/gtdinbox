package com.nervestaple.gtdinbox.index;

import com.nervestaple.gtdinbox.datastore.index.IndexManager;
import com.nervestaple.gtdinbox.datastore.index.IndexManagerException;
import com.nervestaple.gtdinbox.model.Indexable;
import org.apache.log4j.Logger;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

/**
 * Provides a listener that indexes new entities as they are created and updated.
 */
public class IndexListener {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());


    @PostPersist
    public void postPersist(Indexable entity) {

        try {
            IndexManager.getInstance().addIndexable(entity);
        } catch (IndexManagerException exception) {
            logger.warn("Could not index entity!", exception);
        }
    }

    @PostUpdate
    public void postUpdate(Indexable entity) {
        try {
            IndexManager.getInstance().updateIndexable(entity);
        } catch (IndexManagerException exception) {
            logger.warn("Could not index entity!", exception);
        }
    }

    @PostRemove
    public void postRemove(Indexable entity) {
        try {
            IndexManager.getInstance().removeIndexable(entity);
        } catch (IndexManagerException exception) {
            logger.warn("Could not index entity!", exception);
        }
    }
}
