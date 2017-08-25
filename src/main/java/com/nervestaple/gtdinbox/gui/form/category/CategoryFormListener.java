package com.nervestaple.gtdinbox.gui.form.category;

import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.model.reference.category.Category;

/**
 * Provides an interface objects may implement if they want to respond to CategoryForm events
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface CategoryFormListener {

    /**
     * Called when a new Category is added.
     *
     * @param category Category that was added
     */
    public void categoryAdded(Category category);

    /**
     * Called when a new Category is updated.
     *
     * @param category Category that was updated
     */
    public void categoryUpdated(Category category);

    /**
     * Called when an error occurs.
     *
     * @param exception The exception that occurred
     */
    public void exceptionOccurred(GTDInboxException exception);
}
