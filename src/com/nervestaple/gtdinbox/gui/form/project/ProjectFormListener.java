package com.nervestaple.gtdinbox.gui.form.project;

import com.nervestaple.gtdinbox.model.project.Project;
import com.nervestaple.gtdinbox.GTDInboxException;

/**
 * Provides an interface that objects may implement if they want to handle form events.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public interface ProjectFormListener {

    /**
     * Called when a new project is added.
     * @param project Project that was added
     */
    public void projectAdded( Project project );

    /**
     * Called when a new project is updated.
     * @param project Project that was updated
     */
    public void projectUpdated( Project project);

    /**
     * Called when an error occurs.
     * @param exception The exception that occurred
     */
    public void exceptionOccurred( GTDInboxException exception );
}
