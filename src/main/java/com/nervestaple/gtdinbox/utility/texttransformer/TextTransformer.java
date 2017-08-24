package com.nervestaple.gtdinbox.utility.texttransformer;

/**
 * Provides an interface that all text transformer's must implement.
 */
public interface TextTransformer {

    /**
     * Returns plain text ready for display, probably just more plain text.
     *
     * @param textPlain Text to be transformed
     * @return TransformedText
     */
    String transformTextForDisplay( String textPlain, OutputType outputType );
}
