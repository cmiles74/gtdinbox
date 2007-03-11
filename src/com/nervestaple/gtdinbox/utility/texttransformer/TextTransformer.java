package com.nervestaple.gtdinbox.utility.texttransformer;

/**
 * Created by IntelliJ IDEA. User: miles Date: Nov 4, 2006 Time: 4:25:54 PM To change this template use File | Settings
 * | File Templates.
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
