package com.nervestaple.gtdinbox.gui.utility;

import ch.randelshofer.quaqua.QuaquaManager;
import com.nervestaple.utility.Platform;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by IntelliJ IDEA. User: miles Date: Oct 9, 2006 Time: 3:08:51 PM To change this template use File | Settings
 * | File Templates.
 */
public class UtilitiesGui {

    /**
     * Logger instance.
     */
    private static Logger logger;

    /**
     * BrowserLauncher instance for handling hyperlinks in the credits.
     */
    private static BrowserLauncher browserLauncher;

    static {

        logger = Logger.getLogger( "com.nervestaple.gtdinbox.gui.utility.UtilitiesGui" );
    }

    /**
     * Configures the Swing UI.
     */
    public static void configureSwingUI() {

        // make sure there's an option pane stylesheet
        if ( UIManager.getString( "OptionPane.css" ) != null ) {
            System.setProperty( "OptionPane.css", UIManager.getString( "OptionPane.css" ) );
        } else {

            // set OptionPane.css
            System.setProperty( "OptionPane.css",
                    "<head> " +
                            "<style type=\"text/css\">" +
                            "b { font: 13pt \"Lucida Grande\" }" +
                            "p { font: 11pt \"Lucida Grande\"; margin-top: 8px }" +
                            "</style>" +
                            "</head>" );
        }

        if ( Platform.checkMacintosh() ) {

            // set system properties
            System.setProperty( "apple.laf.useScreenMenuBar", "true" );
            System.setProperty( "com.apple.mrj.application.live-resize",
                    "true" );
            System.setProperty( "apple.awt.showGrowBox", "true" );
            System.setProperty( "apple.awt.brushMetalLook", "false" );
            System.setProperty( "Quaqua.visualMargin", "1,1,1,1" );
            System.setProperty( "Quaqua .selectionStyle", "dark" );
            System.setProperty( "Quaqua.opaque", "true" );

            // use the quaqua look and feel
            try {
                UIManager.setLookAndFeel( QuaquaManager.getLookAndFeelClassName() );
                logger.debug( "Using Quaqua look and feel: "
                        + QuaquaManager.getLookAndFeelClassName() );
            } catch ( Exception e ) {
                logger.warn( e );
            }
        } else {

            // use operating system's default look and feel
            try {
                UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
                logger.debug( "Using "
                        +
                        UIManager.getSystemLookAndFeelClassName()
                        + " look and feel" );
            } catch ( Exception e ) {

                logger.debug( e );

                // yikes! Try using the icky cross-platform look and feel.
                try {
                    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
                    logger.debug( "Using "
                            +
                            UIManager.getCrossPlatformLookAndFeelClassName()
                            + " look and feel" );
                } catch ( Exception e1 ) {
                    logger.debug( e1 );
                }
            }
        }
    }

    /**
     * Adds a ComponentListener to the frame that enforces it's minimum size.
     *
     * @param frame Frame the listener will watch
     */
    public static void addEnforceMinimumSizeComponentListener( final JFrame frame ) {

        frame.addComponentListener( getEnforceMinimumSizeComponentListener( frame ) );
    }

    /**
     * Returns the BrowserLauncher instance that can be used to listen for hyperlink clicks.
     *
     * @return BrowserLauncher instance.
     */
    public static BrowserLauncher getBrowserLauncher() {

        if ( browserLauncher == null ) {

            try {
                browserLauncher = new BrowserLauncher( null );
            } catch ( BrowserLaunchingInitializingException e ) {
                logger.warn( e );
            } catch ( UnsupportedOperatingSystemException e ) {
                logger.warn( e );
            }
        }

        return ( browserLauncher );
    }

    /**
     * Returns a HyperlinkListener for handling hyperlink clicks.
     *
     * @return HyperlinkListener to handle hyperlink clicks.
     */
    public static HyperlinkListener getHyperLinkListener() {

        HyperlinkListener listener = new HyperlinkListener() {

            public void hyperlinkUpdate( HyperlinkEvent hyperlinkEvent ) {

                if ( hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED ) {

                    if ( hyperlinkEvent.getURL() != null ) {

                        try {
                            BrowserLauncher launcher = new BrowserLauncher();
                            launcher.openURLinBrowser(hyperlinkEvent.getURL().toString());
                        } catch (BrowserLaunchingInitializingException exception) {
                            logger.warn("Couldn't initialize BrowserLauncher", exception);
                        } catch (UnsupportedOperatingSystemException exception) {
                            logger.warn("Could create a BrowserLauncher", exception);
                        }
//                        BrowserLauncherRunner runner = new BrowserLauncherRunner( getBrowserLauncher(),
//                                hyperlinkEvent.getURL().toString(), null );
//                        Thread launcherThread = new Thread( runner );
//                        launcherThread.start();
                    }
                }
            }
        };

        return ( listener );
    }

    /**
     * Adds a HyperlinkListener to the TextPane for handling hyperlink clicks.
     *
     * @param textPane TextPane with hyperlinks
     */
    public static void addHyperLinkListener( JTextPane textPane ) {

        textPane.addHyperlinkListener( getHyperLinkListener() );
    }

    /**
     * Returns a ComponentListener to the frame that enforces it's minimum size.
     *
     * @param frame Frame the listener will watch
     * @return ComponentListener that will enforce the minimum size
     */
    public static ComponentListener getEnforceMinimumSizeComponentListener( final JFrame frame ) {

        /** Frame's minimum width. */
        final int minimumWidth = ( new Double( frame.getMinimumSize().getWidth() ) ).intValue();

        /** Frame's minimum height. */
        final int minimumHeight = ( new Double( frame.getMinimumSize().getHeight() ) ).intValue();

        ComponentListener componentListener = new ComponentListener() {

            /**
             * Called when the component is resized. We use this to enforce the minimum size of the frame.
             *
             * @param event Resize event
             */
            public void componentResized( ComponentEvent event ) {

                Component component = event.getComponent();

                int width = component.getSize().width;
                if ( component.getSize().width < minimumWidth ) {
                    width = minimumWidth;
                }

                int height = component.getSize().height;
                if ( height < minimumHeight ) {
                    height = minimumHeight;
                }

                frame.setSize( width, height );
            }

            public void componentMoved( ComponentEvent componentEvent ) {

                // do nothing
            }

            public void componentShown( ComponentEvent componentEvent ) {

                // do nothing
            }

            public void componentHidden( ComponentEvent componentEvent ) {

                // do nothing
            }
        };

        return ( componentListener );
    }

    /**
     * Removes all action listeners from the button.
     *
     * @param button Button with actionListeners
     */
    public static void removeActionListeners( AbstractButton button ) {

        ActionListener[] listenerArray = button.getActionListeners();
        for ( int index = 0; index < listenerArray.length; index++ ) {

            button.removeActionListener( listenerArray[ index ] );
        }
    }
}
