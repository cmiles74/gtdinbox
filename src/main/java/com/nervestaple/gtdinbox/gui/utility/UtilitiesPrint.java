package com.nervestaple.gtdinbox.gui.utility;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * Provides utility methods for printing.
 * <p/>
 * Adapted from some code by Roy MacGrogan, he posted it on his blog: http://www.developerdotstar.com/community/node/124
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class UtilitiesPrint implements Printable {

    /**
     * Component to be printed.
     */
    private Component componentToBePrinted;

    /**
     * Prints the Component, the user will be prompted with the standard print dialog.
     *
     * @param c Component to print.
     */
    public static void printComponent(Component c) {
        new UtilitiesPrint(c).print();
    }

    /**
     * Sets the component to print.
     *
     * @param componentToBePrinted Component to print out.
     */
    public UtilitiesPrint(Component componentToBePrinted) {
        this.componentToBePrinted = componentToBePrinted;
    }

    /**
     * Starts the print process, prompts the user with the standard print dialog.
     */
    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog()) {
            try {
                System.out.println("Calling PrintJob.print()");
                printJob.print();
                System.out.println("End PrintJob.print()");
            } catch (PrinterException pe) {
                System.out.println("Error printing: " + pe);
            }
        }
    }

    /**
     * Called be the printing framework, prints the page requested
     *
     * @param g         Graphics are to print on.
     * @param pf        PageFormat of the page.
     * @param pageIndex Page to print.
     * @return Code to indicate if the page exists or not (used by the printing framework).
     */
    public int print(Graphics g, PageFormat pf, int pageIndex) {
        int response = NO_SUCH_PAGE;

        Graphics2D g2 = (Graphics2D) g;

        //  for faster printing, turn off double buffering
        disableDoubleBuffering(componentToBePrinted);

        Dimension d = componentToBePrinted.getSize(); //get size of document
        double panelWidth = d.width; //width in pixels
        double panelHeight = d.height; //height in pixels

        double pageHeight = pf.getImageableHeight(); //height of printer page
        double pageWidth = pf.getImageableWidth(); //width of printer page

        double scale = pageWidth / panelWidth;
        int totalNumPages = (int) Math.ceil(scale * panelHeight / pageHeight);

        //  make sure not print empty pages
        if (pageIndex >= totalNumPages) {
            response = NO_SUCH_PAGE;
        } else {

            //  shift Graphic to line up with beginning of print-imageable region
            g2.translate(pf.getImageableX(), pf.getImageableY());

            //  shift Graphic to line up with beginning of next page to print
            g2.translate(0f, -pageIndex * pageHeight);

            //  scale the page so the width fits...
            g2.scale(scale, scale);

            componentToBePrinted.paint(g2); //repaint the page for printing

            enableDoubleBuffering(componentToBePrinted);
            response = Printable.PAGE_EXISTS;
        }
        return response;
    }

    /**
     * Disables double buffering for the component.
     *
     * @param c Component that will have double buffering disabled.
     */
    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    /**
     * Enables double buffering for the component.
     *
     * @param c Component that will have double buffering enabled.
     */
    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
