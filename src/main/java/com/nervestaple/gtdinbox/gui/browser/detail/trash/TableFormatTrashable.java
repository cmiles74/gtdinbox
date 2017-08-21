package com.nervestaple.gtdinbox.gui.browser.detail.trash;

import ca.odell.glazedlists.gui.TableFormat;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;

import java.util.Hashtable;

/**
 * Provides a TableFormat for Item objects.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TableFormatTrashable implements TableFormat {

    /** Number of columns. */
    public final int COLUMN_COUNT = 3;

    /** Table of column names. */
    private final static Hashtable COLUMN_NAMES;

    static {

        COLUMN_NAMES = new Hashtable();

        COLUMN_NAMES.put( new Integer( 0 ), "Type" );
        COLUMN_NAMES.put( new Integer( 1 ), "Filed Under" );
        COLUMN_NAMES.put( new Integer( 2 ), "Description" );
    }

    public int getColumnCount() {

        return ( COLUMN_COUNT );
    }

    public String getColumnName( int i ) {

        if( i > -1 && i < COLUMN_COUNT ) {

            return ( ( String ) COLUMN_NAMES.get( new Integer( i ) ) );
        }

        throw new IllegalStateException();
    }

    public Object getColumnValue( Object object, int i ) {

        Trashable trashable = ( Trashable ) object;

        if( i == 0 ) {

            if( trashable instanceof Project ) {
                return ( "Project" );
            } else if( trashable instanceof Category ) {
                return ( "Category" );
            } else if( trashable instanceof InboxContext ) {
                return ( "Context" );
            } else if( trashable instanceof ActionItem ) {
                return ( "Action" );
            } else if( trashable instanceof ReferenceItem ) {
                return ( "Reference" );
            }
        } else if( i == 1 ) {

            Object parent = trashable.getParent();

            if( parent == null ) {

                return ( "Unfiled" );
            } else if( parent instanceof Project ) {

                return ( ( ( Project ) parent ).getName() );
            } else if( parent instanceof Category ) {

                return ( ( ( Category ) parent ).getName() );
            }
        } else if( i == 2 ) {

            return ( trashable.getName() );
        }

        throw new IllegalStateException();
    }
}
