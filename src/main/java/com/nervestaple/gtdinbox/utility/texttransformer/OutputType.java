package com.nervestaple.gtdinbox.utility.texttransformer;

import org.hibernate.result.Output;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides an enumeration that defines the different types of output text.
 */
public class OutputType {

    /**
     * Output type.
     */
    private String type;

    /**
     * Title output type.
     */
    public final static OutputType TITLE = new OutputType("title");

    /**
     * Description output type.
     */
    public final static OutputType DESCRIPTION = new OutputType("description");

    /**
     * Action item output type.
     */
    public final static OutputType ACTION_ITEM = new OutputType("action-item");

    /**
     * Action item completed output type.
     */
    public final static OutputType ACTION_ITEM_COMPLETED = new OutputType("action-item-completed");

    /**
     * Action item output type.
     */
    public final static OutputType SUBTITLE = new OutputType("subtitle");

    /**
     * Map of types.
     */
    private static final Map<String, OutputType> INSTANCES = new HashMap<>();

    // populate our map with our types
    static {
        INSTANCES.put(TITLE.toString(), TITLE);
        INSTANCES.put(DESCRIPTION.toString(), DESCRIPTION);
        INSTANCES.put(ACTION_ITEM.toString(), ACTION_ITEM);
        INSTANCES.put(ACTION_ITEM_COMPLETED.toString(), ACTION_ITEM_COMPLETED);
        INSTANCES.put(ACTION_ITEM.toString(), SUBTITLE);
    }

    private OutputType(String type) {

        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Returns the OutputType for the given string.
     *
     * @param type name of OutputType
     * @return OutputType
     */
    public static OutputType getInstance(String type) {

        return INSTANCES.get(type);
    }
}
