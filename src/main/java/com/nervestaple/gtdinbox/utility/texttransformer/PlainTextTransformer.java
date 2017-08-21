package com.nervestaple.gtdinbox.utility.texttransformer;

/**
 * Provides an object for transforming plain text into a format for display.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class PlainTextTransformer implements TextTransformer {

    /** Creates a new PlainTextTranformer. */
    public PlainTextTransformer() {

    }

    /**
     * Returns plain text ready for display, probably just more plain text.
     *
     * @param textPlain Text to be transformed
     * @return TransformedText
     */
    public String transformTextForDisplay( String textPlain, OutputType outputType ) {

        // @todo: convert newlines to HTML line breaks

        StringBuffer buffer = new StringBuffer();

        // add a span tag for the output type.
        if( outputType != null ) {
            buffer.append( "<div class=\"" + outputType.getType() + "\">" );
        }

        buffer.append( textPlain );

        if( outputType != null ) {
            buffer.append( "</div>" );
        }

        return ( buffer.toString() );
    }
}
