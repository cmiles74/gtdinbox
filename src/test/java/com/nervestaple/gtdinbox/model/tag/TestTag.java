package com.nervestaple.gtdinbox.model.tag;

import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.SimpleReferenceItem;
import junit.framework.TestCase;

/**
 * Provides a test suite for the Tag object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestTag extends TestCase {

    public void testConstructor() {

        Tag tag = new Tag();

        assertNotNull( tag );
    }

    public void testId() {

        Tag tag = new Tag();

        tag.setId( Long.valueOf( 10 ) );

        assertEquals( tag.getId(), Long.valueOf( 10 ) );
    }

    public void testName() {

        Tag tag = new Tag();

        tag.setName( "Name" );

        assertEquals( tag.getName(), "Name" );
    }

    public void testDelted() {

        Tag tag = new Tag();

        tag.setDeleted( Boolean.valueOf( true ) );

        assertTrue( tag.getDeleted().booleanValue() );
    }

    public void testAddReferenceItem() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Tag tag = new Tag();

        tag.addReferenceItem( referenceItem );

        assertTrue( tag.getReferenceItems().contains( referenceItem ) );
    }

    public void testAddReferenceItemInverse() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Tag tag = new Tag();

        tag.addReferenceItem( referenceItem );

        assertTrue( referenceItem.getTags().contains( tag ) );
    }

    public void testRemoveAddReferenceItem() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Tag tag = new Tag();

        tag.addReferenceItem( referenceItem );
        tag.removeReferenceItem( referenceItem );

        assertFalse( tag.getReferenceItems().contains( referenceItem ) );
    }

    public void testRemoveReferenceItemInverse() {

        ReferenceItem referenceItem = new SimpleReferenceItem();

        Tag tag = new Tag();

        tag.addReferenceItem( referenceItem );
        tag.removeReferenceItem( referenceItem );

        assertFalse( referenceItem.getTags().contains( tag ) );
    }

    public void testAddActionItem() {

        ActionItem ActionItem = new ActionItem();

        Tag tag = new Tag();

        tag.addActionItem( ActionItem );

        assertTrue( tag.getActionItems().contains( ActionItem ) );
    }

    public void testAddActionItemInverse() {

        ActionItem ActionItem = new ActionItem();

        Tag tag = new Tag();

        tag.addActionItem( ActionItem );

        assertTrue( ActionItem.getTags().contains( tag ) );
    }

    public void testRemoveAddActionItem() {

        ActionItem ActionItem = new ActionItem();

        Tag tag = new Tag();

        tag.addActionItem( ActionItem );
        tag.removeActionItem( ActionItem );

        assertFalse( tag.getActionItems().contains( ActionItem ) );
    }

    public void testRemoveActionItemInverse() {

        ActionItem ActionItem = new ActionItem();

        Tag tag = new Tag();

        tag.addActionItem( ActionItem );
        tag.removeActionItem( ActionItem );

        assertFalse( ActionItem.getTags().contains( tag ) );
    }

    public void testEquals() {

        Tag tag = new Tag();
        tag.setId( Long.valueOf( 10 ) );
        tag.setName( "Name" );

        Tag tag2 = new Tag();
        tag2.setId( Long.valueOf( 10 ) );
        tag2.setName( "Name" );

        assertEquals( tag, tag2 );
    }

    public void testNotEquals() {

        Tag tag = new Tag();
        tag.setId( Long.valueOf( 10 ) );
        tag.setName( "Name" );

        Tag tag2 = new Tag();
        tag2.setId( Long.valueOf( 15 ) );
        tag2.setName( "Hokey" );

        assertFalse( tag.equals( tag2 ) );
    }

    public void testToString() {

        Tag tag = new Tag();
        tag.setId( Long.valueOf( 10 ) );
        tag.setName( "Name" );

        Tag tag2 = new Tag();
        tag2.setId( Long.valueOf( 10 ) );
        tag2.setName( "Name" );

        assertEquals( tag.toString(), tag2.toString() );
    }

    public void testHashCode() {

        Tag tag = new Tag();
        tag.setId( Long.valueOf( 10 ) );
        tag.setName( "Name" );

        Tag tag2 = new Tag();
        tag2.setId( Long.valueOf( 10 ) );
        tag2.setName( "Name" );

        assertEquals( tag.hashCode(), tag2.hashCode() );
    }
}
