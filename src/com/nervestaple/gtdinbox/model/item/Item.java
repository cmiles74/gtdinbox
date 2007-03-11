package com.nervestaple.gtdinbox.model.item;

import java.util.Date;

/**
 * Provides an interface that all items must implement. We use this items to manipulate bags that have a mix of both
 * action and reference items.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface Item {

    public Long getId();

    public String getName();

    public Object getParent();

    public Date getCreatedDate();
}
