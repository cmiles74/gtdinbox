package com.nervestaple.gtdinbox.model.item.referenceitem;

import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;

import java.util.Date;

/**
 * Provides an object to model a simple reference item. This reference item contains  plain or styled text.
 */
public class SimpleReferenceItem extends ReferenceItem {

    /**
     * Content
     */
    private String content;

    /**
     * Content text style type.
     */
    private TextStyleType contentTextStyleType;

    /**
     * Creates a new SimpleReferenceItem.
     */
    public SimpleReferenceItem() {

        contentTextStyleType = TextStyleType.MARKDOWN_TEXT;
    }

    // accessor and mutator methods

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
        setLastModifiedDate(new Date());
    }

    public TextStyleType getContentTextStyleType() {
        return contentTextStyleType;
    }

    public void setContentTextStyleType(final TextStyleType contentTextStyleType) {
        this.contentTextStyleType = contentTextStyleType;
    }

    // other required methods

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        SimpleReferenceItem that = (SimpleReferenceItem) o;

        if (contentTextStyleType != null ? !contentTextStyleType.equals(that.contentTextStyleType)
                : that.contentTextStyleType != null) {
            return false;
        }
        return !(content != null ? !content.equals(that.content) : that.content != null);

    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (contentTextStyleType != null ? contentTextStyleType.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "SimpleReferenceItem{" +
                "referenceItem='" + super.toString() + "', " +
                "content='" + content + '\'' +
                ", contentTextStyleType=" + contentTextStyleType +
                '}';
    }
}
