package com.nervestaple.gtdinbox.model;

/**
 * Provides an interface that objects can implement if they can be stored in the "Trash", that is, if they may be tagged
 * for deletion at a later date.
 */
public interface Trashable {

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
     * Flag indiciating that the Trashable has been deleted.
     *
     * @param deleted flag
     */
    public void setDeleted(Boolean deleted);

    /**
     * Returns the deleted flag for the Trashable.
     *
     * @return deleted flag
     */
    public Boolean getDeleted();

    /**
     * Called before a Trashable is deleted.
     */
    public void prepareForDeletion();
}
