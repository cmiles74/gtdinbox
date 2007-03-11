package com.nervestaple.gtdinbox.gui.browser.detail;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.nervestaple.gtdinbox.GTDInboxException;
import com.nervestaple.gtdinbox.configuration.ConfigurationFactory;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManager;
import com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException;
import com.nervestaple.gtdinbox.gui.GTDInboxExceptionHandler;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.textstyletypes.TextStyleType;
import com.nervestaple.gtdinbox.utility.texttransformer.OutputType;
import com.nervestaple.gtdinbox.utility.texttransformer.UtilityTextTransformer;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Provides a detail view for Project objects.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class DetailProjectPanel extends JPanel implements GTDInboxExceptionHandler {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger( this.getClass() );

    /**
     * Project being displayed.
     */
    private Project project;

    /**
     * Exception handler.
     */
    private GTDInboxExceptionHandler gtdInboxExceptionHandler;

    /**
     * Action item list for displaying the items.
     */
    private DetailActionItemList detailActionItemList;

    /**
     * Defaut padding between gui form objects.
     */
    private final int GUI_LAYOUT_PADDING = 10;

    // gui form objects
    private JPanel panelMain;
    private JPanel panelActionItems;
    private JPanel panelFiller;
    private JViewport viewportDescription;
    private JViewport viewportTitle;
    private JLabel labelTypeIcon;
    private JTextPane textPaneDescription;
    private JTextPane textPaneTitle;

    /**
     * Creates a new DetailProjectPanel.
     *
     * @param gtdInboxExceptionHandler Exception to handle
     */
    public DetailProjectPanel( GTDInboxExceptionHandler gtdInboxExceptionHandler ) {

        super();

        this.gtdInboxExceptionHandler = gtdInboxExceptionHandler;

        detailActionItemList = new DetailActionItemList( gtdInboxExceptionHandler );

        setupPanel();
    }

    public void addActionItem( ActionItem actionItem ) throws DataBaseManagerException {

        detailActionItemList.addActionItem( actionItem );
    }

    public void updateActionItem( final ActionItem actionItem ) {

        detailActionItemList.updateActionItem( actionItem );
    }

    public void removeActionItem( ActionItem actionItem ) {

        detailActionItemList.removeActionItem( actionItem );
    }

    public void clearSelection() {

        detailActionItemList.clearSelection();
    }

    // exception handler methods

    /**
     * Called when an exception from a child component occurs.
     *
     * @param exception Exception that occurred
     */
    public void handleException( GTDInboxException exception ) {

        gtdInboxExceptionHandler.handleException( exception );
    }

    // accessor and mutator methods

    public Project getProject() {
        return project;
    }

    /**
     * Sets the project being displayed.
     *
     * @param project Project to display
     * @throws com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException
     *          Problems loading the project data
     */
    public void setProject( final Project project ) throws DataBaseManagerException {

        this.project = project;

        // make sure we have the newest version from the database
        DataBaseManager.getInstance().getSession().refresh( this.project );

        clearPanel();

        updateProjectView( project );
    }

    /**
     * Returns the preferred size of this panel.
     *
     * @return Dimension with the size
     */
    public Dimension getPreferredSize() {

        // get the height of the whole panel
        double height = 0;
        height += viewportTitle.getSize().getHeight();
        height += GUI_LAYOUT_PADDING;
        height += viewportDescription.getSize().getHeight();
        height += GUI_LAYOUT_PADDING;
        height += panelActionItems.getSize().getHeight();
        double width = panelActionItems.getPreferredSize().getWidth();

        // subtract the height of the filler panel
        //height -= panelFiller.getSize().getHeight(); // this seems to be messing up (more) on fast resizing

        return ( new Dimension( ( new Double( width ) ).intValue(), ( new Double( height ) ).intValue() ) );
    }

    public DetailActionItemList getDetailActionItemList() {
        return detailActionItemList;
    }

    // private methods

    private void clearPanel() {

        SwingUtilities.invokeLater( new Runnable() {

            public void run() {

                textPaneTitle.setText( "<html></html>" );

                textPaneDescription.setText( "<html></html>" );
            }
        } );

        detailActionItemList.clearList();
    }

    /**
     * Sets the project to display.
     *
     * @param project Project to display
     * @throws com.nervestaple.gtdinbox.datastore.database.DataBaseManagerException
     *          Problems loading project data
     */
    private void updateProjectView( final Project project ) throws DataBaseManagerException {

        SwingUtilities.invokeLater( new Runnable() {

            public void run() {

                textPaneTitle.setText(
                        "<html>" + UtilityTextTransformer.STYLESHEET +
                                UtilityTextTransformer.transformTextForDisplay( project.getName(),
                                        TextStyleType.MARKDOWN_TEXT, OutputType.TITLE ) + "</html>" );

                textPaneDescription.setText( UtilityTextTransformer.transformTextForDisplay( project.getDescription(),
                        project.getTextStyleType(), OutputType.DESCRIPTION ) );
            }
        } );

        // update the action item panel
        detailActionItemList.setProject( project, false );
    }

    private void setupPanel() {

        textPaneTitle = new JTextPane();
        textPaneTitle.setContentType( "text/html" );
        textPaneTitle.setText( "<html></html>" );
        textPaneTitle.setEditable( false );
        viewportTitle.setLayout( new BorderLayout() );
        viewportTitle.setView( textPaneTitle );

        textPaneDescription = new JTextPane();
        textPaneDescription.setContentType( "text/html" );
        textPaneDescription.setText( "<html></html>" );
        textPaneDescription.setEditable( false );
        viewportDescription.setLayout( new BorderLayout() );
        viewportDescription.setView( textPaneDescription );

        UtilitiesGui.addHyperLinkListener( textPaneTitle );
        UtilitiesGui.addHyperLinkListener( textPaneDescription );

        panelActionItems.setLayout( new GridLayout( 1, 1 ) );
        panelActionItems.add( detailActionItemList );

        detailActionItemList.addDetailActionItemListListener( new DetailActionItemListListener() {

            public void selectionChanged() {
                // do nothing
            }

            public void selectedItemDoubleClicked() {
                // do nothing
            }

            public void componentSizeChanged() {

                logger.debug( "componentSizeChanged" );
                SwingUtilities.invokeLater( new Runnable() {

                    public void run() {

                        panelMain.invalidate();
                        revalidate();
                    }
                } );
            }
        } );

        setLayout( new BorderLayout() );
        add( panelMain );

        // listen for changes to the archive days value
        ConfigurationFactory.getInstance().getApplicationConfiguration().addPropertyChangeListener( "archiveDays",
                new PropertyChangeListener() {

                    public void propertyChange( PropertyChangeEvent propertyChangeEvent ) {

                        if( project != null ) {

                            // reload our data
                            detailActionItemList.setProject( project, false );
                        }
                    }
                } );
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
        panelMain.setLayout( new GridLayoutManager( 3, 1, new Insets( 0, 0, 0, 0 ), -1, 8 ) );
        panelMain.setBackground( new Color( -1 ) );
        panelMain.setMaximumSize( new Dimension( -1, -1 ) );
        panelMain.setMinimumSize( new Dimension( 400, -1 ) );
        panelMain.setPreferredSize( new Dimension( -1, -1 ) );
        panelMain.setRequestFocusEnabled( false );
        panelMain.setVerifyInputWhenFocusTarget( true );
        panelActionItems = new JPanel();
        panelActionItems.setLayout( new BorderLayout( 0, 0 ) );
        panelActionItems.setAutoscrolls( false );
        panelActionItems.setOpaque( false );
        panelMain.add( panelActionItems, new GridConstraints( 1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        panelFiller = new JPanel();
        panelFiller.setLayout( new GridLayoutManager( 1, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        panelFiller.setOpaque( false );
        panelMain.add( panelFiller, new GridConstraints( 2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false ) );
        final JPanel panel1 = new JPanel();
        panel1.setLayout( new GridLayoutManager( 2, 2, new Insets( 5, 5, 0, 5 ), -1, -1 ) );
        panel1.setOpaque( false );
        panelMain.add( panel1, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        viewportDescription = new JViewport();
        viewportDescription.setLayout( new GridLayoutManager( 1, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        panel1.add( viewportDescription, new GridConstraints( 1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        viewportTitle = new JViewport();
        viewportTitle.setLayout( new GridLayoutManager( 1, 1, new Insets( 0, 0, 0, 0 ), -1, -1 ) );
        panel1.add( viewportTitle, new GridConstraints( 0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false ) );
        labelTypeIcon = new JLabel();
        labelTypeIcon.setIcon( new ImageIcon( getClass().getResource( "/com/nervestaple/gtdinbox/gui/browser/images/project-32.png" ) ) );
        labelTypeIcon.setText( "" );
        panel1.add( labelTypeIcon, new GridConstraints( 0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false ) );
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
