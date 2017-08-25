package com.nervestaple.gtdinbox.datastore;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.datastore.database.QueryBuilder;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.gtdinbox.model.tag.Tag;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Provides some simple methods for getting data from the data store.
 */
public class DataStoreManager {

    /**
     * Logger instance.
     */
    private static Logger logger = Logger.getLogger("com.nervestaple.gtdinbox.datastore.DataStoreManager");

    /**
     * Calendar instance.
     */
    private static Calendar calendar = Calendar.getInstance();

    /**
     * Singleton instance.
     */
    private final static DataStoreManager dataStoreManager;

    // query builders
    private static QueryBuilder<Project> projectQueryBuilder;
    private static QueryBuilder<ActionItem> actionItemQueryBuilder;
    private static QueryBuilder<InboxContext> inboxContextQueryBuilder;
    private static QueryBuilder<Category> categoryQueryBuilder;
    private static QueryBuilder<ReferenceItem> referenceItemQueryBuilder;
    private static QueryBuilder<Tag> tagQueryBuilder;

    static {

        dataStoreManager = new DataStoreManager();
    }

    private DataStoreManager() {

        projectQueryBuilder =  new QueryBuilder<>(Project.class);
        actionItemQueryBuilder = new QueryBuilder<>(ActionItem.class);
        inboxContextQueryBuilder = new QueryBuilder<>(InboxContext.class);
        categoryQueryBuilder = new QueryBuilder<>(Category.class);
        referenceItemQueryBuilder = new QueryBuilder<>(ReferenceItem.class);
        tagQueryBuilder = new QueryBuilder<>(Tag.class);
    }

    /**
     * Returns a list of all Projects objects in the data store.
     *
     * @return A list of Project objects
     * @throws DataStoreException On problems reading from the data store
     */
    public static Collection<Project> getProjects() throws DataStoreException {

        try {

            List<Project> items = projectQueryBuilder.buildNotDeletedQuery().getResultList();

            return (items);
        } catch (DataBaseManagerException e) {

            throw new DataStoreException(e);
        }
    }

    /**
     * Returns a list of all InboxContext objects in the data store.
     *
     * @return A list of InboxContext objects
     * @throws DataStoreException On problems reading from the data store
     */
    public static Collection<InboxContext> getContexts() throws DataStoreException {

        try {

            List<InboxContext> contexts = inboxContextQueryBuilder.buildNotDeletedQuery().getResultList();

            return (contexts);
        } catch (DataBaseManagerException e) {

            throw new DataStoreException(e);
        }
    }

    /**
     * Returns a list of all Category objects in the data store.
     *
     * @return A list of Category objects
     * @throws DataStoreException On problems reading from the data store
     */
    public static Collection<Category> getCategories() throws DataStoreException {

        try {

            List<Category> categories = categoryQueryBuilder.buildNotDeletedQuery().getResultList();

            return (categories);
        } catch (DataBaseManagerException e) {

            throw new DataStoreException(e);
        }
    }

    /**
     * Returns a list of all objects that are tagged for deletion in the data store.
     *
     * @return A list of objects tagged for deletion.
     * @throws DataStoreException On problems reading from the data store
     */
    public static Collection<Trashable> getTrash() throws DataStoreException {

        try {

            List<Trashable> trash = new ArrayList<>();

            trash.addAll(actionItemQueryBuilder.buildDeletedQuery().getResultList());
            trash.addAll(projectQueryBuilder.buildDeletedQuery().getResultList());
            trash.addAll(inboxContextQueryBuilder.buildDeletedQuery().getResultList());
            trash.addAll(categoryQueryBuilder.buildDeletedQuery().getResultList());
            trash.addAll(referenceItemQueryBuilder.buildDeletedQuery().getResultList());
            trash.addAll(tagQueryBuilder.buildDeletedQuery().getResultList());

            logger.debug("Trash contains " + trash.size() + " objects");
            return (trash);
        } catch (DataBaseManagerException e) {

            throw new DataStoreException(e);
        }
    }

    /**
     * Returns a list of all objects that are tagged as completed in the data store.
     *
     * @return A list of objects tagged as completed.
     * @throws DataStoreException On problems reading from the data store
     */
    public static Collection<ActionItem> getArchive() throws DataStoreException {

        // get archive days
        Integer days = ConfigurationFactory.getInstance().getApplicationConfiguration().getArchiveDays();
        logger.debug("Archive starts " + days + " ago");

        // figure out the date we're looking for
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, (days * -1));
        Date date = calendar.getTime();

        try {

            List<ActionItem> archive = actionItemQueryBuilder.buildArchivedQuery(date).getResultList();

            logger.debug("Archive contains " + archive.size() + " objects");
            return (archive);
        } catch (DataBaseManagerException e) {

            throw new DataStoreException(e);
        }
    }
}
