package com.nervestaple.gtdinbox.model.item.actionitem;

import com.nervestaple.gtdinbox.index.IndexListener;
import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.Item;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.tag.Tag;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides an object to model an action item. In this application, an action item is a task that needs to be performed.
 * This action item represents the simplest kind of item and may be extended to provide more complex behaviors in the
 * future
 */
@Entity
@EntityListeners({IndexListener.class})
public class ActionItem implements Serializable, Indexable, Trashable, Item {

    /**
     * Unique id.
     */
    @Id
    @SequenceGenerator(name = "ActionItemSequence")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "ActionItemSequence")
    private Long id;

    /**
     * A brief description of the action item.
     */
    private String description;

    /**
     * The style of text used for the description.
     */
    @Enumerated(EnumType.STRING)
    private TextStyleType descriptionTextStyleType;

    /**
     * Date the item was created.
     */
    private Date createdDate;

    /**
     * Date the item was most recently modified.
     */
    private Date lastModifiedDate;

    /**
     * Date the item was completed.
     */
    private Date completedDate;

    /**
     * Object tagged for deletion
     */
    private Boolean deleted;

    /**
     * This action item's project.
     */
    @ManyToOne
    private Project project;

    /**
     * This action item's context
     */
    @ManyToOne
    private InboxContext inboxContext;

    /**
     * The action item's tags.
     */
    @OneToMany(targetEntity = Tag.class)
    @JoinTable
    private Set tags;

    /**
     * Creates a new action item.
     */
    public ActionItem() {

        tags = new HashSet();
        createdDate = new Date();
        descriptionTextStyleType = TextStyleType.MARKDOWN_TEXT;
        deleted = Boolean.valueOf( false );
    }

    // item methods

    public String getName() {

        return ( description );
    }

    public Project getParent() {

        return ( project );
    }

    public void prepareForDeletion() {

        if( project != null ) {
            project.removeActionItem( this );
        }

        if( inboxContext != null ) {
            inboxContext.removeActionItem( this );
        }
    }

    // accessor and mutator methods

    public Long getId() {
        return id;
    }

    public void setId( final Long id ) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( final String description ) {
        this.description = description;
        lastModifiedDate = new Date();
    }

    public TextStyleType getDescriptionTextStyleType() {
        return descriptionTextStyleType;
    }

    public void setDescriptionTextStyleType( final TextStyleType descriptionTextStyleType ) {
        this.descriptionTextStyleType = descriptionTextStyleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate( final Date createdDate ) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate( final Date lastModifiedDate ) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted( final Boolean deleted ) {
        this.deleted = deleted;
    }

    public Project getProject() {
        return project;
    }

    public void setProject( final Project project ) {

        if( project != null && this.project != null && project.equals( this.project ) ) {

            return;
        }

        Project projectOld = this.project;

        this.project = project;

        /* if( project != null && !project.getActionItems().contains( this ) ) {
            project.addActionItem( this );
        }*/

        if( projectOld != null ) {

            projectOld.removeActionItem( this );
        }
    }

    public InboxContext getInboxContext() {
        return inboxContext;
    }

    public void setInboxContext( final InboxContext inboxContext ) {

        if( inboxContext != null && this.inboxContext != null && inboxContext.equals( this.getInboxContext() ) ) {

            return;
        }

        InboxContext inboxContextOld = this.inboxContext;

        this.inboxContext = inboxContext;

        /*if( inboxContext != null && !inboxContext.getActionItems().contains( this ) ) {
            inboxContext.addActionItem( this );
        }*/

        if( inboxContextOld != null ) {

            inboxContextOld.removeActionItem( this );
        }
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate( final Date completedDate ) {
        this.completedDate = completedDate;
    }

    public Set getTags() {
        return tags;
    }

    public void setTags( final Set tags ) {
        this.tags = tags;
    }

    // collection manipulation methods

    public void addTag( final Tag tag ) {

        if( !tags.contains( tag ) ) {

            tags.add( tag );
            tag.addActionItem( this );
        }
    }

    public void removeTag( final Tag tag ) {

        tags.remove( tag );
        tag.removeActionItem( this );
    }

    // other required methods

    public boolean equals( final Object o ) {
        if( this == o ) {
            return true;
        }
        if( o == null || getClass() != o.getClass() ) {
            return false;
        }

        ActionItem that = ( ActionItem ) o;

        if( descriptionTextStyleType != null ? !descriptionTextStyleType.equals( that.descriptionTextStyleType )
                : that.descriptionTextStyleType != null ) {
            return false;
        }
        if( completedDate != null ? !completedDate.equals( that.completedDate ) : that.completedDate != null ) {
            return false;
        }
        if( createdDate != null ? !createdDate.equals( that.createdDate ) : that.createdDate != null ) {
            return false;
        }
        if( deleted != null ? !deleted.equals( that.deleted ) : that.deleted != null ) {
            return false;
        }
        if( description != null ? !description.equals( that.description ) : that.description != null ) {
            return false;
        }
        if( id != null ? !id.equals( that.id ) : that.id != null ) {
            return false;
        }
        if( inboxContext != null ? !inboxContext.equals( that.inboxContext ) : that.inboxContext != null ) {
            return false;
        }
        if( lastModifiedDate != null ? !lastModifiedDate.equals( that.lastModifiedDate )
                : that.lastModifiedDate != null ) {
            return false;
        }
        if( project != null ? !project.equals( that.project ) : that.project != null ) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = ( id != null ? id.hashCode() : 0 );
        result = 31 * result + ( description != null ? description.hashCode() : 0 );
        result = 31 * result + ( descriptionTextStyleType != null ? descriptionTextStyleType.hashCode() : 0 );
        result = 31 * result + ( createdDate != null ? createdDate.hashCode() : 0 );
        result = 31 * result + ( lastModifiedDate != null ? lastModifiedDate.hashCode() : 0 );
        result = 31 * result + ( completedDate != null ? completedDate.hashCode() : 0 );
        result = 31 * result + ( deleted != null ? deleted.hashCode() : 0 );
        result = 31 * result + ( project != null ? project.hashCode() : 0 );
        result = 31 * result + ( inboxContext != null ? inboxContext.hashCode() : 0 );
        return result;
    }

    public String toString() {
        return "ActionItem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", descriptionTextStyleType=" + descriptionTextStyleType +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", completedDate=" + completedDate +
                ", deleted=" + deleted +
                ", project=" + project +
                ", inboxContext=" + inboxContext +
                '}';
    }
}
