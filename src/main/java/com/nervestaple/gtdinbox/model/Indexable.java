package com.nervestaple.gtdinbox.model;

/**
 * Provides an interface the indicates the implementing class can be indexed.
 */
public interface Indexable {

    /**
     * Returns the Indexble's unique id.
     *
     * @return unique identifier of the Indexable
     */
    public Object getId();
}
