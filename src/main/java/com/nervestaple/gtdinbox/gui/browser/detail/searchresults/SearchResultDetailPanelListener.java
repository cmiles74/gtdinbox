package com.nervestaple.gtdinbox.gui.browser.detail.searchresults;

import com.nervestaple.gtdinbox.model.inboxcontext.InboxContext;
import com.nervestaple.gtdinbox.model.item.actionitem.ActionItem;
import com.nervestaple.gtdinbox.model.item.referenceitem.ReferenceItem;
import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.model.reference.category.Category;

/**
 * Provides an interface objects may implement if they want to handle events form the SearchResultDetailPanelListener.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface SearchResultDetailPanelListener {

    public void projectDoubleClicked( Project project );

    public void actionItemDoubleClicked( ActionItem actionItem );

    public void inboxContextDoubleClicked( InboxContext inboxContext );

    public void categoryDoubleClicked( Category category );

    public void referenceItemDoubleClicked( ReferenceItem referenceItem );
}
