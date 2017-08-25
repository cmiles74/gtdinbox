package com.nervestaple.gtdinbox.utility.texttransformer;

/**
 * Provides an interface that all text transformer's must implement.
 */
public interface TextTransformer {

    /**
     * Returns HTML text ready for display.
     *
     * @param textPlain Text to be transformed
     * @return transformed HTML
     */
    String transformTextForDisplay(String textPlain, OutputType outputType);
}
