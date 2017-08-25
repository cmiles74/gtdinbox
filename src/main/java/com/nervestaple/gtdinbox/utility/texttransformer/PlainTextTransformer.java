package com.nervestaple.gtdinbox.utility.texttransformer;

/**
 * Provides an object for transforming plain text into HTML for display.
 */
public class PlainTextTransformer implements TextTransformer {

    /**
     * Creates a new PlainTextTransformer.
     */
    public PlainTextTransformer() {

    }

    /**
     * Returns HTML text ready for display.
     *
     * @param textPlain Text to be transformed
     * @return transformed HTML
     */
    public String transformTextForDisplay(String textPlain, OutputType outputType) {

        // @todo: convert newlines to HTML line breaks

        StringBuffer buffer = new StringBuffer();

        // add a span tag for the output type.
        if (outputType != null) {
            buffer.append("<div class=\"" + outputType.getType() + "\">");
        }

        buffer.append(textPlain);

        if (outputType != null) {
            buffer.append("</div>");
        }

        return (buffer.toString());
    }
}
