package com.nervestaple.gtdinbox.gui.form.markdownsheet;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.nervestaple.gtdinbox.gui.utility.UtilitiesGui;

import javax.swing.*;
import java.awt.*;

/**
 * Provides a panel with a Markdown cheat sheet.
 *
 * @author Christopher Miles
 * @version 1.0
 */
public class MarkdownSheetPanel extends JPanel {

    // gui form objects
    private JTextPane textPane;
    private JPanel panelMain;
    private JScrollPane scrollPane;

    public MarkdownSheetPanel() {

        super();

        setContent();

        UtilitiesGui.addHyperLinkListener(textPane);

        setLayout(new GridLayout(1, 1));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(panelMain);
    }

    // private methods

    private void setContent() {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                textPane.setContentType("text/html");
                textPane.setText(
                        "<html>\n" +
                                "<head>\n" +
                                "<style type=\"text/css\">\n" +
                                "\n" +
                                "* {\n" +
                                "\tmargin: 0;\n" +
                                "\tpadding: 0;\n" +
                                "}\n" +
                                "a, a:visited {\n" +
                                "\ttext-decoration:none;\n" +
                                "\tcolor:#6792EE;\n" +
                                "}\n" +
                                "a:hover {\n" +
                                "\tcolor:#FFAC00;\n" +
                                "}\n" +
                                "body {\n" +
                                "\tcolor:#555;\n" +
                                "\tfont-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n" +
                                "\tfont-size: 10px;\n" +
                                "\tline-height: 1.3;\n" +
                                "\tmargin: 10px 0 0 25px;\n" +
                                "\twidth:930px;\n" +
                                "}\n" +
                                "h1 {\n" +
                                "\tmargin-bottom:1em;\n" +
                                "}\n" +
                                "h2, h3, h4, h5, h6 {\n" +
                                "\tmargin-bottom: 10px;\n" +
                                "\ttext-transform:uppercase;\n" +
                                "\tletter-spacing: 3px;\n" +
                                "\tcolor:#84db17;\n" +
                                "}\n" +
                                "\n" +
                                "h3 {\n" +
                                "\tfont-size: 110%;\n" +
                                "}\n" +
                                "h4 {\n" +
                                "\tfont-size: 100%;\n" +
                                "}\n" +
                                "h5 {\n" +
                                "\tfont-size: 100%;\n" +
                                "}\n" +
                                "p, ul, ol {\n" +
                                "\tfont-size: 10px;\n" +
                                "\tmargin-bottom: 10px;\n" +
                                "}\n" +
                                "ul, ol {\n" +
                                "\tmargin-left: 20px;\n" +
                                "}\n" +
                                "pre {\n" +
                                "\tpadding: 5px;\n" +
                                "\tmargin-bottom: 10px;\n" +
                                "\tbackground:#f5f5f5;\n" +
                                "\tborder:1px solid #eee;\n" +
                                "}\n" +
                                "code {\n" +
                                "\tcolor:#000;\n" +
                                "\tfont-family:Consolas, Osaka-Mono, monospace;\n" +
                                "\tline-height:1;\n" +
                                "}\n" +
                                ".column {\n" +
                                "\twidth:30%;\n" +
                                "\tmin-width:24em;\n" +
                                "\tfloat: left;\n" +
                                "\tmargin-right:20px;\n" +
                                "}\n" +
                                "div#top {\n" +
                                "\twidth: 95%;\n" +
                                "\tclear:both;\n" +
                                "\tmargin-bottom:1em;\n" +
                                "\tborder-bottom:1px solid #aaa;\n" +
                                "}\n" +
                                "p.credit {\n" +
                                "\tfont:1em helvetica, sans-serif;\n" +
                                "\tposition:absolute;\n" +
                                "\ttop:2em;\n" +
                                "\tleft:60%;\n" +
                                "}\n" +
                                "\n" +
                                "</style>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "\n" +
                                "<div id=\"top\">\n" +
                                "<h1>The <a href=\"http://ollieman.net/\">ollieman</a> Markdown cheatsheet</h1>\n" +
                                "\n" +
                                "\t<p class=\"credit\"><a href=\"http://daringfireball.net/projects/markdown/\">Markdown</a> and the Markdown Cheatsheet are copyright <a href=\"http://daringfireball.net/\">John Gruber</a>.\n" +
                                "</div><!-- top -->\n" +
                                "\n" +
                                "<div id=\"cheatsheet\">\n" +
                                "\n" +
                                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td>\n" +
                                "<div id=\"one\" class=\"column\">\n" +
                                "<h3>Phrase Emphasis</h3>\n" +
                                "\n" +
                                "<pre><code>*italic*   **bold**\n" +
                                "_italic_   __bold__\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h3>Links</h3>\n" +
                                "\n" +
                                "<p>Inline:</p>\n" +
                                "\n" +
                                "<pre><code>An [example](http://url.com/ \"Title\")\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<p>Reference-style labels (titles are optional):</p>\n" +
                                "\n" +
                                "<pre><code>An [example][id]. Then, anywhere\n" +
                                "else in the doc, define the link:\n" +
                                "\n" +
                                "  [id]: http://example.com/  \"Title\"\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h3>Images</h3>\n" +
                                "\n" +
                                "<p>Inline (titles are optional):</p>\n" +
                                "\n" +
                                "<pre><code>![alt text](/path/img.jpg \"Title\")\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<p>Reference-style:</p>\n" +
                                "\n" +
                                "<pre><code>![alt text][id]\n" +
                                "\n" +
                                "  [id]: /url/to/img.jpg \"Title\"\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h3>Headers</h3>\n" +
                                "\n" +
                                "<p>Setext-style:</p>\n" +
                                "\n" +
                                "<pre><code>Header 1\n" +
                                "========\n" +
                                "\n" +
                                "Header 2\n" +
                                "--------\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "</div> <!-- column -->\n" +
                                "</td><td>\n" +
                                "<div id=\"two\" class=\"column\">\n" +
                                "\n" +
                                "<p>atx-style (closing #'s are optional):</p>\n" +
                                "\n" +
                                "<pre><code># Header 1 #\n" +
                                "\n" +
                                "## Header 2 ##\n" +
                                "\n" +
                                "###### Header 6\n" +
                                "\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h3>Lists</h3>\n" +
                                "\n" +
                                "<p>Ordered, without paragraphs:</p>\n" +
                                "\n" +
                                "<pre><code>1.  Foo\n" +
                                "2.  Bar\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<p>Unordered, with paragraphs:</p>\n" +
                                "\n" +
                                "<pre><code>*   A list item.\n" +
                                "\n" +
                                "    With multiple paragraphs.\n" +
                                "\n" +
                                "*   Bar\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<p>You can nest them:</p>\n" +
                                "\n" +
                                "<pre><code>*   Abacus\n" +
                                "    * ass\n" +
                                "*   Bastard\n" +
                                "    1.  bitch\n" +
                                "    2.  bupkis\n" +
                                "        * BELITTLER\n" +
                                "    3. burper\n" +
                                "*   Cunning\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h3>Manual Line Breaks</h3>\n" +
                                "\n" +
                                "<p>End a line with two or more spaces:</p>\n" +
                                "\n" +
                                "<pre><code>Roses are red,   \n" +
                                "Violets are blue.\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "</div> <!-- column -->\n" +
                                "</td><td>\n" +
                                "<div id=\"three\" class=\"column\">\n" +
                                "\n" +
                                "<h3>Blockquotes</h3>\n" +
                                "\n" +
                                "<pre><code>&gt; Email-style angle brackets\n" +
                                "&gt; are used for blockquotes.\n" +
                                "\n" +
                                "&gt; &gt; And, they can be nested.\n" +
                                "\n" +
                                "&gt; #### Headers in blockquotes\n" +
                                "\n" +
                                "&gt; \n" +
                                "&gt; * You can quote a list.\n" +
                                "&gt; * Etc.\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h3>Code Spans</h3>\n" +
                                "\n" +
                                "<pre><code>`&lt;code&gt;` spans are delimited\n" +
                                "by backticks.\n" +
                                "\n" +
                                "You can include literal backticks\n" +
                                "like `` `this` ``.\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h3>Preformatted Code Blocks</h3>\n" +
                                "\n" +
                                "<p>Indent every line of a code block by at least 4 spaces or 1 tab.</p>\n" +
                                "\n" +
                                "<pre><code>This is a normal paragraph.\n" +
                                "\n" +
                                "    This is a preformatted\n" +
                                "    code block.\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "<h3>Horizontal Rules</h3>\n" +
                                "\n" +
                                "<p>Three or more dashes or asterisks:</p>\n" +
                                "\n" +
                                "<pre><code>---\n" +
                                "\n" +
                                "* * *\n" +
                                "\n" +
                                "- - - -\n" +
                                "</code></pre>\n" +
                                "\n" +
                                "</div><!-- column -->\n" +
                                "</td></tr></table>" +
                                "</div><!-- cheetsheet -->\n" +
                                "\n" +
                                "</body>\n" +
                                "</html>");
            }
        });
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
        panelMain.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.setBackground(new Color(-1));
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(31);
        scrollPane.setVerticalScrollBarPolicy(22);
        panelMain.add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textPane = new JTextPane();
        textPane.setContentType("text/html");
        textPane.setEditable(false);
        scrollPane.setViewportView(textPane);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }
}
