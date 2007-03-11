package com.nervestaple.gtdinbox.model.reference.category;

import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.SimpleReferenceItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import junit.framework.TestCase;

import java.awt.*;

/**
 * Provides a test suite for the Category object.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class TestCategory extends TestCase {

    public void testConstructor() {

        Category category = new Category();

        assertNotNull( category );
    }

    public void testId() {

        Category category = new Category();

        category.setId( Long.valueOf( 10 ) );

        assertEquals( category.getId(), Long.valueOf( 10 ) );
    }

    public void testName() {

        Category category = new Category();

        category.setName( "Name" );

        assertEquals( category.getName(), "Name" );
    }

    public void setDescription() {

        Category category = new Category();

        category.setDescription( "Description" );

        assertEquals( category.getDescription(), "Description" );
    }

    public void setDescriptionTextStyleType() {

        Category category = new Category();

        category.setTextStyleType( TextStyleType.PLAIN_TEXT );

        assertEquals( category.getTextStyleType(), TextStyleType.PLAIN_TEXT );
    }

    public void setRed() {

        Category category = new Category();

        category.setRed( Integer.valueOf( 255 ) );

        assertEquals( category.getRed(), Integer.valueOf( 255 ) );
    }

    public void setGreen() {

        Category category = new Category();

        category.setGreen( Integer.valueOf( 255 ) );

        assertEquals( category.getGreen(), Integer.valueOf( 255 ) );
    }

    public void setBlue() {

        Category category = new Category();

        category.setBlue( Integer.valueOf( 255 ) );

        assertEquals( category.getBlue(), Integer.valueOf( 255 ) );
    }

    public void setColor() {

        Category category = new Category();

        category.setColor( Color.WHITE );

        assertEquals( category.getColor(), Color.WHITE );
    }

    public void setColorComponents() {

        Category category = new Category();

        category.setColor( Color.WHITE );

        assertTrue( category.getRed().equals( Integer.valueOf( 255 ) )
                && category.getGreen().equals( Integer.valueOf( 255 ) )
                && category.getBlue().equals( Integer.valueOf( 255 ) ) );
    }

    public void testDeleted() {

        ActionItem actionItem = new ActionItem();

        actionItem.setDeleted( Boolean.valueOf( true ) );

        assertTrue( actionItem.getDeleted().booleanValue() );
    }

    public void testAddReferenceItem() {

        Category Category = new Category();

        ReferenceItem ReferenceItem = new SimpleReferenceItem();

        Category.addReferenceItem( ReferenceItem );

        assertTrue( Category.getReferenceItems().contains( ReferenceItem ) );
    }

    public void testAddReferenceItemInverse() {

        Category Category = new Category();

        ReferenceItem ReferenceItem = new SimpleReferenceItem();

        Category.addReferenceItem( ReferenceItem );

        assertTrue( ReferenceItem.getCategory().equals( Category ) );
    }

    public void testRemoveReferenceItem() {

        Category Category = new Category();

        ReferenceItem ReferenceItem = new SimpleReferenceItem();

        Category.addReferenceItem( ReferenceItem );

        Category.removeReferenceItem( ReferenceItem );

        assertFalse( Category.getReferenceItems().contains( ReferenceItem ) );
    }

    public void testRemoveReferenceItemInverse() {

        Category Category = new Category();

        ReferenceItem ReferenceItem = new SimpleReferenceItem();

        Category.addReferenceItem( ReferenceItem );

        Category.removeReferenceItem( ReferenceItem );

        assertTrue( ReferenceItem.getCategory() == null );
    }

    public void testEquals() {

        Category category1 = new Category();
        category1.setId( Long.valueOf( 10 ) );
        category1.setName( "Name" );
        category1.setColor( Color.WHITE );

        Category category2 = new Category();
        category2.setId( Long.valueOf( 10 ) );
        category2.setName( "Name" );
        category2.setColor( Color.WHITE );

        assertEquals( category1, category2 );
    }

    public void testNotEquals() {

        Category category1 = new Category();
        category1.setId( Long.valueOf( 10 ) );
        category1.setName( "Name" );
        category1.setColor( Color.WHITE );

        Category category2 = new Category();
        category2.setId( Long.valueOf( 15 ) );
        category2.setName( "Name" );
        category2.setColor( Color.BLUE );

        assertFalse( category1.equals( category2 ) );
    }

    public void testToString() {

        Category category1 = new Category();
        category1.setId( Long.valueOf( 10 ) );
        category1.setName( "Name" );
        category1.setColor( Color.WHITE );

        Category category2 = new Category();
        category2.setId( Long.valueOf( 10 ) );
        category2.setName( "Name" );
        category2.setColor( Color.WHITE );

        assertEquals( category1.toString(), category2.toString() );
    }

    public void testHashCode() {

        Category category1 = new Category();
        category1.setId( Long.valueOf( 10 ) );
        category1.setName( "Name" );
        category1.setColor( Color.WHITE );

        Category category2 = new Category();
        category2.setId( Long.valueOf( 10 ) );
        category2.setName( "Name" );
        category2.setColor( Color.WHITE );

        assertEquals( category1.hashCode(), category2.hashCode() );
    }
}
