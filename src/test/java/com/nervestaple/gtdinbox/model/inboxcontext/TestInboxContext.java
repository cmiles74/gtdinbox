package com.nervestaple.gtdinbox.model.inboxcontext;

import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import junit.framework.TestCase;

/**
 * Provides a test suite for the InboxContext object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestInboxContext extends TestCase {

    public void testConstructor() {

        InboxContext inboxContext = new InboxContext();

        assertNotNull(inboxContext);
    }

    public void testId() {

        InboxContext inboxContext = new InboxContext();

        inboxContext.setId(Long.valueOf(10));

        assertEquals(inboxContext.getId(), Long.valueOf(10));
    }

    public void testDescription() {

        InboxContext inboxContext = new InboxContext();

        inboxContext.setDescription("Description");

        assertEquals(inboxContext.getDescription(), "Description");
    }

    public void testDescriptionTextStyleType() {

        InboxContext inboxContext = new InboxContext();

        inboxContext.setTextStyleType(TextStyleType.PLAIN_TEXT);

        assertEquals(inboxContext.getTextStyleType(), TextStyleType.PLAIN_TEXT);
    }

    public void testDeleted() {

        ActionItem actionItem = new ActionItem();

        actionItem.setDeleted(Boolean.valueOf(true));

        assertTrue(actionItem.getDeleted().booleanValue());
    }

    public void testAddActionItem() {

        ActionItem actionItem = new ActionItem();

        InboxContext inboxContext = new InboxContext();

        inboxContext.addActionItem(actionItem);

        assertTrue(inboxContext.getActionItems().contains(actionItem));
    }

    public void testAddActionItemRelation() {

        ActionItem actionItem = new ActionItem();

        InboxContext inboxContext = new InboxContext();

        inboxContext.addActionItem(actionItem);

        assertTrue(actionItem.getInboxContext().equals(inboxContext));
    }

    public void testRemoveActionItem() {

        ActionItem actionItem = new ActionItem();

        InboxContext inboxContext = new InboxContext();

        inboxContext.addActionItem(actionItem);
        inboxContext.removeActionItem(actionItem);

        assertTrue(!inboxContext.getActionItems().contains(actionItem));
    }

    public void testRemoveActionItemRelation() {

        ActionItem actionItem = new ActionItem();

        InboxContext inboxContext = new InboxContext();

        inboxContext.addActionItem(actionItem);
        inboxContext.removeActionItem(actionItem);

        assertNull(actionItem.getInboxContext());
    }

    public void testEquals() {

        ActionItem actionItem = new ActionItem();

        InboxContext inboxContext = new InboxContext();
        inboxContext.setId(Long.valueOf(10));
        inboxContext.setName("Name");
        inboxContext.setTextStyleType(TextStyleType.PLAIN_TEXT);
        inboxContext.addActionItem(actionItem);

        InboxContext inboxContext2 = new InboxContext();
        inboxContext2.setId(Long.valueOf(10));
        inboxContext2.setName("Name");
        inboxContext2.setTextStyleType(TextStyleType.PLAIN_TEXT);
        inboxContext2.addActionItem(actionItem);

        assertEquals(inboxContext, inboxContext2);
    }

    public void testNotEquals() {

        ActionItem actionItem = new ActionItem();
        ActionItem actionItem2 = new ActionItem();

        InboxContext inboxContext = new InboxContext();
        inboxContext.setId(Long.valueOf(10));
        inboxContext.setName("Name");
        inboxContext.setTextStyleType(TextStyleType.PLAIN_TEXT);
        inboxContext.addActionItem(actionItem);

        InboxContext inboxContext2 = new InboxContext();
        inboxContext2.setId(Long.valueOf(15));
        inboxContext2.setName("Name");
        inboxContext2.setTextStyleType(TextStyleType.PLAIN_TEXT);
        inboxContext2.addActionItem(actionItem2);
    }

    public void testToString() {

        ActionItem actionItem = new ActionItem();

        InboxContext inboxContext = new InboxContext();
        inboxContext.setId(Long.valueOf(10));
        inboxContext.setName("Name");
        inboxContext.setTextStyleType(TextStyleType.PLAIN_TEXT);
        inboxContext.addActionItem(actionItem);

        InboxContext inboxContext2 = new InboxContext();
        inboxContext2.setId(Long.valueOf(10));
        inboxContext2.setName("Name");
        inboxContext2.setTextStyleType(TextStyleType.PLAIN_TEXT);
        inboxContext2.addActionItem(actionItem);

        assertEquals(inboxContext.toString(), inboxContext2.toString());
    }

    public void testHashCode() {

        ActionItem actionItem = new ActionItem();

        InboxContext inboxContext = new InboxContext();
        inboxContext.setId(Long.valueOf(10));
        inboxContext.setName("Name");
        inboxContext.setTextStyleType(TextStyleType.PLAIN_TEXT);
        inboxContext.addActionItem(actionItem);

        InboxContext inboxContext2 = new InboxContext();
        inboxContext2.setId(Long.valueOf(10));
        inboxContext2.setName("Name");
        inboxContext2.setTextStyleType(TextStyleType.PLAIN_TEXT);
        inboxContext2.addActionItem(actionItem);

        assertEquals(inboxContext.hashCode(), inboxContext2.hashCode());
    }
}
