package com.nervestaple.gtdinbox.datastore;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.gtdinbox.model.tag.Tag;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Provides some simple methods for getting data from the data store.
 */
public class DataStoreManager {

    /**
     * Logger instance.
     */
    private static Logger logger = Logger.getLogger( "com.nervestaple.gtdinbox.datastore.DataStoreManager" );

    /**
     * Calendar instance.
     */
    private static Calendar calendar = Calendar.getInstance();

    /**
     * Returns a list of all Projects objects in the data store.
     *
     * @return A list of Project objects
     * @throws DataStoreException On problems reading from the data store
     */
    public static Collection<Project> getProjects() throws DataStoreException {

        try {

            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Project> query = builder.createQuery(Project.class);
            Root<Project> project = query.from(Project.class);
            query.select(project).where(builder.or(builder.isNotNull(project.get("deleted")),
                    builder.equal(project.get("deleted"), true)));
            List<Project> projects = entityManager.createQuery(query).getResultList();

            return ( projects );
        } catch( DataBaseManagerException e ) {

            throw new DataStoreException( e );
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

            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<InboxContext> query = builder.createQuery(InboxContext.class);
            Root<InboxContext> context = query.from(InboxContext.class);
            query.select(context).where(builder.or(builder.isNotNull(context.get("deleted")),
                    builder.equal(context.get("deleted"), true)));
            List<InboxContext> contexts = entityManager.createQuery(query).getResultList();

            return ( contexts );
        } catch( DataBaseManagerException e ) {

            throw new DataStoreException( e );
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

            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Category> query = builder.createQuery(Category.class);
            Root<Category> category = query.from(Category.class);
            query.select(category).where(builder.or(builder.isNotNull(category.get("deleted")),
                    builder.equal(category.get("deleted"), true)));
            List<Category> categories = entityManager.createQuery(query).getResultList();

            return ( categories );
        } catch( DataBaseManagerException e ) {

            throw new DataStoreException( e );
        }
    }

    private static List<Trashable> getTrashedItemsByClass(Class entityClass) throws DataStoreException {

        List<Trashable> trashables = new ArrayList<>();

        EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<ActionItem> query = builder.createQuery(entityClass);
        Root trashable = query.from(entityClass);
        query.select(trashable).where(builder.equal(trashable.get("deleted"), true));
        List trash = entityManager.createQuery(query).getResultList();
        trashables.addAll(trash);

        return trashables;
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
            trash.addAll(getTrashedItemsByClass(InboxContext.class));
            trash.addAll(getTrashedItemsByClass(ActionItem.class));
            trash.addAll(getTrashedItemsByClass(ReferenceItem.class));
            trash.addAll(getTrashedItemsByClass(Project.class));
            trash.addAll(getTrashedItemsByClass(Category.class));
            trash.addAll(getTrashedItemsByClass(Tag.class));


            logger.debug( "Trash contains " + trash.size() + " objects" );
            return ( trash );
        } catch( DataBaseManagerException e ) {

            throw new DataStoreException( e );
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
        logger.debug( "Archive starts " + days + " ago" );

        // figure out the date we're looking for
        calendar.setTime( new Date() );
        calendar.add( Calendar.DAY_OF_YEAR, ( days * -1 ) );
        Date date = calendar.getTime();

        try {

            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ActionItem> query = builder.createQuery(ActionItem.class);
            Root<ActionItem> item = query.from(ActionItem.class);
            query.select(item).where(builder.lessThanOrEqualTo(item.get("completedDate"), date));
            List<ActionItem> archive = entityManager.createQuery(query).getResultList();

            logger.debug( "Archive contains " + archive.size() + " objects" );
            return ( archive );
        } catch( DataBaseManagerException e ) {

            throw new DataStoreException( e );
        }
    }
}
