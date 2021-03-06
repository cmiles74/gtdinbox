package com.nervestaple.gtdinbox.model.project;

import com.nervestaple.gtdinbox.index.IndexListener;
import com.nervestaple.gtdinbox.model.Indexable;
import com.nervestaple.gtdinbox.model.Trashable;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import javax.persistence.*;
import javax.swing.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides an object to model a project instance. In this application, a Project groups actions that are related to the
 * same goal.
 */
@Entity
@EntityListeners({IndexListener.class})
public class Project implements Serializable, Indexable, Trashable {

    /**
     * Unique id.
     */
    @Id
    @SequenceGenerator(name = "ProjectItemSequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProjectItemSequence")
    private Long id;

    /**
     * Name of this project.
     */
    private String name;

    /**
     * A brief description of the project.
     */
    private String description;

    /**
     * The style of text used for the description.
     */
    private TextStyleType textStyleType;

    /**
     * Date the project was first creatd.
     */
    private Date createdDate;

    /**
     * Indicates the item has been deleted
     */
    private Boolean deleted;

    /**
     * Action items associates with this project.
     */
    @OneToMany(mappedBy = "project")
    private Set<ActionItem> actionItems;

    /**
     * Creates a new project.
     */
    public Project() {

        actionItems = new HashSet<>();
        deleted = false;
        createdDate = new Date();
        textStyleType = TextStyleType.MARKDOWN_TEXT;
    }

    // item methods

    public Object getParent() {

        // projects do not have parents
        return (null);
    }

    // trashable methods

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<ActionItem> getActionItems() {
        return actionItems;
    }

    public void setActionItems(final Set<ActionItem> actionItems) {
        this.actionItems = actionItems;
    }

    // collection methods

    public void addActionItem(ActionItem actionItem) {

        Validate.notNull(actionItem);

        actionItem.setProject(this);
        actionItems.add(actionItem);
    }

    public void removeActionItem(ActionItem actionItem) {

        Validate.notNull(actionItem);

        if (actionItems.contains(actionItem)) {

            actionItems.remove(actionItem);

            if (actionItem.getProject() != null && actionItem.getProject().equals(this)) {
                actionItem.setProject(null);
            }
        }
    }

    // other required methods

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Project project = (Project) o;

        if (createdDate != null ? !createdDate.equals(project.createdDate) : project.createdDate != null) {
            return false;
        }
        if (deleted != null ? !deleted.equals(project.deleted) : project.deleted != null) {
            return false;
        }
        if (description != null ? !description.equals(project.description) : project.description != null) {
            return false;
        }
        if (id != null ? !id.equals(project.id) : project.id != null) {
            return false;
        }
        if (name != null ? !name.equals(project.name) : project.name != null) {
            return false;
        }
        if (textStyleType != null ? !textStyleType.equals(project.textStyleType) : project.textStyleType != null) {
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
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (deleted != null ? deleted.hashCode() : 0);
        return result;
    }

    public String toString() {
        return (name);
    }
}
