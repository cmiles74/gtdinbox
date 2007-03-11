package com.nervestaple.gtdinbox.model.textstyletypes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * An object that represents a type of plain text style, like Markdown or HumaneText.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TextStyleType implements Serializable {

    /** The type of text style. */
    private String type;

    /** The plain text style type. */
    public static final TextStyleType PLAIN_TEXT = new TextStyleType( "plain-text" );

    /** The markdown text style type. */
    public static final TextStyleType MARKDOWN_TEXT = new TextStyleType( "markdown-text" );

    /** Map of types. */
    private static final Map INSTANCES = new HashMap();

    // populate our map with our types
    static {
        INSTANCES.put( PLAIN_TEXT.toString(), PLAIN_TEXT );
        INSTANCES.put( MARKDOWN_TEXT.toString(), MARKDOWN_TEXT );
    }

    /**
     * Creates a new TextStyleType.
     *
     * @param type TextStyleType bame
     */
    private TextStyleType( String type ) {

        this.type = type;
    }

    public String toString() {
        return ( type );
    }

    /**
     * Returns the TextStyleType for this TextStyleType.
     *
     * @return TextStyleType
     */
    Object readResolve() {
        return ( getInstance( type ) );
    }

    /**
     * Returns the TextSyleType for the given string.
     *
     * @param type name of TextStyleType
     * @return TextStyleType
     */
    public static TextStyleType getInstance( String type ) {

        return ( ( TextStyleType ) INSTANCES.get( type ) );
    }
}
