package com.nervestaple.gtdinbox.model.reference.category;

import com.nervestaple.gtdinbox.index.IndexListener;
import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import org.apache.commons.lang.Validate;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides an object to model a category. In this application, a category is used to group references items.
 */
@Entity
@EntityListeners({IndexListener.class})
public class Category implements Serializable, Indexable, Trashable {

    /**
     * Unique id.
     */
    @Id
    @SequenceGenerator(name = "CategoryItemSequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CategoryItemSequence")
    private Long id;

    /**
     * Name of this category.
     */
    private String name;

    /**
     * A brief description of the category.
     */
    private String description;

    /**
     * The style of text used for the description.
     */
    private TextStyleType textStyleType;

    /**
     * Color red *
     */
    private Integer red;

    /**
     * Color green *
     */
    private Integer green;

    /**
     * Color blue *
     */
    private Integer blue;

    /**
     * Indicates the item has been deleted.
     */
    private Boolean deleted;

    /**
     * Reference items associated with this category.
     */
    @OneToMany(mappedBy = "category")
    private Set<ReferenceItem> referenceItems;

    /**
     * Creates a new Category.
     */
    public Category() {

        referenceItems = new HashSet<>();
        textStyleType = TextStyleType.MARKDOWN_TEXT;
        deleted = false;
    }

    // item methods
    public Object getParent() {

        // categories do not have parents
        return (null);
    }

    public void prepareForDeletion() {

        // do nothing
    }

    // accessor and mutator methods

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public TextStyleType getTextStyleType() {
        return textStyleType;
    }

    public void setTextStyleType(final TextStyleType textStyleType) {
        this.textStyleType = textStyleType;
    }

    public Integer getRed() {
        return red;
    }

    public void setRed(final Integer red) {
        this.red = red;
    }

    public Integer getGreen() {
        return green;
    }

    public void setGreen(final Integer green) {
        this.green = green;
    }

    public Integer getBlue() {
        return blue;
    }

    public void setBlue(final Integer blue) {
        this.blue = blue;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<ReferenceItem> getReferenceItems() {
        return referenceItems;
    }

    public void setReferenceItems(final Set<ReferenceItem> referenceItems) {
        this.referenceItems = referenceItems;
    }

    // collection methods

    public void addReferenceItem(ReferenceItem referenceItem) {

        Validate.notNull(referenceItem);

        referenceItem.setCategory(this);
        referenceItems.add(referenceItem);
    }

    public void removeReferenceItem(ReferenceItem referenceItem) {

        Validate.notNull(referenceItem);

        if (referenceItems.contains(referenceItem)) {

            referenceItems.remove(referenceItem);

            if (referenceItem.getCategory() != null && referenceItem.getCategory().equals(this)) {
                referenceItem.setCategory(null);
            }
        }
    }

    // other public methods

    public void setColor(final Color color) {

        this.red = color.getRed();
        this.green = color.getGreen();
        this.blue = color.getBlue();
    }

    public Color getColor() {

        Color color = null;

        if (red != null && green != null && blue != null) {

            color = new Color(red, green, blue);
        }

        return (color);
    }

    // other required methods

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Category category = (Category) o;

        if (blue != null ? !blue.equals(category.blue) : category.blue != null) {
            return false;
        }
        if (deleted != null ? !deleted.equals(category.deleted) : category.deleted != null) {
            return false;
        }
        if (description != null ? !description.equals(category.description) : category.description != null) {
            return false;
        }
        if (green != null ? !green.equals(category.green) : category.green != null) {
            return false;
        }
        if (id != null ? !id.equals(category.id) : category.id != null) {
            return false;
        }
        if (name != null ? !name.equals(category.name) : category.name != null) {
            return false;
        }
        if (red != null ? !red.equals(category.red) : category.red != null) {
            return false;
        }
        if (textStyleType != null ? !textStyleType.equals(category.textStyleType) : category.textStyleType != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (textStyleType != null ? textStyleType.hashCode() : 0);
        result = 31 * result + (red != null ? red.hashCode() : 0);
        result = 31 * result + (green != null ? green.hashCode() : 0);
        result = 31 * result + (blue != null ? blue.hashCode() : 0);
        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", textStyleType=" + textStyleType +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                ", deleted=" + deleted +
                '}';
    }
}
