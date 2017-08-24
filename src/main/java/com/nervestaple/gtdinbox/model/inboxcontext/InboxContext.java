package com.nervestaple.gtdinbox.model.inboxcontext;

import com.nervestaple.gtdinbox.index.IndexListener;
import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides an object to model a context instance. In this application, a context groups actions that need to be carried
 * out in a particular location or work area.
 */
@Entity
@EntityListeners({IndexListener.class})
public class InboxContext implements Serializable, Indexable, Trashable {

    /**
     * Unique id.
     */
    @Id
    @SequenceGenerator(name = "InboxContextSequence")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "InboxContextSequence")
    private Long id;

    /**
     * Name of this context.
     */
    private String name;

    /**
     * A brief description of the context.
     */
    private String description;

    /**
     * The style of text used for the description.
     */
    private TextStyleType textStyleType;

    /**
     * Indicates the item has been deleted
     */
    private Boolean deleted;

    /**
     * Action items associated with this context.
     */
    @OneToMany(targetEntity = ActionItem.class)
    private Set actionItems = new HashSet();

    /**
     * Creates a new InboxContext.
     */
    public InboxContext() {

        actionItems = new HashSet();
        textStyleType = TextStyleType.MARKDOWN_TEXT;
        deleted = Boolean.valueOf( false );
    }

    // Item methods

    public Object getParent() {
        return ( null );
    }

    // Trashable methods

    public void prepareForDeletion() {

        // do nothing
    }


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

    public String getDescription() {
        return description;
    }

    public void setDescription( final String description ) {
        this.description = description;
    }

    public TextStyleType getTextStyleType() {
        return textStyleType;
    }

    public void setTextStyleType( final TextStyleType textStyleType ) {
        this.textStyleType = textStyleType;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted( final Boolean deleted ) {
        this.deleted = deleted;
    }

    public Set getActionItems() {
        return actionItems;
    }

    public void setActionItems( final Set actionItems ) {
        this.actionItems = actionItems;
    }

    // collection methods

    public void addActionItem( ActionItem actionItem ) {

        Validate.notNull( actionItem );

        actionItem.setInboxContext( this );
        actionItems.add( actionItem );
    }

    public void removeActionItem( ActionItem actionItem ) {

        Validate.notNull( actionItem );

        if( actionItems.contains( actionItem ) ) {

            actionItems.remove( actionItem );

            if( actionItem.getInboxContext() != null && actionItem.getInboxContext().equals( this ) ) {
                actionItem.setInboxContext( null );
            }
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

        InboxContext that = ( InboxContext ) o;

        if( deleted != null ? !deleted.equals( that.deleted ) : that.deleted != null ) {
            return false;
        }
        if( description != null ? !description.equals( that.description ) : that.description != null ) {
            return false;
        }
        if( id != null ? !id.equals( that.id ) : that.id != null ) {
            return false;
        }
        if( name != null ? !name.equals( that.name ) : that.name != null ) {
            return false;
        }
        if( textStyleType != null ? !textStyleType.equals( that.textStyleType ) : that.textStyleType != null ) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = ( id != null ? id.hashCode() : 0 );
        result = 31 * result + ( name != null ? name.hashCode() : 0 );
        result = 31 * result + ( description != null ? description.hashCode() : 0 );
        result = 31 * result + ( textStyleType != null ? textStyleType.hashCode() : 0 );
        result = 31 * result + ( deleted != null ? deleted.hashCode() : 0 );
        return result;
    }

    public String toString() {
        return ( name );
    }
}
