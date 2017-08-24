package com.nervestaple.gtdinbox.model.tag;

import com.nervestaple.gtdinbox.index.IndexListener;
import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import org.apache.commons.lang.Validate;
import org.hibernate.tool.schema.TargetType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides an object to model a tag. In this application, a tag is a short string (all one word) that is used for
 * quick, on-the-fly grouping. This is very similar to the tags used by tagging services like Del.icio.us.
 */
@Entity
@EntityListeners({IndexListener.class})
public class Tag implements Serializable, Indexable, Trashable {

    /**
     * Unique id.
     */
    @Id
    @SequenceGenerator(name = "TagItemSequence")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "TagItemSequence")
    private Long id;

    /**
     * Name of this tag.
     */
    private String name;

    /**
     * Indicates that this item has been deleted.
     */
    private Boolean deleted;

    /**
     * Reference items associated with this tag
     */
    @OneToMany(targetEntity = ReferenceItem.class)
    @JoinTable
    private Set referenceItems = new HashSet();

    /**
     * Action items associated with this tag
     */
    @OneToMany(targetEntity = ActionItem.class)
    @JoinTable
    private Set actionItems = new HashSet();

    /**
     * Creates a new tag.
     */
    public Tag() {

        referenceItems = new HashSet();
        actionItems = new HashSet();
        deleted = Boolean.valueOf( false );
    }

    // item methods

    public Object getParent() {
        return ( null );
    }

    public void prepareForDeletion() {

        // do nothing
    }

    // accessor and mutator methods

    public Long getId() {
        return id;
    }

    public void setId( final Long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( final String name ) {
        this.name = name;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted( final Boolean deleted ) {
        this.deleted = deleted;
    }

    public Set getReferenceItems() {
        return referenceItems;
    }

    public void setReferenceItems( final Set referenceItems ) {
        this.referenceItems = referenceItems;
    }

    public Set getActionItems() {
        return actionItems;
    }

    public void setActionItems( final Set actionItems ) {
        this.actionItems = actionItems;
    }

    // collection methods

    public void addReferenceItem( ReferenceItem referenceItem ) {

        Validate.notNull( referenceItem );

        if( !referenceItems.contains( referenceItem ) ) {

            referenceItems.add( referenceItem );

            referenceItem.getTags().add( this );
        }
    }

    public void removeReferenceItem( ReferenceItem referenceItem ) {

        Validate.notNull( referenceItem );

        if( referenceItems.contains( referenceItem ) ) {

            referenceItems.remove( referenceItem );
            referenceItem.removeTag( this );
        }
    }

    public void addActionItem( ActionItem actionItem ) {

        Validate.notNull( actionItem );

        if( !actionItems.contains( actionItem ) ) {

            actionItems.add( actionItem );
            actionItem.getTags().add( this );
        }
    }

    public void removeActionItem( ActionItem actionItem ) {

        Validate.notNull( actionItem );

        if( actionItems.contains( actionItem ) ) {

            actionItems.remove( actionItem );
            actionItem.removeTag( this );
        }
    }

    // other required methods

    public boolean equals( final Object o ) {
        if( this == o ) {
            return true;
        }
        if( o == null || getClass() != o.getClass() ) {
            return false;
        }

        Tag tag = ( Tag ) o;

        if( deleted != null ? !deleted.equals( tag.deleted ) : tag.deleted != null ) {
            return false;
        }
        if( id != null ? !id.equals( tag.id ) : tag.id != null ) {
            return false;
        }
        if( name != null ? !name.equals( tag.name ) : tag.name != null ) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = ( id != null ? id.hashCode() : 0 );
        result = 31 * result + ( name != null ? name.hashCode() : 0 );
        result = 31 * result + ( deleted != null ? deleted.hashCode() : 0 );
        ;
        return result;
    }

    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deleted=" + deleted +
                '}';
    }
}
