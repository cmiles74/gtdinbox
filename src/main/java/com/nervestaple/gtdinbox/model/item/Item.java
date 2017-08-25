package com.nervestaple.gtdinbox.model.item;

import java.util.Date;

/**
 * Provides an interface that all items must implement. We use this items to manipulate bags that have a mix of both
 * action and reference items.
 */
public interface Item {

    /**
     * The unique identifier of the Item.
     *
     * @return unique identifier of the Item
     */
    public Long getId();

    /**
     * The name of the Item.
     *
     * @return name of the Item
     */
    public String getName();

    /**
     * The parent of this Item.
     *
     * @return parent of the Item
     */
    public Object getParent();

    /**
     * Date when the Item was created
     *
     * @return creation date of the Item
     */
    public Date getCreatedDate();
}
