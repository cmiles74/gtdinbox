package com.nervestaple.gtdinbox.gui.browser;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides the top level containers that we display in the browser.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TopLevelContainer {

    /**
     * The name of the container.
     */
    private String name;

    /**
     * The Project container.
     */
    public static final TopLevelContainer PROJECTS = new TopLevelContainer("Projects");

    /**
     * The Contexts container.
     */
    public static final TopLevelContainer CONTEXTS = new TopLevelContainer("Contexts");

    /**
     * The Categories container.
     */
    public static final TopLevelContainer CATEGORIES = new TopLevelContainer("Categories");

    /**
     * The Trash container.
     */
    public static final TopLevelContainer TRASH = new TopLevelContainer("Trash");

    /**
     * The Archive container.
     */
    public static final TopLevelContainer ARCHIVE = new TopLevelContainer("Archive");

    /**
     * Map of containers.
     */
    private static final Map INSTANCES = new HashMap();

    static {

        INSTANCES.put(PROJECTS.toString(), PROJECTS);
        INSTANCES.put(CONTEXTS.toString(), CONTEXTS);
        INSTANCES.put(CATEGORIES.toString(), CATEGORIES);
        INSTANCES.put(ARCHIVE.toString(), ARCHIVE);
        INSTANCES.put(TRASH.toString(), TRASH);
    }

    /**
     * Creates a new TopLevelContainer and sets its name.
     *
     * @param name Name of the container
     */
    private TopLevelContainer(String name) {

        this.name = name;
    }

    /**
     * Returns the name of the container.
     *
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the TopLevelContainer for the given name.
     *
     * @param name Name of the container
     * @return TopLevelContainer
     */
    public static TopLevelContainer getInstance(String name) {

        return ((TopLevelContainer) INSTANCES.get(name));
    }

    // other methods

    /**
     * Returns a String representation of the container.
     *
     * @return
     */
    public String toString() {

        return (name);
    }
}
