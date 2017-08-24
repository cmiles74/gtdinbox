package com.nervestaple.gtdinbox.model.item.referenceitem;

import com.nervestaple.gtdinbox.index.IndexListener;
import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.item.Item;
import com.nervestaple.gtdinbox.model.reference.category.Category;
import com.nervestaple.gtdinbox.model.tag.Tag;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides an object to model a reference item. In this application, a reference item is information that is stored and
 * filed away. This class is abstract, it will be extended to implement specific types of reference items.
 */
@Entity
@EntityListeners({IndexListener.class})
public abstract class ReferenceItem implements Serializable, Indexable, Trashable, Item {

    /**
     * Unique id.
     */
    @Id
    @SequenceGenerator(name = "ReferenceItemSequence")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "ReferenceItemSequence")
    private Long id;

    /**
     * Name of this ReferenceItem.
     */
    private String name;

    /**
     * A brief description of the ReferenceItem.
     */
    private String description;

    /**
     * The style of text used for the description.
     */
    private TextStyleType DescriptionTextStyleType;

    /**
     * Date the item was created.
     */
    private Date createdDate;

    /**
     * Date the item was most recently modified.
     */
    private Date lastModifiedDate;

    /**
     * Indicates the item has been deleted.
     */
    private Boolean deleted;

    /**
     * This reference item's category
     */
    private Category category;

    /**
     * This reference item's tags.
     */
    @OneToMany(targetEntity = Tag.class)
    @JoinTable
    private Set tags;

    /**
     * Creates a new reference item.
     */
    public ReferenceItem() {

        tags = new HashSet();
        createdDate = new Date();
        deleted = Boolean.valueOf( false );
    }

    // item methods

    public Category getParent() {

        return ( category );
    }

    public void prepareForDeletion() {

        if( category != null ) {
            category.removeReferenceItem( this );
        }
    }

    // accessor and mutator methods

    /**
     * @return unique id
     * @hibernate.id column="referenceItemId" unsaved-value="null" generator-class="native"
     */
    public Long getId() {
        return id;
    }

    public void setId( final Long id ) {
        this.id = id;
    }

    /**
     * @return name of the reference item
     * @hibernate.property
     */
    public String getName() {
        return name;
    }

    public void setName( final String name ) {
        this.name = name;
    }

    /**
     * @return description of the reference item
     * @hibernate.property
     * @hibernate.column name="description" length="32672"
     */
    public String getDescription() {
        return description;
    }

    public void setDescription( final String description ) {
        this.description = description;
        lastModifiedDate = new Date();
    }

    /**
     * @return text style type of the description
     * @hibernate.property type="com.nervestaple.gtdinbox.model.textstyletypes.TextStyleTypeUserType"
     * @hibernate.column name="descriptionTextStyleType"
     */
    public TextStyleType getDescriptionTextStyleType() {
        return DescriptionTextStyleType;
    }

    public void setDescriptionTextStyleType( final TextStyleType descriptionTextStyleType ) {
        this.DescriptionTextStyleType = descriptionTextStyleType;
    }

    /**
     * @return date the item was created
     * @hibernate.property
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate( final Date createdDate ) {
        this.createdDate = createdDate;
    }

    /**
     * @return date the item was last updated
     * @hibernate.property
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate( final Date lastModifiedDate ) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * @return true if the item has been deleted
     * @hibernate.property
     */
    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted( final Boolean deleted ) {
        this.deleted = deleted;
    }

    /**
     * @return category for this item
     * @hibernate.many-to-one column="categoryId"
     */
    public Category getCategory() {
        return category;
    }

    public void setCategory( final Category category ) {

        if( category != null && this.category != null && category.equals( this.category ) ) {

            return;
        }

        Category categoryOld = this.category;

        this.category = category;

        /*if( category != null && !category.getReferenceItems().contains( this ) ) {
            category.addReferenceItem( this );
        }*/

        if( categoryOld != null ) {

            categoryOld.removeReferenceItem( this );
        }
    }

    /**
     * @return collection of tags for this action
     * @hibernate.set table="referenceItemTags" inverse="true" lazy="true" cascade="save-update"
     * @hibernate.collection-key column="tagId"
     * @hibernate.collection-many-to-many class="com.nervestaple.gtdinbox.model.tag.Tag"
     */
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
            tag.addReferenceItem( this );
        }
    }

    public void removeTag( final Tag tag ) {

        tags.remove( tag );
        tag.removeReferenceItem( this );
    }

    // other required methods

    public boolean equals( final Object o ) {
        if( this == o ) {
            return true;
        }
        if( o == null || getClass() != o.getClass() ) {
            return false;
        }

        ReferenceItem that = ( ReferenceItem ) o;

        if( DescriptionTextStyleType != null ? !DescriptionTextStyleType.equals( that.DescriptionTextStyleType )
                : that.DescriptionTextStyleType != null ) {
            return false;
        }
        if( category != null ? !category.equals( that.category ) : that.category != null ) {
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
        if( lastModifiedDate != null ? !lastModifiedDate.equals( that.lastModifiedDate )
                : that.lastModifiedDate != null ) {
            return false;
        }
        if( name != null ? !name.equals( that.name ) : that.name != null ) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = ( id != null ? id.hashCode() : 0 );
        result = 31 * result + ( name != null ? name.hashCode() : 0 );
        result = 31 * result + ( description != null ? description.hashCode() : 0 );
        result = 31 * result + ( DescriptionTextStyleType != null ? DescriptionTextStyleType.hashCode() : 0 );
        result = 31 * result + ( createdDate != null ? createdDate.hashCode() : 0 );
        result = 31 * result + ( lastModifiedDate != null ? lastModifiedDate.hashCode() : 0 );
        result = 31 * result + ( deleted != null ? deleted.hashCode() : 0 );
        result = 31 * result + ( category != null ? category.hashCode() : 0 );
        return result;
    }


    public String toString() {
        return "ReferenceItem{" +
                "category=" + category +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", DescriptionTextStyleType=" + DescriptionTextStyleType +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", deleted=" + deleted +
                ", id=" + id +
                '}';
    }
}
