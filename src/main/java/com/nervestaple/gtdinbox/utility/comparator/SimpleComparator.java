package com.nervestaple.gtdinbox.utility.comparator;

import java.util.Comparator;

/**
 * Provides a very simple comparator. All objects passed in must implement the Comparable interface.
 */
public class SimpleComparator implements Comparator {

    public SimpleComparator() {

    }

    public int compare(Object o, Object o1) {

        Comparable comparable1 = (Comparable) o;
        Comparable comparable2 = (Comparable) o1;

        return (comparable1.compareTo(comparable2));
    }
}
