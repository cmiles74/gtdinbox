package com.nervestaple.gtdinbox.gui.browser.detail;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.SortedList;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.gui.GTDInboxExceptionHandler;
import com.nervestaple.gtdinbox.model.comparator.ActionItemComparator;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.project.Project;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
 * Provides a view of a list of action items.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class DetailActionItemList extends JPanel {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * List of action items.
     */
    private SortedList listItems;

    /**
     * Box to hold the action items.
     */
    private Box boxItems;

    /**
     * Exception handler.
     */
    private GTDInboxExceptionHandler exceptionHandler;

    /**
     * Index of the selected item.
     */
    private int indexSelected;

    /**
     * Mouse listener to manage clicks on the items.
     */
    private MouseListener mouseListenerItems;

    /**
     * Set of listeners.
     */
    private Set listeners;

    /**
     * Calendar instance.
     */
    private Calendar calendar;

    /**
     * Creates a new DetailActionItemList.
     *
     * @param exceptionHandler Exception handler
     */
    public DetailActionItemList( GTDInboxExceptionHandler exceptionHandler ) {

        // save a reference to the exception handler
        this.exceptionHandler = exceptionHandler;

        // create a new sorted list for the items and pass in a comparator
        listItems = new SortedList( new BasicEventList(), new ActionItemComparator() );

        // set the selected index to indicate no selection
        indexSelected = -1;

        // setup the calendar
        calendar = Calendar.getInstance();

        initializeListeners();

        // setup a set for the listeners
        listeners = new HashSet();

        // update the box of items
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                updateBoxItems( listItems );
            }
        } );
    }

    /**
     * Sets the Project to be displayed and updates the list of action items.
     *
     * @param project     Project to display
     * @param showDeleted Display items tagged for deletion
     */
    public void setProject( Project project, boolean showDeleted ) {

        listItems.clear();

        // get archive days
        Integer days = ConfigurationFactory.getInstance().getApplicationConfiguration().getArchiveDays();
        logger.debug( "Archive starts " + days + " ago" );

        // figure out the date we're looking for
        calendar.setTime( new Date() );
        calendar.add( Calendar.DAY_OF_YEAR, ( days * -1 ) );
        Date date = calendar.getTime();

        Iterator iterator = project.getActionItems().iterator();
        while( iterator.hasNext() ) {

            ActionItem actionItem = ( ActionItem ) iterator.next();

            boolean addItem = true;

            if( !showDeleted && actionItem.getDeleted().booleanValue() ) {

                addItem = false;
            }

            if( actionItem.getCompletedDate() != null
                    && ( actionItem.getCompletedDate().before( date ) || actionItem.getCompletedDate().equals( date ) ) ) {

                addItem = false;
            }

            if( addItem ) {

                listItems.add( actionItem );
            }
        }

        // update the box of items
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                updateBoxItems( listItems );
            }
        } );
    }

    /**
     * Sets the ActionItem objects to be displayed and updates the list of action items.
     *
     * @param listActionItems ActionItem objects to display
     * @param showDeleted     items tagged for deletion
     */
    public void setActionItems( EventList listActionItems, boolean showDeleted ) {

        listItems.clear();

        if( showDeleted ) {
            listItems.addAll( listActionItems );
        } else {

            Iterator iterator = listActionItems.iterator();
            while( iterator.hasNext() ) {

                ActionItem actionItem = ( ActionItem ) iterator.next();
                if( actionItem.getDeleted() != null && !actionItem.getDeleted().booleanValue() ) {
                    listItems.add( actionItem );
                }
            }
        }

        // update the box of items
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                updateBoxItems( listItems );
            }
        } );
    }

    /**
     * Sets the Context to be displayed and updates the list of action items.
     *
     * @param context     Context to display
     * @param showDeleted Display items tagged for deletion
     */
    public void setContext( InboxContext context, boolean showDeleted ) {

        listItems.clear();

        // get archive days
        Integer days = ConfigurationFactory.getInstance().getApplicationConfiguration().getArchiveDays();
        logger.debug( "Archive starts " + days + " ago" );

        // figure out the date we're looking for
        calendar.setTime( new Date() );
        calendar.add( Calendar.DAY_OF_YEAR, ( days * -1 ) );
        Date date = calendar.getTime();

        Iterator iterator = context.getActionItems().iterator();
        while( iterator.hasNext() ) {

            ActionItem actionItem = ( ActionItem ) iterator.next();

            boolean addItem = true;

            if( !showDeleted && actionItem.getDeleted().booleanValue() ) {

                addItem = false;
            } else if( actionItem.getCompletedDate() != null
                    && ( actionItem.getCompletedDate().before( date )
                    || actionItem.getCompletedDate().equals( date ) ) ) {

                addItem = false;
            }

            if( addItem ) {

                listItems.add( actionItem );
            }
        }

        // update the box of items
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                updateBoxItems( listItems );
            }
        } );
    }

    /**
     * Clears the detail list.
     */
    public void clearList() {

        listItems.clear();

        indexSelected = -1;

        // update the box of items
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                updateBoxItems( listItems );
            }
        } );
    }

    /**
     * Returns the selected ActionItem.
     *
     * @return The selected ActionItem
     */
    public ActionItem getSelectedActionItem() {

        ActionItem actionItem = null;

        if( indexSelected > -1 ) {

            actionItem = ( ActionItem ) listItems.get( indexSelected );
        }

        return ( actionItem );
    }

    public void addActionItem( ActionItem actionItem ) {

        listItems.add( actionItem );

        updateBoxItems( listItems );
    }

    public void removeActionItem( ActionItem actionItem ) {

        Iterator iterator = listItems.listIterator();
        while( iterator.hasNext() ) {

            ActionItem actionItemThis = ( ActionItem ) iterator.next();

            if( actionItemThis.getId().equals( actionItem.getId() ) ) {

                iterator.remove();
                break;
            }
        }

        logger.debug( "List contains " + listItems.size() );
        updateBoxItems( listItems );
    }

    public void updateActionItem( ActionItem actionItem ) {

        Iterator iterator = listItems.listIterator();
        while( iterator.hasNext() ) {

            ActionItem actionItemThis = ( ActionItem ) iterator.next();

            if( actionItemThis.getId().equals( actionItem.getId() ) ) {

                int index = listItems.indexOf( actionItemThis );
                iterator.remove();
                listItems.add( index, actionItem );
                break;
            }
        }

        Component[] componentsArray = boxItems.getComponents();
        for( int index = 0; index < componentsArray.length; index++ ) {

            ActionItemCheckBox actionItemCheckBox = ( ActionItemCheckBox ) componentsArray[ index ];

            if( actionItemCheckBox.getActionItem().getId().equals( actionItem.getId() ) ) {

                try {
                    actionItemCheckBox.setActionItem( actionItem );
                } catch( DataBaseManagerException e ) {
                    exceptionHandler.handleException( e );
                }

                break;
            }
        }

        SwingUtilities.invokeLater( new Runnable() {

            public void run() {

                boxItems.invalidate();
                revalidate();

                fireComponentSizeChanged();
            }
        } );
    }

    /**
     * Adds a new listener.
     *
     * @param listener Listener
     */
    public void addDetailActionItemListListener( DetailActionItemListListener listener ) {

        if( !listeners.contains( listener ) ) {

            listeners.add( listener );
        }
    }

    /**
     * Removes a Listener
     *
     * @param listener Listener
     */
    public void removeDetailActionItemListListener( DetailActionItemListListener listener ) {

        listeners.remove( listener );
    }

    public void clearSelection() {

        indexSelected = -1;

        clearActionItemCheckBoxSelected();

        fireSelectionChanged();
    }

    public Dimension getPreferredSize() {

        double height = 0;
        double width = 0;
        Component[] componentArray = getComponents();
        for( int index = 0; index < componentArray.length; index++ ) {

            height += componentArray[ index ].getPreferredSize().getHeight();

            if( componentArray[ index ].getMinimumSize().getWidth() > width ) {
                width = componentArray[ index ].getMinimumSize().getWidth();
            }
        }

        Dimension dimension = new Dimension( ( new Double( width ) ).intValue(), ( new Double( height ) ).intValue() );

        return ( dimension );
    }

    public Dimension getMinimumSize() {

        return ( getPreferredSize() );
    }

    public Dimension getMaximumSize() {

        return ( getPreferredSize() );
    }

    // private methods

    /**
     * Notifies listeners that the selected item has changed.
     */
    private void fireSelectionChanged() {

        DetailActionItemListListener[] listenersArray =
                ( DetailActionItemListListener[] ) listeners.toArray(
                        new DetailActionItemListListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].selectionChanged();
        }
    }

    /**
     * Notifies listeners that the selected item was double clicked.
     */
    private void fireSelectedItemDoubleClicked() {

        DetailActionItemListListener[] listenersArray =
                ( DetailActionItemListListener[] ) listeners.toArray(
                        new DetailActionItemListListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].selectedItemDoubleClicked();
        }
    }

    private void fireComponentSizeChanged() {

        DetailActionItemListListener[] listenersArray =
                ( DetailActionItemListListener[] ) listeners.toArray(
                        new DetailActionItemListListener[listeners.size()] );

        for( int index = 0; index < listenersArray.length; index++ ) {

            listenersArray[ index ].componentSizeChanged();
        }
    }

    /**
     * Updates the Box containing the items to keep it in sync with the list.
     *
     * @param listItems List of ActionItem objects
     */
    private void updateBoxItems( EventList listItems ) {

        // make sure we have a box to hold the items
        if( boxItems == null ) {

            boxItems = Box.createVerticalBox();

            SwingUtilities.invokeLater( new Runnable() {

                public void run() {

                    setLayout( new GridLayout( 1, 1 ) );
                    add( boxItems );
                }
            } );
        }

        clearBox();

        if( listItems.size() < 1 ) {

            // there are no items
            return;
        }

        // loop through the action items
        Iterator iterator = listItems.iterator();
        while( iterator.hasNext() ) {

            // get the next action item
            ActionItem actionItem = ( ActionItem ) iterator.next();

            try {

                // create a gui form for the action item
                final ActionItemCheckBox actionItemCheckBox = new ActionItemCheckBox( actionItem, exceptionHandler );

                // add our mouse listener
                actionItemCheckBox.addMouseListener( mouseListenerItems );

                if( !iterator.hasNext() ) {

                    // pad the bottom of the last action item
                    actionItemCheckBox.getInsets().set( 3, 5, 8, 3 );
                }

                SwingUtilities.invokeLater( new Runnable() {

                    public void run() {

                        // add it to our box
                        boxItems.add( actionItemCheckBox );
                    }
                } );
            } catch( DataBaseManagerException e ) {
                exceptionHandler.handleException( e );
            }
        }

        SwingUtilities.invokeLater( new Runnable() {

            public void run() {

                boxItems.invalidate();
                revalidate();

                fireComponentSizeChanged();
            }
        } );
    }

    /**
     * Removes all items from the box.
     */
    private void clearBox() {

        // clear the box
        removeListenersFromItems();

        SwingUtilities.invokeLater( new Runnable() {

            public void run() {

                boxItems.removeAll();
                boxItems.invalidate();
                revalidate();
            }
        } );
    }

    /**
     * Initializes listeners for the panel.
     */
    private void initializeListeners() {

        mouseListenerItems = new MouseAdapter() {

            public void mouseClicked( MouseEvent event ) {

                // clear selected index
                indexSelected = -1;

                // get the selected action tiem check box
                ActionItemCheckBox actionItemCheckBox = ( ActionItemCheckBox ) event.getSource();

                // set selected index
                indexSelected = listItems.indexOf( actionItemCheckBox.getActionItem() );

                // set item as selected
                setActionItemCheckBoxSelected( actionItemCheckBox );

                // notifie listeners the the selection has changed
                fireSelectionChanged();

                if( event.getClickCount() == 2 ) {

                    // notify listeners that the item was double-clicked
                    fireSelectedItemDoubleClicked();
                }
            }
        };
    }

    private void setActionItemCheckBoxSelected( ActionItemCheckBox actionItemCheckBox ) {

        Component[] componentArray = boxItems.getComponents();
        for( int index = 0; index < componentArray.length; index++ ) {

            ActionItemCheckBox actionItemCheckBoxThis = ( ActionItemCheckBox ) componentArray[ index ];

            if( actionItemCheckBoxThis == actionItemCheckBox ) {

                actionItemCheckBoxThis.setSelected( true );
            } else {

                actionItemCheckBoxThis.setSelected( false );
            }
        }
    }

    private void clearActionItemCheckBoxSelected() {

        Component[] componentArray = boxItems.getComponents();
        for( int index = 0; index < componentArray.length; index++ ) {

            ActionItemCheckBox actionItemCheckBoxThis = ( ActionItemCheckBox ) componentArray[ index ];

            actionItemCheckBoxThis.setSelected( false );
        }
    }

    private void removeListenersFromItems() {

        Component[] componentArray = boxItems.getComponents();
        for( int index = 0; index < componentArray.length; index++ ) {

            componentArray[ index ].removeMouseListener( mouseListenerItems );
        }
    }
}
