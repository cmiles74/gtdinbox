package com.nervestaple.gtdinbox.utility.texttransformer;

import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import org.apache.log4j.Logger;

/**
 * Provides an object for transforming text.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class UtilityTextTransformer {

    /**
     * Logger instance.
     */
    private static Logger logger = Logger.getLogger(
            "com.nervestaple.gtdinbox.utility.texttransformer.UtilityTextTransformer" );

    /**
     * Plain text transformer.
     */
    public static TextTransformer PLAIN_TEXT_TRANSFORMER;

    /**
     * Markdown text transformer.
     */
    public static TextTransformer MARKDOWN_TEXT_TRANSFORMER;

    /**
     * CSS STYLESHEET.
     */
    public static String STYLESHEET;

    static {

        PLAIN_TEXT_TRANSFORMER = new PlainTextTransformer();
        MARKDOWN_TEXT_TRANSFORMER = new MarkdownTextTransformer();

        STYLESHEET = "<style>\n" +
                "body { font-family: serif; font-size: 14pt; }\n" +
                "p { margin-top: 0px; padding-top: 0px; margin-bottom: 3px; padding-bottom: 3px; }\n" +
                "div.title { font-size: 16pt; font-weight: bold;}\n" +
                "div.description {}\n" +
                "div.action-item {}\n" +
                "div.action-item-completed { text-decoration: line-through; }\n" +
                "div.subtitle { font-style: italic; color: rgb( 125, 125, 125 ); font-size: 12pt; " +
                "padding-bottom: 6pt}\n" +
                "</style>\n";
    }

    /**
     * Transforms the text into a format for display (usually plain text or HMTL).
     *
     * @param text          Text to transform
     * @param textStyleType The text's type
     * @return Transformed text
     */
    public static String transformTextForDisplay( String text, TextStyleType textStyleType, OutputType outputType ) {

        String textOut = null;
        if( textStyleType == TextStyleType.PLAIN_TEXT ) {

            textOut = PLAIN_TEXT_TRANSFORMER.transformTextForDisplay( text, outputType );
        } else if( textStyleType == TextStyleType.MARKDOWN_TEXT ) {

            textOut = MARKDOWN_TEXT_TRANSFORMER.transformTextForDisplay( text, outputType );
        } else {

            // fallback to plain text
            textOut = MARKDOWN_TEXT_TRANSFORMER.transformTextForDisplay( text, outputType );
        }

        // return the text, it's all we have
        return ( textOut );
    }
}
