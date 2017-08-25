package com.nervestaple.gtdinbox.gui.browser.detail.searchresults;

import ca.odell.glazedlists.gui.AdvancedTableFormat;
import com.nervestaple.gtdinbox.utility.comparator.SimpleComparator;
import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

/**
 * Provides a table model for search results.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TableFormatSearchResult implements AdvancedTableFormat {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * Number of columns.
     */
    public final int COLUMN_COUNT = 4;

    /**
     * Table of column names.
     */
    private final static Hashtable COLUMN_NAMES;

    /**
     * Table of column classes.
     */
    private final static Hashtable COLUMN_CLASSES;

    /**
     * Table of column classes.
     */
    private final static Hashtable COLUMN_COMPARATOR;

    static {

        COLUMN_NAMES = new Hashtable();

        COLUMN_NAMES.put(new Integer(0), "Type");
        COLUMN_NAMES.put(new Integer(1), "Filed Under");
        COLUMN_NAMES.put(new Integer(2), "Modified");
        COLUMN_NAMES.put(new Integer(3), "Description");

        COLUMN_CLASSES = new Hashtable();

        COLUMN_CLASSES.put(new Integer(0), String.class);
        COLUMN_CLASSES.put(new Integer(1), String.class);
        COLUMN_CLASSES.put(new Integer(2), Date.class);
        COLUMN_CLASSES.put(new Integer(3), String.class);

        COLUMN_COMPARATOR = new Hashtable();

        COLUMN_COMPARATOR.put(new Integer(0), new SimpleComparator());
        COLUMN_COMPARATOR.put(new Integer(1), new SimpleComparator());
        COLUMN_COMPARATOR.put(new Integer(2), new SimpleComparator());
        COLUMN_COMPARATOR.put(new Integer(3), new SimpleComparator());
    }

    public int getColumnCount() {

        return (COLUMN_COUNT);
    }

    public String getColumnName(int i) {

        if (i > -1 && i < COLUMN_COUNT) {

            return ((String) COLUMN_NAMES.get(new Integer(i)));
        }

        throw new IllegalStateException();
    }

    public Class getColumnClass(int i) {

        if (i > -1 && i < COLUMN_COUNT) {

            return ((Class) COLUMN_CLASSES.get(new Integer(i)));
        }

        throw new IllegalStateException();
    }

    public Comparator getColumnComparator(int i) {

        if (i > -1 && i < COLUMN_COUNT) {

            return ((Comparator) COLUMN_COMPARATOR.get(new Integer(i)));
        }

        throw new IllegalStateException();
    }

    public Object getColumnValue(Object object, int i) {

        Document document = (Document) object;

        if (i == 0) {

            String className = document.get("class");

            if (className == null) {
                return ("Unkown");
            } else if (className.equals("com.nervestaple.gtdinbox.model.item.actionitem.actionitem")) {
                return ("Action");
            } else if (className.equals("com.nervestaple.gtdinbox.model.inboxcontext.inboxcontext")) {
                return ("Context");
            } else if (className.equals("com.nervestaple.gtdinbox.model.project.project")) {
                return ("Project");
            } else if (className.equals("com.nervestaple.gtdinbox.model.reference.category.category")) {
                return ("Category");
            } else if (className.equals("com.nervestaple.gtdinbox.model.item.referenceitem.referenceitem")) {
                return ("Reference");
            }

            return ("Unknown");
        } else if (i == 1) {

            String filedUnder = document.get("parent");

            if (filedUnder != null) {
                return (filedUnder);
            } else {
                return (" ");
            }
        } else if (i == 2) {

            Date date = new Date(0);

            if (document.get("lastModifiedDate") != null) {

                try {
                    date = DateTools.stringToDate(document.get("lastModifiedDate"));
                } catch (ParseException e) {
                    logger.warn(e);
                }
            }

            return (date);
        } else if (i == 3) {

            if (document.get("description") != null) {
                return (document.get("description"));
            } else {
                return (" ");
            }
        }

        logger.debug("No data for " + i + " with " + document);

        throw new IllegalStateException();
    }
}