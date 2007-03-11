package com.nervestaple.gtdinbox.model.comparator;

import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.Date;

/**
 * Provides a comparator for ActionItems.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ActionItemComparator implements Comparator {

    /** Logger instance. */
    private Logger logger = Logger.getLogger( this.getClass() );

    public int compare( Object a, Object b ) {

        if( a != null && a instanceof ActionItem && b != null && b instanceof ActionItem ) {

            ActionItem actionItemA = ( ActionItem ) a;
            ActionItem actionItemB = ( ActionItem ) b;

            // get the dates for the two action times
            Date dateA = actionItemA.getCreatedDate();
            Date dateB = actionItemB.getCreatedDate();

            return ( ( new Long( dateB.getTime() ) ).compareTo( new Long( dateA.getTime() ) ) );
        }

        return ( 0 );
    }
}
