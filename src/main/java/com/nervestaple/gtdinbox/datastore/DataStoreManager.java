package com.nervestaple.gtdinbox.datastore;

import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Provides some simple methods for getting data from the data store.
 *
 * @author Christopher Miles
 * @version 1.0
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
    public static Collection getProjects() throws DataStoreException {

        try {

            List projects = DataBaseManager.getInstance().getSession()
                    .createCriteria( Project.class )
                    .add( Expression.eq( "deleted", Boolean.valueOf( false ) ) ).list();

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
    public static Collection getContexts() throws DataStoreException {

        try {

            List contexts = DataBaseManager.getInstance().getSession()
                    .createCriteria( InboxContext.class )
                    .add( Expression.eq( "deleted", Boolean.valueOf( false ) ) ).list();

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
    public static Collection getCategories() throws DataStoreException {

        try {

            List categories = DataBaseManager.getInstance().getSession()
                    .createCriteria( Category.class )
                    .add( Expression.eq( "deleted", Boolean.valueOf( false ) ) ).list();

            return ( categories );
        } catch( DataBaseManagerException e ) {

            throw new DataStoreException( e );
        }
    }

    /**
     * Returns a list of all objects that are tagged for deletion in the data store.
     *
     * @return A list of objects tagged for deletion.
     * @throws DataStoreException On problems reading from the data store
     */
    public static Collection getTrash() throws DataStoreException {

        try {

            List trash = DataBaseManager.getInstance().getSession()
                    .createCriteria( Trashable.class )
                    .add( Expression.eq( "deleted", Boolean.valueOf( true ) ) ).list();

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
    public static Collection getArchive() throws DataStoreException {

        // get archive days
        Integer days = ConfigurationFactory.getInstance().getApplicationConfiguration().getArchiveDays();
        logger.debug( "Archive starts " + days + " ago" );

        // figure out the date we're looking for
        calendar.setTime( new Date() );
        calendar.add( Calendar.DAY_OF_YEAR, ( days * -1 ) );
        Date date = calendar.getTime();

        try {

            Criteria criteria = DataBaseManager.getInstance().getSession().createCriteria( ActionItem.class );
            criteria.add( Expression.isNotNull( "completedDate" ) );
            criteria.add( Expression.le( "completedDate", date ) );

            List archive = criteria.list();

            logger.debug( "Archive contains " + archive.size() + " objects" );
            return ( archive );
        } catch( DataBaseManagerException e ) {

            throw new DataStoreException( e );
        }
    }
}
