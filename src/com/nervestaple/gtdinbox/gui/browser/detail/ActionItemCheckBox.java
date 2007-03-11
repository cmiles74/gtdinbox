package com.nervestaple.gtdinbox.gui.browser.detail;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.gui.GTDInboxExceptionHandler;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import com.nervestaple.gtdinbox.utility.texttransformer.OutputType;
import com.nervestaple.gtdinbox.utility.texttransformer.UtilityTextTransformer;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

/**
 * Provides a fancier ActionItemCheckBox interface.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class ActionItemCheckBox extends JPanel {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * ActionItem being displayed.
     */
    private ActionItem actionItem;

    /**
     * Exception handler for this object.
     */
    private GTDInboxExceptionHandler exceptionHandler;

    /**
     * Hash table of mouse listeners and the passthrough listeners that go with them.
     */
    private Hashtable hashTableMouseListeners;

    /**
     * Selected item background color.
     */
    private Color colorSelectedItemBackground;

    /**
     * Selected item foreground color.
     */
    private Color colorSelectedItemForeground;

    /**
     * Default backgrounds.
     */
    private Color colorDefaultBackground;

    /**
     * Default foreground.
     */
    private Color colorDefaultForeground;

    /**
     * Long date format.
     */
    private DateFormat dateFormatLong;

    // gui form objects
    private JCheckBox checkBox;
    private JTextPane textPane;
    private JPanel panelMain;
    private JViewport viewport;

    /**
     * Creates a new ActionItemCheckBox.
     *
     * @param actionItem       ActionItem to display
     * @param exceptionHandler handler for this object's exceptions
     * @throws com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException
     *          Problems loading ActionItem data
     */
    public ActionItemCheckBox( ActionItem actionItem, GTDInboxExceptionHandler exceptionHandler )
            throws DataBaseManagerException {

        this.exceptionHandler = exceptionHandler;

        hashTableMouseListeners = new Hashtable();

        initializePanel();

        dateFormatLong = new SimpleDateFormat( "EEEE MMMM d, yyyy @ h:mm aa" );

        // setup colors
        colorSelectedItemBackground = ( ( Color ) UIManager.get( "TextArea.selectionBackground" ) );
        colorSelectedItemForeground = ( ( Color ) UIManager.get( "TextArea.selectionForeground" ) );
        colorDefaultBackground = panelMain.getBackground();
        colorDefaultForeground = panelMain.getForeground();

        setActionItem( actionItem );
    }

    /**
     * Adds a mouse listener to the objects.
     *
     * @param mouseListener MouseListener
     */
    public void addMouseListener( MouseListener mouseListener ) {

        MouseListener passThroughListener = createPassThroughMouseListener( mouseListener, this );

        hashTableMouseListeners.put( mouseListener, passThroughListener );

        checkBox.addMouseListener( passThroughListener );
        textPane.addMouseListener( passThroughListener );
        panelMain.addMouseListener( passThroughListener );
    }

    /**
     * Removes a mouse listener from the objects
     *
     * @param mouseListener MouseListener
     */
    public void removeMouseListeners( MouseListener mouseListener ) {

        MouseListener passThroughListener = ( MouseListener ) hashTableMouseListeners.get( mouseListener );

        hashTableMouseListeners.remove( mouseListener );

        if( passThroughListener != null ) {

            checkBox.removeMouseListener( passThroughListener );
            textPane.removeMouseListener( passThroughListener );
            panelMain.removeMouseListener( passThroughListener );
        }
    }

    public void setSelected( final Boolean isSelected ) {

        SwingUtilities.invokeLater( new Runnable() {

            public void run() {

                if( isSelected ) {

                    panelMain.setBackground( colorSelectedItemBackground );
                    panelMain.setForeground( colorSelectedItemForeground );

                    textPane.setBackground( colorSelectedItemBackground );
                    textPane.setForeground( colorSelectedItemForeground );
                } else {

                    panelMain.setBackground( colorDefaultBackground );
                    panelMain.setForeground( colorDefaultForeground );

                    textPane.setBackground( colorDefaultBackground );
                    textPane.setForeground( colorDefaultForeground );
                }
            }
        } );
    }

    // accessor and mutator methods

    public ActionItem getActionItem() {
        return actionItem;
    }

    public void setActionItem( final ActionItem actionItem ) throws DataBaseManagerException {

        this.actionItem = actionItem;

        updateDisplayedData();

        UtilitiesGui.removeActionListeners( checkBox );

        checkBox.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent actionEvent ) {

                if( checkBox.isSelected() ) {
                    updateActionItemCompleted( actionItem );
                } else {
                    updateActionItemIncompleted( actionItem );
                }
            }
        } );
    }

    // private methods

    private void updateDisplayedData() {

        SwingUtilities.invokeLater( new Runnable() {

            public void run() {

                checkBox.setSelected( actionItem.getCompletedDate() != null );

                StringBuffer bufferInfo = new StringBuffer();

                if( actionItem.getCreatedDate() != null ) {

                    bufferInfo.append( "Created " );
                    bufferInfo.append( dateFormatLong.format( actionItem.getCreatedDate() ) );
                }
                if( actionItem.getCompletedDate() != null ) {

                    if( bufferInfo.length() > 0 ) {
                        bufferInfo.append( ", " );
                    }

                    bufferInfo.append( "Closed " );
                    bufferInfo.append( dateFormatLong.format( actionItem.getCompletedDate() ) );
                }

                String textInfo = UtilityTextTransformer
                        .transformTextForDisplay( bufferInfo.toString(), TextStyleType.PLAIN_TEXT,
                                OutputType.SUBTITLE );

                String textItem = null;
                if( actionItem.getCompletedDate() != null ) {

                    textItem = UtilityTextTransformer.transformTextForDisplay(
                            actionItem.getDescription(), actionItem.getDescriptionTextStyleType(),
                            OutputType.ACTION_ITEM_COMPLETED );
                } else {

                    textItem = UtilityTextTransformer.transformTextForDisplay(
                            actionItem.getDescription(), actionItem.getDescriptionTextStyleType(),
                            OutputType.ACTION_ITEM );
                }


                StringBuffer bufferContent = new StringBuffer();

                bufferContent.append( "<html>" );
                bufferContent.append( UtilityTextTransformer.STYLESHEET );
                bufferContent.append( textItem );
                bufferContent.append( textInfo );
                bufferContent.append( "</html>" );

                textPane.setText( bufferContent.toString() );

                textPane.invalidate();
                viewport.validate();
            }
        } );
    }

    private void updateActionItemCompleted( final ActionItem actionItem ) {

        actionItem.setCompletedDate( new Date() );

        Thread thread = new Thread( new Runnable() {
            public void run() {

                try {
                    Session session = DataBaseManager.getInstance().getSession();
                    Transaction transaction = session.beginTransaction();
                    session.saveOrUpdate( actionItem );
                    transaction.commit();

                    updateDisplayedData();
                } catch( DataBaseManagerException e ) {

                    handleException( e );
                }
            }
        } );
        thread.start();
    }

    private void updateActionItemIncompleted( final ActionItem actionItem ) {

        actionItem.setCompletedDate( null );

        Thread thread = new Thread( new Runnable() {
            public void run() {

                try {
                    Session session = DataBaseManager.getInstance().getSession();
                    Transaction transaction = session.beginTransaction();
                    session.saveOrUpdate( actionItem );
                    transaction.commit();

                    updateDisplayedData();
                } catch( DataBaseManagerException e ) {

                    handleException( e );
                }
            }
        } );
        thread.start();
    }

    private void clearPanel() {

        SwingUtilities.invokeLater( new Runnable() {

            public void run() {

                checkBox.setSelected( false );
                textPane.setText( "<html></html>" );
            }
        } );
    }

    private void initializePanel() {

        textPane = new JTextPane();
        textPane.setContentType( "text/html" );
        textPane.setText( "<html></html>" );
        textPane.setEditable( false );
        viewport.setLayout( new BorderLayout() );
        viewport.setView( textPane );

        UtilitiesGui.addHyperLinkListener( textPane );

        setLayout( new GridLayout( 1, 1 ) );
        add( panelMain );
    }

    private void handleException( GTDInboxException exception ) {

        if( exceptionHandler != null ) {

            exceptionHandler.handleException( exception );
        } else {

            logger.warn( exception );
        }
    }

    /**
     * Creates a mouse listener that propogates event with this ActionItemCheckBox as the source of the event.
     *
     * @param mouseListener      MouseListener to received events
     * @param actionItemCheckBox ActionItemCheckBox to use as the source of the events
     * @return MouseListener
     */
    private MouseListener createPassThroughMouseListener( final MouseListener mouseListener,
                                                          final ActionItemCheckBox actionItemCheckBox ) {

        MouseListener mouseListenerThis = new MouseListener() {

            public void mouseClicked( MouseEvent mouseEvent ) {

                mouseEvent.setSource( actionItemCheckBox );

                mouseListener.mouseClicked( mouseEvent );
            }

            public void mousePressed( MouseEvent mouseEvent ) {

                mouseEvent.setSource( actionItemCheckBox );

                mouseListener.mousePressed( mouseEvent );
            }

            public void mouseReleased( MouseEvent mouseEvent ) {

                mouseEvent.setSource( actionItemCheckBox );

                mouseListener.mouseReleased( mouseEvent );
            }

            public void mouseEntered( MouseEvent mouseEvent ) {

                mouseEvent.setSource( actionItemCheckBox );

                mouseListener.mouseEntered( mouseEvent );
            }

            public void mouseExited( MouseEvent mouseEvent ) {

                mouseEvent.setSource( actionItemCheckBox );

                mouseListener.mouseExited( mouseEvent );
            }
        };

        return ( mouseListenerThis );
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
        panelMain.setLayout( new GridLayoutManager( 1, 2, new Insets( 3, 5, 3, 5 ), -1, 0 ) );
        panelMain.setBackground( new Color( -1 ) );
        panelMain.setOpaque( true );
        checkBox = new JCheckBox();
        checkBox.setContentAreaFilled( false );
        checkBox.setText( "" );
        panelMain.add( checkBox, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
        viewport = new JViewport();
        viewport.setLayout( new GridLayoutManager( 1, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        panelMain.add( viewport, new GridConstraints( 0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false ) );
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
