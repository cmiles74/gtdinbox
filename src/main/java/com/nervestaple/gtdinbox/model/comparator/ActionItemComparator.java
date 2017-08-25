package com.nervestaple.gtdinbox.model.comparator;

import com.nervestaple.gtdinbox.model.item.Item;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import org.apache.log4j.Logger;

import java.util.Comparator;
import java.util.Date;

/**
 * Provides a comparator for Item instances.
 */
public class ActionItemComparator implements Comparator<Item> {

    /**
     * We want to sort Item instances by their creation date.
     *
     * @param a First Item
     * @param b Second Item
     * @return comparator result
     */
    public int compare(Item a, Item b) {

        if (a != null && b != null) {

            ActionItem actionItemA = (ActionItem) a;
            ActionItem actionItemB = (ActionItem) b;

            // get the dates for the two action times
            Date dateA = actionItemA.getCreatedDate();
            Date dateB = actionItemB.getCreatedDate();

            return ((new Long(dateB.getTime())).compareTo(new Long(dateA.getTime())));
        }

        return (0);
    }
}
