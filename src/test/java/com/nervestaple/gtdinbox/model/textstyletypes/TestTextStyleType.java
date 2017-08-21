package com.nervestaple.gtdinbox.model.textstyletypes;

import junit.framework.TestCase;

/**
 * Provides a test suite for the TextStyleType object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestTextStyleType extends TestCase {

    public void testGetPlainTextType() {

        assertNotNull( TextStyleType.PLAIN_TEXT );
    }

    public void testPlainTextTypeEquality() {

        TextStyleType plain = TextStyleType.PLAIN_TEXT;

        TextStyleType plain2 = TextStyleType.getInstance( "plain-text" );

        assertTrue( plain == plain2 );
    }

    public void testReadResolve() {

        TextStyleType plain = TextStyleType.PLAIN_TEXT;

        TextStyleType plain2 = TextStyleType.getInstance( "plain-text" );

        assertTrue( plain == plain2.readResolve() );
    }
}