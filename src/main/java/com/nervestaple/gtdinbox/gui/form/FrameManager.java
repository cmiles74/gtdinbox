package com.nervestaple.gtdinbox.gui.form;

import com.nervestaple.gtdinbox.gui.SplashFrame;
import com.nervestaple.gtdinbox.gui.browser.BrowserFrame;
import com.nervestaple.gtdinbox.gui.form.about.AboutFrame;
import com.nervestaple.gtdinbox.gui.form.actionitem.ActionItemFrame;
import com.nervestaple.gtdinbox.gui.form.category.CategoryFrame;
import com.nervestaple.gtdinbox.gui.form.context.ContextFrame;
import com.nervestaple.gtdinbox.gui.form.markdownsheet.MarkdownSheetFrame;
import com.nervestaple.gtdinbox.gui.form.preferences.PreferencesFrame;
import com.nervestaple.gtdinbox.gui.form.project.ProjectFrame;

/**
 * Provides a central location for managing all application frames.
 */
public class FrameManager {

    /**
     * Singleton instance.
     */
    private static FrameManager frameManager;

    /**
     * Splash frame.
     */
    private SplashFrame splashFrame;

    /**
     * Browser frame.
     */
    private BrowserFrame browserFrame;

    /**
     * About Frame.
     */
    private AboutFrame aboutFrame;

    /**
     * Preferences Frame.
     */
    private PreferencesFrame preferencesFrame;

    /**
     * Markdown cheat sheet frame.
     */
    private MarkdownSheetFrame markdownSheetFrame;

    /**
     * Project frame.
     */
    private ProjectFrame projectFrame;

    /**
     * InboxContext frame.
     */
    private ContextFrame contextFrame;

    /**
     * Category frame.
     */
    private CategoryFrame categoryFrame;

    /**
     * ActionItem frame.
     */
    private ActionItemFrame actionItemFrame;

    public FrameManager() {

    }

    public SplashFrame getSplashFrame() {
        if(splashFrame == null) {
            splashFrame = new SplashFrame();
        }

        return splashFrame;
    }

    public BrowserFrame getBrowserFrame() {
        if(browserFrame == null) {
            browserFrame = new BrowserFrame();
        }

        return browserFrame;
    }

    public AboutFrame getAboutFrame() {
        if(aboutFrame == null) {
            aboutFrame = new AboutFrame();
        }

        return aboutFrame;
    }

    public PreferencesFrame getPreferencesFrame() {
        if(preferencesFrame == null) {
            preferencesFrame = new PreferencesFrame();
        }

        return preferencesFrame;
    }

    public MarkdownSheetFrame getMarkdownSheetFrame() {
        if(markdownSheetFrame == null) {
            markdownSheetFrame = new MarkdownSheetFrame();
        }

        return markdownSheetFrame;
    }

    public ProjectFrame getProjectFrame() {
        if(projectFrame == null) {
            projectFrame = new ProjectFrame();
        }

        return projectFrame;
    }

    public ContextFrame getContextFrame() {
        if(contextFrame == null) {
            contextFrame = new ContextFrame();
        }

        return contextFrame;
    }

    public CategoryFrame getCategoryFrame() {
        if(categoryFrame == null) {
            categoryFrame = new CategoryFrame();
        }

        return categoryFrame;
    }

    public ActionItemFrame getActionItemFrame() {
        if(actionItemFrame == null) {
            actionItemFrame = new ActionItemFrame();
        }

        return actionItemFrame;
    }
}
