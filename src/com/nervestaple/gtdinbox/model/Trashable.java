package com.nervestaple.gtdinbox.model;

/**
 * Provides an interface that objects can implement if they can be stored in the "Trash", that is, if they may be tagged
 * for deletion at a later date.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface Trashable {

    public Long getId();

    public String getName();

    public Object getParent();

    public void setDeleted( Boolean deleted );

    public Boolean getDeleted();

    public void prepareForDeletion();
}
