package com.nervestaple.gtdinbox.utility.texttransformer;

import com.petebevin.markdown.MarkdownProcessor;

/**
 * Provides a text transformer for converting Markdown.
 */
public class MarkdownTextTransformer implements TextTransformer {

    /** Markdown text processor. */
    MarkdownProcessor markdownProcessor;

    /** Creates a new MarkdownTextTransformer. */
    public MarkdownTextTransformer() {

        // get a new markdown processor
        markdownProcessor = new MarkdownProcessor();
    }

    /**
     * Returns plain text ready for display, probably just more plain text.
     *
     * @param textPlain Text to be transformed
     * @return TransformedText
     */
    public String transformTextForDisplay( String textPlain, OutputType outputType ) {

        StringBuffer buffer = new StringBuffer();

        // add a span tag for the output type.
        if( outputType != null ) {
            buffer.append( "<div class=\"" + outputType.getType() + "\">" );
        }

        buffer.append( markdownProcessor.markdown( textPlain ) );

        if( outputType != null ) {
            buffer.append( "</div>" );
        }

        return ( buffer.toString() );
    }
}
