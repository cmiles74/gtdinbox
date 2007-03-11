package com.nervestaple.gtdinbox.gui.form.about;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.nervestaple.gtdinbox.gui.ApplicationMenuBar;
import com.nervestaple.gtdinbox.gui.GTDInboxGUI;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.utility.swing.GuiSwing;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Provides a simple panel with the "About" information for the application.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class AboutFrame extends JFrame {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Menu bar for this window.
     */
    private ApplicationMenuBar applicationMenuBar;

    /**
     * Name of the application.
     */
    private String applicationName;

    /**
     * Version number for the application.
     */
    private String versionNumber;

    /**
     * Copyright message.
     */
    private String copyright;

    /**
     * Credits, HTML.
     */
    private String credits;

    /**
     * Application icon, preferably 64x64 pixels.
     */
    private ImageIcon ICON_APPLICATION;

    /**
     * Application icon small.
     */
    private final ImageIcon ICON_APPLICATION_SMALL;

    /**
     * Default credit text.
     */
    private final String DEFAULT_CREDITS = "<html><body>\n" +
            "<center><p>by Christopher Miles</p></center>\n" +
            "<p>An application designed to help you keep track of the things you have to get done. It has been" +
            " modeled after the methodology described by <a href=\"http://www.davidco.com/\">David Allen</a> in" +
            " his book" +
            " <a href=\"http://www.amazon.com/Getting-Things-Done-Stress-Free-Productivity/dp/0142000280/sr=8-1/" +
            "qid=1160945637/ref=pd_bbs_sr_1/104-6998584-4712705?ie=UTF8\"><i>Getting Things Done: The Art of" +
            " Stress Free Productivity</i></a>.</p>\n" +
            "</body></html>";

    // gui form objects
    private JPanel panelMain;
    private JTextPane textPaneCredits;
    private JLabel labelApplicationName;
    private JLabel labelVersion;
    private JLabel labelCopyright;
    private JLabel labelIcon;

    public AboutFrame() {

        super( "About GTD Inbox" );

        initializePanel();

        ICON_APPLICATION_SMALL = new ImageIcon( getClass().getResource(
                "/com/nervestaple/gtdinbox/gui/images/application-16.png" ) );

        setIconImage( ICON_APPLICATION_SMALL.getImage() );

        setResizable( false );
    }

    // accessor and mutator methods

    public ApplicationMenuBar getApplicationMenuBar() {
        return applicationMenuBar;
    }

    public void setApplicationMenuBar( ApplicationMenuBar applicationMenuBar ) {
        this.applicationMenuBar = applicationMenuBar;

        // get a menu bar
        applicationMenuBar = new ApplicationMenuBar( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                GTDInboxGUI.getInstance().handleMenuAction( actionEvent );
            }
        } );

        setJMenuBar( applicationMenuBar );
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName( final String applicationName ) {
        this.applicationName = applicationName;

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                labelApplicationName.setText( applicationName );
            }
        } );
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber( final String versionNumber ) {
        this.versionNumber = versionNumber;

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                labelVersion.setText( versionNumber );
            }
        } );
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright( final String copyright ) {
        this.copyright = copyright;

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                labelCopyright.setText( copyright );
            }
        } );
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits( final String credits ) {
        this.credits = credits;

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {

            }
        } );
    }

    public ImageIcon getICON_APPLICATION() {
        return ICON_APPLICATION;
    }

    public void setICON_APPLICATION( final ImageIcon ICON_APPLICATION ) {
        this.ICON_APPLICATION = ICON_APPLICATION;

        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                labelIcon.setIcon( ICON_APPLICATION );
            }
        } );
    }

    // private methods

    private void initializePanel() {

        getContentPane().setLayout( new GridLayout( 1, 1 ) );
        getContentPane().add( panelMain );
        textPaneCredits.setText( DEFAULT_CREDITS );
        pack();
        setResizable( false );
        GuiSwing.centerWindow( this );

        UtilitiesGui.addHyperLinkListener( textPaneCredits );

        setApplicationMenuBar( new ApplicationMenuBar( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                GTDInboxGUI.getInstance().handleMenuAction( actionEvent );
            }
        } ) );
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout( new GridLayoutManager( 3, 1, new Insets( 8, 0, 8, 0 ), -1, -1 ) );
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setHorizontalScrollBarPolicy( 31 );
        scrollPane1.setVerticalScrollBarPolicy( 22 );
        panelMain.add( scrollPane1, new GridConstraints( 1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension( 250, 150 ), null, 0, false ) );
        textPaneCredits = new JTextPane();
        textPaneCredits.setContentType( "text/html" );
        textPaneCredits.setEditable( false );
        textPaneCredits.setText( "<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0\">\n      \n    </p>\n  </body>\n</html>\n" );
        textPaneCredits.putClientProperty( "JEditorPane.w3cLengthUnits", Boolean.FALSE );
        textPaneCredits.putClientProperty( "JEditorPane.honorDisplayProperties", Boolean.FALSE );
        scrollPane1.setViewportView( textPaneCredits );
        final JPanel panel1 = new JPanel();
        panel1.setLayout( new GridLayoutManager( 3, 1, new Insets( 8, 5, 5, 3 ), -1, -1 ) );
        panelMain.add( panel1, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        labelApplicationName = new JLabel();
        labelApplicationName.setFont( new Font( labelApplicationName.getFont().getName(), Font.BOLD, 14 ) );
        labelApplicationName.setText( "GTD Inbox" );
        panel1.add( labelApplicationName, new GridConstraints( 1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        labelVersion = new JLabel();
        labelVersion.setFont( new Font( labelVersion.getFont().getName(), labelVersion.getFont().getStyle(), 11 ) );
        labelVersion.setText( "0.61 alpha" );
        panel1.add( labelVersion, new GridConstraints( 2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        labelIcon = new JLabel();
        labelIcon.setIcon( new ImageIcon( getClass().getResource( "/com/nervestaple/gtdinbox/gui/images/application-64.png" ) ) );
        labelIcon.setText( "" );
        panel1.add( labelIcon, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension( 64, 64 ), null, 0, false ) );
        final JPanel panel2 = new JPanel();
        panel2.setLayout( new GridLayoutManager( 1, 1, new Insets( 3, 4, 0, 5 ), -1, -1 ) );
        panelMain.add( panel2, new GridConstraints( 2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        labelCopyright = new JLabel();
        labelCopyright.setFont( new Font( labelCopyright.getFont().getName(), labelCopyright.getFont().getStyle(), 11 ) );
        labelCopyright.setText( "© Copyright 2006 Nervestaple Development" );
        panel2.add( labelCopyright, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
