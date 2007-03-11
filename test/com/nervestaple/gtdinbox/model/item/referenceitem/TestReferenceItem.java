package com.nervestaple.gtdinbox.model.item.referenceitem;

import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.gtdinbox.model.tag.Tag;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Provides a test suite for the SimpleReferenceItem object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestReferenceItem extends TestCase {

    public void testConstructor() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        assertNotNull( referenceItem );
    }

    public void testId() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        referenceItem.setId( Long.valueOf( 10 ) );

        assertEquals( referenceItem.getId(), Long.valueOf( 10 ) );
    }

    public void testName() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        referenceItem.setName( "Name" );

        assertEquals( referenceItem.getName(), "Name" );
    }

    public void testDescription() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        referenceItem.setDescription( "Description" );

        assertEquals( referenceItem.getDescription(), "Description" );
    }

    public void testDescriptionTextStyle() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        referenceItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );

        assertEquals( referenceItem.getDescriptionTextStyleType(), TextStyleType.PLAIN_TEXT );
    }

    public void testDateCreated() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Date now = new Date();
        referenceItem.setCreatedDate( now );

        assertEquals( referenceItem.getCreatedDate(), now );
    }

    public void testDateLastModified() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Date now = new Date();
        referenceItem.setLastModifiedDate( now );

        assertEquals( referenceItem.getLastModifiedDate(), now );
    }

    public void testDeleted() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        referenceItem.setDeleted( Boolean.valueOf( true ) );

        assertTrue( referenceItem.getDeleted().booleanValue() );
    }

    public void testAddTag() {

        Tag tag = new Tag();

        ActionItem actionItem = new ActionItem();

        actionItem.addTag( tag );

        assertTrue( actionItem.getTags().contains( tag ) );
    }

    public void testAddTagInverse() {

        Tag tag = new Tag();

        ActionItem actionItem = new ActionItem();

        actionItem.addTag( tag );

        assertTrue( tag.getActionItems().contains( actionItem ) );
    }

    public void testRemoveTag() {

        Tag tag = new Tag();

        ActionItem actionItem = new ActionItem();

        actionItem.addTag( tag );
        actionItem.removeTag( tag );

        assertFalse( actionItem.getTags().contains( actionItem ) );
    }

    public void testRemoveTagInverse() {

        Tag tag = new Tag();

        ActionItem actionItem = new ActionItem();

        actionItem.addTag( tag );
        actionItem.removeTag( tag );

        assertTrue( !tag.getActionItems().contains( tag ) );
    }

    public void testCategory() {

        Category Category = new Category();

        ReferenceItem referenceItem = new SimpleReferenceItem();

        referenceItem.setCategory( Category );

        assertEquals( referenceItem.getCategory(), Category );
    }

    /*public void testCategoryInverse() {

        Category Category = new Category();

        ReferenceItem referenceItem = new SimpleReferenceItem();

        referenceItem.setCategory( Category );

        assertTrue( Category.getReferenceItems().contains( referenceItem ) );
    }*/

    public void testEquals() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Category category = new Category();
        Date now = new Date();

        referenceItem.setCategory( category );
        referenceItem.setCreatedDate( now );
        referenceItem.setDescription( "Description" );
        referenceItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        referenceItem.setId( Long.valueOf( 10 ) );
        referenceItem.setLastModifiedDate( now );
        referenceItem.setName( "Name" );

        ReferenceItem referenceItem2 = new SimpleReferenceItem();
        referenceItem2.setCategory( category );
        referenceItem2.setCreatedDate( now );
        referenceItem2.setDescription( "Description" );
        referenceItem2.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        referenceItem2.setId( Long.valueOf( 10 ) );
        referenceItem2.setLastModifiedDate( now );
        referenceItem2.setName( "Name" );

        assertEquals( referenceItem, referenceItem2 );
    }

    public void testNotEquals() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Category category = new Category();
        Date now = new Date();
        Date then = new Date();

        referenceItem.setCategory( category );
        referenceItem.setCreatedDate( now );
        referenceItem.setDescription( "Description" );
        referenceItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        referenceItem.setId( Long.valueOf( 10 ) );
        referenceItem.setLastModifiedDate( now );
        referenceItem.setName( "Name" );

        ReferenceItem referenceItem2 = new SimpleReferenceItem();
        referenceItem2.setCategory( category );
        referenceItem2.setCreatedDate( now );
        referenceItem2.setDescription( "La La LA" );
        referenceItem2.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        referenceItem2.setId( Long.valueOf( 15 ) );
        referenceItem2.setLastModifiedDate( then );
        referenceItem2.setName( "Yeeha!" );

        assertFalse( referenceItem.equals( referenceItem2 ) );
    }

    public void testToString() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Category category = new Category();
        Date now = new Date();

        referenceItem.setCategory( category );
        referenceItem.setCreatedDate( now );
        referenceItem.setDescription( "Description" );
        referenceItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        referenceItem.setId( Long.valueOf( 10 ) );
        referenceItem.setLastModifiedDate( now );
        referenceItem.setName( "Name" );

        ReferenceItem referenceItem2 = new SimpleReferenceItem();
        referenceItem2.setCategory( category );
        referenceItem2.setCreatedDate( now );
        referenceItem2.setDescription( "Description" );
        referenceItem2.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        referenceItem2.setId( Long.valueOf( 10 ) );
        referenceItem2.setLastModifiedDate( now );
        referenceItem2.setName( "Name" );

        assertEquals( referenceItem.toString(), referenceItem2.toString() );
    }

    public void testHashCode() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Category category = new Category();
        Date now = new Date();

        referenceItem.setCategory( category );
        referenceItem.setCreatedDate( now );
        referenceItem.setDescription( "Description" );
        referenceItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        referenceItem.setId( Long.valueOf( 10 ) );
        referenceItem.setLastModifiedDate( now );
        referenceItem.setName( "Name" );

        ReferenceItem referenceItem2 = new SimpleReferenceItem();
        referenceItem2.setCategory( category );
        referenceItem2.setCreatedDate( now );
        referenceItem2.setDescription( "Description" );
        referenceItem2.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        referenceItem2.setId( Long.valueOf( 10 ) );
        referenceItem2.setLastModifiedDate( now );
        referenceItem2.setName( "Name" );

        assertEquals( referenceItem.hashCode(), referenceItem2.hashCode() );
    }
}
