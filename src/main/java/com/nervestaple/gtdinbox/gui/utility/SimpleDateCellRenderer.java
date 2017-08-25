package com.nervestaple.gtdinbox.gui.utility;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * Provides a cell renderer for date cells.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class SimpleDateCellRenderer extends DefaultTableCellRenderer {

    /**
     * Format for the date cells.
     */
    private DateFormat dateFormat;

    /**
     * Creates a new SimpleDateCellRenderer.
     *
     * @param dateFormat format for the date cells
     */
    public SimpleDateCellRenderer(DateFormat dateFormat) {

        super();

        this.dateFormat = dateFormat;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                   int rowIndex, int vColIndex) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, rowIndex, vColIndex);

        if (value instanceof Date) {

            Date date = (Date) value;

            if (date.getTime() != 0) {
                setText(dateFormat.format((Date) value));
            } else {
                setText("");
            }
        }

        return this;
    }
}
