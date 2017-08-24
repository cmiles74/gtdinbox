package com.nervestaple.gtdinbox.gui.browser.detail.trash;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import com.nervestaple.gtdinbox.datastore.DataStoreException;
import com.nervestaple.gtdinbox.datastore.DataStoreManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.gui.GTDInboxExceptionHandler;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * Provides a controller class for the TrashDetail form.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TrashDetailController {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Form for this controller.
     */
    private TrashDetailForm form;

    /**
     * Model for this controller.
     */
    private TrashDetailModel model;

    /**
     * Exception handler.
     */
    private GTDInboxExceptionHandler exceptionHandler;

    /**
     * Listeners.
     */
    private Set listeners;

    /**
     * Creates a new TrashDetailController.
     */
    public TrashDetailController( GTDInboxExceptionHandler exceptionHandler ) {

        this.exceptionHandler = exceptionHandler;

        model = new TrashDetailModel();

        form = new TrashDetailForm( model );

        listeners = new HashSet();

        initializeModelListeners();
    }

    /**
     * Loads the trash from the data store.
     */
    public void loadData() {

        try {

            Collection collection = DataStoreManager.getTrash();
            EventList list = new BasicEventList();
            list.addAll( collection );
            model.setListItems( list );
        } catch( DataStoreException e ) {
            exceptionHandler.handleException( e );
        }
    }

    /**
     * Registers a new listener with the controller.
     *
     * @param listener Listener to be registered.
     */
    public void addTrashDetailControllerListener( TrashDetailControllerListener listener ) {

        if( !listeners.contains( listener ) ) {

            listeners.add( listener );
        }
    }

    /**
     * De-registers a listener the the controller.
     *
     * @param listener The listener to remove.
     */
    public void removeTrashDetailControllerListener( TrashDetailControllerListener listener ) {

        listeners.remove( listener );
    }

    /**
     * Deletes all items currently in the trash.
     */
    public void emptyTrash() {

        Thread thread = new Thread( new Runnable() {

            public void run() {

                List listProjects = new ArrayList();
                List listContexts = new ArrayList();
                List listCategories = new ArrayList();

                Iterator iterator = model.getListItems().listIterator();
                while( iterator.hasNext() ) {

                    Trashable trashable = ( Trashable ) iterator.next();
                    logger.debug( trashable.getClass() );

                    // if it's a container item, hold it out for last
                    boolean holdForLater = false;
                    if( trashable instanceof Project ) {
                        listProjects.add( trashable );
                        holdForLater = true;
                    } else if( trashable instanceof InboxContext ) {
                        listContexts.add( trashable );
                        holdForLater = true;
                    } else if( trashable instanceof Category ) {
                        listCategories.add( trashable );
                        holdForLater = true;
                    }

                    if( !holdForLater ) {

                        try {
                            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();

                            // make sure we have an up-to-date version
                            entityManager.refresh( trashable );
                            trashable.prepareForDeletion();

                            // start a new transaction and delete the item
                            DataBaseManager.getInstance().beginTransaction();
                            logger.debug( "DELETING: " + trashable );
                            entityManager.remove( trashable );

                            // commit the transaction and close our session
                            DataBaseManager.getInstance().commitTransaction();
                        } catch( DataBaseManagerException e ) {
                            exceptionHandler.handleException( e );
                        }
                    }
                }

                if( listProjects.size() > 0 ) {

                    iterator = listProjects.iterator();
                    while( iterator.hasNext() ) {

                        Project project = ( Project ) iterator.next();

                        try {

                            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();

                            // make sure we have an up-to-date version
                            entityManager.refresh( project );
                            project.prepareForDeletion();

                            if( project.getActionItems().size() < 1 ) {

                                // start a new transaction and delete the item
                                DataBaseManager.getInstance().beginTransaction();
                                entityManager.remove( project );

                                // commit the transaction and close our session
                                DataBaseManager.getInstance().commitTransaction();
                            }
                        } catch( DataBaseManagerException e ) {
                            exceptionHandler.handleException( e );
                        }
                    }
                }

                if( listContexts.size() > 0 ) {

                    iterator = listContexts.iterator();
                    while( iterator.hasNext() ) {

                        InboxContext context = ( InboxContext ) iterator.next();

                        try {

                            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();

                            // make sure we have an up-to-date version
                            entityManager.refresh( context );
                            context.prepareForDeletion();

                            if( context.getActionItems().size() < 1 ) {

                                // start a new transaction and delete the item
                                DataBaseManager.getInstance().beginTransaction();
                                entityManager.remove( context );

                                // commit the transaction and close our session
                                DataBaseManager.getInstance().commitTransaction();
                            }
                        } catch( DataBaseManagerException e ) {
                            exceptionHandler.handleException( e );
                        }
                    }
                }

                if( listCategories.size() > 0 ) {

                    iterator = listCategories.iterator();
                    while( iterator.hasNext() ) {

                        Category category = ( Category ) iterator.next();

                        try {

                            EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();

                            // make sure we have an up-to-date version
                            entityManager.refresh( category );
                            category.prepareForDeletion();

                            if( category.getReferenceItems().size() < 1 ) {

                                // start a new transaction and delete the item
                                DataBaseManager.getInstance().beginTransaction();
                                entityManager.remove( category );

                                // commit the transaction and close our session
                                DataBaseManager.getInstance().commitTransaction();
                            }
                        } catch( DataBaseManagerException e ) {
                            exceptionHandler.handleException( e );
                        }
                    }
                }

                loadData();
            }
        } );
        thread.start();
    }

    // accessor and mutator methods

    public TrashDetailForm getForm() {
        return form;
    }

    public TrashDetailModel getModel() {
        return model;
    }

    // private methods

    private void firePutAwayTrashable( Trashable trashable ) {

        TrashDetailControllerListener[] listenerArray = ( TrashDetailControllerListener[] ) listeners.toArray(
                new TrashDetailControllerListener[listeners.size()] );

        for( int index = 0; index < listenerArray.length; index++ ) {

            listenerArray[ index ].putAwayTrashable( trashable );
        }
    }

    private void fireConfirmEmptyTrash( String message ) {

        TrashDetailControllerListener[] listenerArray = ( TrashDetailControllerListener[] ) listeners.toArray(
                new TrashDetailControllerListener[listeners.size()] );

        for( int index = 0; index < listenerArray.length; index++ ) {

            listenerArray[ index ].confirmEmptyTrash( message );
        }
    }

    private void addPutAwayListener() {

        model.setActionListenerPutAway( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                final List listItems = model.getSelectedItems();

                Thread thread = new Thread( new Runnable() {

                    public void run() {

                        Iterator iterator = listItems.iterator();
                        while( iterator.hasNext() ) {

                            Trashable trashable = ( Trashable ) iterator.next();

                            trashable.setDeleted( new Boolean( false ) );

                            try {
                                EntityManager entityManager = DataBaseManager.getInstance().getEntityManager();

                                // start a new transaction and save the item
                                DataBaseManager.getInstance().beginTransaction();
                                entityManager.persist( trashable );

                                // commit the transaction and close our session
                                DataBaseManager.getInstance().commitTransaction();

                                // notify listeners
                                firePutAwayTrashable( trashable );
                            } catch( DataBaseManagerException e ) {
                                exceptionHandler.handleException( e );
                            }
                        }

                        loadData();
                    }
                } );
                thread.start();
            }
        } );
    }

    private void addEmptyTrashListener() {

        model.setActionListenerEmptyTrash( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                String message = "The trash contains " + model.getListItems().size() + " items.";

                fireConfirmEmptyTrash( message );
            }
        } );
    }

    private void initializeModelListeners() {

        model.addPropertyChangeListener( new PropertyChangeListener() {

            public void propertyChange( PropertyChangeEvent event ) {

                logger.debug( event.getPropertyName() + ": " + event.getOldValue() + " -> " + event.getNewValue() );
            }
        } );

        model.addPropertyChangeListener( "listItems", new PropertyChangeListener() {

            public void propertyChange( PropertyChangeEvent event ) {

                if( model.getListItems().size() > 0 ) {

                    model.setDescription( "The trash contains " + model.getListItems().size() + " items." );
                    addEmptyTrashListener();
                } else {
                    model.setDescription( "The trash is empty." );
                    model.setActionListenerEmptyTrash( null );
                }
            }
        } );

        model.addPropertyChangeListener( "selectedItems", new PropertyChangeListener() {

            public void propertyChange( PropertyChangeEvent propertyChangeEvent ) {

                if( model.getSelectedItems().size() > 0 ) {

                    addPutAwayListener();
                } else {

                    model.setActionListenerPutAway( null );
                }
            }
        } );
    }
}
