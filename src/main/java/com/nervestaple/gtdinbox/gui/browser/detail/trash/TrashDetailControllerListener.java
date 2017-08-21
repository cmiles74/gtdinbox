package com.nervestaple.gtdinbox.gui.browser.detail.trash;

import com.nervestaple.gtdinbox.model.Trashable;

/**
 * Provides an interface objects may implement if they want to handle TrashDetailController events.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface TrashDetailControllerListener {

    public void putAwayTrashable( Trashable trashable );

    public void confirmEmptyTrash( String message );
}
