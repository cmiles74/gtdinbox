package com.nervestaple.gtdinbox.gui.utility;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a scroll pane that inherits the width of another component.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class WidthInheritingScrollPane extends JScrollPane {

    /**
     * Component to look to for width information.
     */
    private Component componentMatch;

    public WidthInheritingScrollPane(Component componentMatch) {

        super();

        this.componentMatch = componentMatch;
    }

    public Dimension getPreferredSize() {

        Dimension dimensionThis = super.getPreferredSize();
        Dimension dimensionMatch = componentMatch.getPreferredSize();

        int width = (new Double(dimensionMatch.getWidth())).intValue();
        int height = (new Double(dimensionThis.getHeight())).intValue();

        return (new Dimension(width, height));
    }

    public Dimension getMaximumSize() {

        Dimension dimensionThis = super.getPreferredSize();
        Dimension dimensionMatch = componentMatch.getPreferredSize();

        int width = (new Double(dimensionMatch.getWidth())).intValue();
        int height = (new Double(dimensionThis.getHeight())).intValue();

        return (new Dimension(width, height));
    }

    public Dimension getMinimumSize() {

        Dimension dimensionThis = super.getPreferredSize();
        Dimension dimensionMatch = componentMatch.getPreferredSize();

        int width = (new Double(dimensionMatch.getWidth())).intValue();
        int height = (new Double(dimensionThis.getHeight())).intValue();

        return (new Dimension(width, height));
    }
}
