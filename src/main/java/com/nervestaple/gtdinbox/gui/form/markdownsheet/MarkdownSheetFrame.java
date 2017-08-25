package com.nervestaple.gtdinbox.gui.form.markdownsheet;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a frame that displays a Markdown cheat sheet.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class MarkdownSheetFrame extends JFrame {

    /**
     * Application icon small.
     */
    private final ImageIcon ICON_APPLICATION_SMALL;

    // gui form objects
    private MarkdownSheetPanel panelMain;

    public MarkdownSheetFrame() {

        super("Markdown Cheat Sheet");

        ICON_APPLICATION_SMALL = new ImageIcon(getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/images/help-16.png"));

        setIconImage(ICON_APPLICATION_SMALL.getImage());

        panelMain = new MarkdownSheetPanel();

        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(panelMain);

        setSize(735, 530);
    }
}
