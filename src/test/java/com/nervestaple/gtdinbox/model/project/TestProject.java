package com.nervestaple.gtdinbox.model.project;

import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * Provides a test suite for the Project object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestProject extends TestCase {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(this.getClass());

    public void testConstructor() {

        Project project = new Project();

        assertNotNull(project);
    }

    public void testId() {

        Project project = new Project();

        project.setId(Long.valueOf(10));

        assertEquals(project.getId(), Long.valueOf(10));
    }

    public void testDescription() {

        Project project = new Project();

        project.setDescription("Description");

        assertEquals(project.getDescription(), "Description");
    }

    public void testDescriptionTextStyleType() {

        Project project = new Project();

        project.setTextStyleType(TextStyleType.PLAIN_TEXT);

        assertEquals(project.getTextStyleType(), TextStyleType.PLAIN_TEXT);
    }

    public void testDeleted() {

        ActionItem actionItem = new ActionItem();

        actionItem.setDeleted(Boolean.valueOf(true));

        assertTrue(actionItem.getDeleted().booleanValue());
    }

    public void testAddActionItem() {

        Project project = new Project();

        ActionItem actionItem = new ActionItem();

        project.addActionItem(actionItem);

        assertTrue(project.getActionItems().contains(actionItem));
    }

    public void testAddActionItemInverse() {

        Project project = new Project();

        ActionItem actionItem = new ActionItem();

        project.addActionItem(actionItem);

        assertTrue(actionItem.getProject().equals(project));
    }

    public void testRemoveActionItem() {

        Project project = new Project();

        ActionItem actionItem = new ActionItem();

        project.addActionItem(actionItem);

        project.removeActionItem(actionItem);

        assertFalse(project.getActionItems().contains(actionItem));
    }

    public void testRemoveActionItemInverse() {

        Project project = new Project();

        ActionItem actionItem = new ActionItem();

        project.addActionItem(actionItem);

        project.removeActionItem(actionItem);

        assertTrue(actionItem.getProject() == null);
    }

    public void testEquals() {

        Project project = new Project();

        Date now = new Date();
        project.setCreatedDate(now);
        project.setDescription("description");
        project.setTextStyleType(TextStyleType.PLAIN_TEXT);
        project.setId(Long.valueOf(10));
        project.setName("Name");

        Project project2 = new Project();
        project2.setCreatedDate(now);
        project2.setDescription("description");
        project2.setTextStyleType(TextStyleType.PLAIN_TEXT);
        project2.setId(Long.valueOf(10));
        project2.setName("Name");

        assertEquals(project, project2);
    }

    public void testNotEquals() {

        Project project = new Project();

        Date now = new Date();
        project.setCreatedDate(now);
        project.setDescription("description");
        project.setTextStyleType(TextStyleType.PLAIN_TEXT);
        project.setId(Long.valueOf(10));
        project.setName("Name");

        Project project2 = new Project();
        project2.setCreatedDate(new Date());
        project2.setDescription("description");
        project2.setTextStyleType(TextStyleType.PLAIN_TEXT);
        project2.setId(Long.valueOf(15));
        project2.setName("Name");

        assertFalse(project.equals(project2));
    }

    public void testToString() {

        Project project = new Project();

        Date now = new Date();
        project.setCreatedDate(now);
        project.setDescription("description");
        project.setTextStyleType(TextStyleType.PLAIN_TEXT);
        project.setId(Long.valueOf(10));
        project.setName("Name");

        Project project2 = new Project();
        project2.setCreatedDate(now);
        project2.setDescription("description");
        project2.setTextStyleType(TextStyleType.PLAIN_TEXT);
        project2.setId(Long.valueOf(10));
        project2.setName("Name");

        assertEquals(project.toString(), project2.toString());
    }

    public void testHashCode() {

        Project project = new Project();

        Date now = new Date();
        project.setCreatedDate(now);
        project.setDescription("description");
        project.setTextStyleType(TextStyleType.PLAIN_TEXT);
        project.setId(Long.valueOf(10));
        project.setName("Name");

        Project project2 = new Project();
        project2.setCreatedDate(now);
        project2.setDescription("description");
        project2.setTextStyleType(TextStyleType.PLAIN_TEXT);
        project2.setId(Long.valueOf(10));
        project2.setName("Name");

        assertEquals(project.hashCode(), project2.hashCode());
    }
}
