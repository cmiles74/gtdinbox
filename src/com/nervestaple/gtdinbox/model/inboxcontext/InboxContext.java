package com.nervestaple.gtdinbox.model.inboxcontext;

import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides an object to model a context instance. In this application, a context groups actions that need to be carried
 * out in a particular location or work area.
 *
 * @author Christopher Miles
 * @version 1.0
 * @hibernate.class table="InboxContexts"
 * @hibernate.cache usage="read-write"
 */
public class InboxContext implements Serializable, Indexable, Trashable {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Unique id.
     */
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

    // accessor and mutator methods

    /**
     * @return unique id
     * @hibernate.id column="inboxContextId" unsaved-value="null" generator-class="native"
     */
    public Long getId() {
        return id;
    }

    public void setId( final Long id ) {
        this.id = id;
    }

    /**
     * @return name of the context
     * @hibernate.property
     */
    public String getName() {
        return name;
    }

    public void setName( final String name ) {
        this.name = name;
    }

    /**
     * @return description of the context
     * @hibernate.property
     * @hibernate.column name="description" length="32672"
     */
    public String getDescription() {
        return description;
    }

    public void setDescription( final String description ) {
        this.description = description;
    }

    /**
     * @return text style type of the description
     * @hibernate.property type="com.nervestaple.gtdinbox.model.textstyletypes.TextStyleTypeUserType"
     * @hibernate.column name="descriptionTextStyleType"
     */
    public TextStyleType getTextStyleType() {
        return textStyleType;
    }

    public void setTextStyleType( final TextStyleType textStyleType ) {
        this.textStyleType = textStyleType;
    }


    /**
     * @return true if this item has been deleted
     * @hibernate.property
     */
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted( final Boolean deleted ) {
        this.deleted = deleted;
    }

    /**
     * @return set of action items for this context
     * @hibernate.set inverse="true" lazy="true"
     * @hibernate.collection-key column="inboxContextId"
     * @hibernate.collection-one-to-many class="com.nervestaple.gtdinbox.model.item.actionitem.ActionItem"
     * @hibernate.cache usage="read-write"
     */
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
