package com.nervestaple.gtdinbox.utility.comparator;

import org.apache.lucene.document.Document;

import java.util.Comparator;

/**
 * Provides a comparator that sorts Lucene documents by a "rank" field.
 */
public class LuceneComparator implements Comparator<Document> {

    public LuceneComparator() {

    }

    /**
     * Compares two Lucene Document instances by their rank
     *
     * @param document1 Lucene Document
     * @param document2 Lucene Document
     * @return comparison result
     */
    public int compare(Document document1, Document document2) {

        Float rank1 = Float.parseFloat(document1.get("rank"));
        Float rank2 = Float.parseFloat(document2.get("rank"));

        return (rank1.compareTo(rank2) * -1);
    }
}
