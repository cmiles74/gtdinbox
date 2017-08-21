package com.nervestaple.gtdinbox.model.item.actionitem;

import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.tag.Tag;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import junit.framework.TestCase;

import java.util.Date;

/**
 * Provides a test suite for the ActionItem object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestActionItem extends TestCase {

    public void testConstructor() {

        ActionItem actionItem = new ActionItem();

        assertNotNull( actionItem );
    }

    public void testId() {

        ActionItem actionItem = new ActionItem();

        actionItem.setId( Long.valueOf( 10 ) );

        assertEquals( actionItem.getId(), Long.valueOf( 10 ) );
    }

    public void testDescription() {

        ActionItem actionItem = new ActionItem();

        actionItem.setDescription( "Description" );

        assertEquals( actionItem.getDescription(), "Description" );
    }

    public void testDescriptionTextStyleType() {

        ActionItem actionItem = new ActionItem();

        actionItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );

        assertEquals( actionItem.getDescriptionTextStyleType(), TextStyleType.PLAIN_TEXT );
    }

    public void testCreatedDate() {

        ActionItem actionItem = new ActionItem();

        Date now = new Date();

        actionItem.setCreatedDate( now );

        assertEquals( actionItem.getCreatedDate(), now );
    }

    public void testLastModifiedDate() {

        ActionItem actionItem = new ActionItem();

        Date now = new Date();

        actionItem.setLastModifiedDate( now );

        assertEquals( actionItem.getLastModifiedDate(), now );
    }

    public void testProject() {

        Project project = new Project();

        ActionItem actionItem = new ActionItem();

        actionItem.setProject( project );

        assertEquals( actionItem.getProject(), project );
    }

    /*public void testProjectInverse() {

        Project project = new Project();

        ActionItem actionItem = new ActionItem();

        actionItem.setProject( project );

        assertTrue( project.getActionItems().contains( actionItem ) );
    }*/

    public void testInboxContext() {

        InboxContext inboxContext = new InboxContext();

        ActionItem actionItem = new ActionItem();

        actionItem.setInboxContext( inboxContext );

        assertEquals( actionItem.getInboxContext(), inboxContext );
    }

    /*public void testInboxContextInverse() {

        InboxContext inboxContext = new InboxContext();

        ActionItem actionItem = new ActionItem();

        actionItem.setInboxContext( inboxContext );

        assertTrue( inboxContext.getActionItems().contains( actionItem ) );
    }*/

    public void testCompletedDate() {

        Date now = new Date();

        ActionItem actionItem = new ActionItem();

        actionItem.setCompletedDate( now );

        assertEquals( actionItem.getCompletedDate(), now );
    }

    public void testDeleted() {

        ActionItem actionItem = new ActionItem();

        actionItem.setDeleted( Boolean.valueOf( true ) );

        assertTrue( actionItem.getDeleted().booleanValue() );
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

    public void testEquals() {

        Date now = new Date();
        Project project = new Project();
        InboxContext inboxContext = new InboxContext();
        Tag tag = new Tag();

        ActionItem actionItem = new ActionItem();
        actionItem.setCompletedDate( now );
        actionItem.setCreatedDate( now );
        actionItem.setDescription( "description" );
        actionItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        actionItem.setId( Long.valueOf( 10 ) );
        actionItem.setInboxContext( inboxContext );
        actionItem.setProject( project );
        actionItem.getTags().add( tag );
        actionItem.setLastModifiedDate( now );

        ActionItem actionItem2 = new ActionItem();
        actionItem2.setCompletedDate( now );
        actionItem2.setCreatedDate( now );
        actionItem2.setDescription( "description" );
        actionItem2.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        actionItem2.setId( Long.valueOf( 10 ) );
        actionItem2.setInboxContext( inboxContext );
        actionItem2.setProject( project );
        actionItem2.getTags().add( tag );
        actionItem2.setLastModifiedDate( now );

        assertEquals( actionItem, actionItem2 );
    }

    public void testNotEquals() {

        Date now = new Date();
        Project project = new Project();
        InboxContext inboxContext = new InboxContext();
        Tag tag = new Tag();

        ActionItem actionItem = new ActionItem();
        actionItem.setCompletedDate( now );
        actionItem.setCreatedDate( now );
        actionItem.setDescription( "description" );
        actionItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        actionItem.setId( Long.valueOf( 10 ) );
        actionItem.setInboxContext( inboxContext );
        actionItem.setProject( project );
        actionItem.getTags().add( tag );

        ActionItem actionItem2 = new ActionItem();
        actionItem2.setCompletedDate( now );
        actionItem2.setCreatedDate( now );
        actionItem2.setDescription( "helluva" );
        actionItem2.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        actionItem2.setId( Long.valueOf( 12 ) );
        actionItem2.setInboxContext( inboxContext );
        actionItem2.setProject( project );
        actionItem2.getTags().add( tag );

        assertNotSame( actionItem, actionItem2 );
    }

    public void testToString() {

        Date now = new Date();
        Project project = new Project();
        InboxContext inboxContext = new InboxContext();
        Tag tag = new Tag();

        ActionItem actionItem = new ActionItem();
        actionItem.setCompletedDate( now );
        actionItem.setCreatedDate( now );
        actionItem.setDescription( "description" );
        actionItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        actionItem.setId( Long.valueOf( 10 ) );
        actionItem.setInboxContext( inboxContext );
        actionItem.setProject( project );
        actionItem.getTags().add( tag );

        ActionItem actionItem2 = new ActionItem();
        actionItem2.setCompletedDate( now );
        actionItem2.setCreatedDate( now );
        actionItem2.setDescription( "description" );
        actionItem2.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        actionItem2.setId( Long.valueOf( 10 ) );
        actionItem2.setInboxContext( inboxContext );
        actionItem2.setProject( project );
        actionItem2.getTags().add( tag );

        assertEquals( actionItem.toString(), actionItem2.toString() );
    }

    public void testHashCode() {

        Date now = new Date();
        Project project = new Project();
        InboxContext inboxContext = new InboxContext();
        Tag tag = new Tag();

        ActionItem actionItem = new ActionItem();
        actionItem.setCompletedDate( now );
        actionItem.setCreatedDate( now );
        actionItem.setDescription( "description" );
        actionItem.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        actionItem.setId( Long.valueOf( 10 ) );
        actionItem.setInboxContext( inboxContext );
        actionItem.setProject( project );
        actionItem.getTags().add( tag );

        ActionItem actionItem2 = new ActionItem();
        actionItem2.setCompletedDate( now );
        actionItem2.setCreatedDate( now );
        actionItem2.setDescription( "description" );
        actionItem2.setDescriptionTextStyleType( TextStyleType.PLAIN_TEXT );
        actionItem2.setId( Long.valueOf( 10 ) );
        actionItem2.setInboxContext( inboxContext );
        actionItem2.setProject( project );
        actionItem2.getTags().add( tag );

        assertEquals( actionItem.hashCode(), actionItem2.hashCode() );
    }
}
