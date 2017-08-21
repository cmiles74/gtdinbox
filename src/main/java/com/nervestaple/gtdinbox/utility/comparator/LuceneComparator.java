package com.nervestaple.gtdinbox.utility.comparator;

import org.apache.lucene.document.Document;

import java.util.Comparator;

/**
 * Provides a comparator that sorts Lucene documents by a "rank" field.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class LuceneComparator implements Comparator {

    public LuceneComparator() {

    }


    public int compare( Object o, Object o1 ) {

        Document document1 = ( Document ) o;
        Document document2 = ( Document ) o1;

        Float rank1 = Float.parseFloat( document1.get( "rank" ) );
        Float rank2 = Float.parseFloat( document2.get( "rank" ) );

        return ( rank1.compareTo( rank2 ) * -1 );
    }
}
