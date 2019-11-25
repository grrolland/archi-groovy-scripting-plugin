/**
 * Copyright (c) 2017-2019 Phillip Beauvoir & Jean-Baptiste Sarrodie
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.archimatetool.groovyscripts.views.scripts;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.groovyscripts.views.scripts.messages"; //$NON-NLS-1$

    public static String RestoreExampleScriptsAction_0;

    public static String RestoreExampleScriptsAction_1;

    public static String RunScriptAction_0;

    public static String RunScriptAction_1;

    public static String ScriptsFileViewer_0;

    public static String ScriptsFileViewer_1;
    
    public static String ScriptsFileDepsViewer_0;

    public static String ScriptsFileDepsViewer_1;

    public static String ScriptsFileViewer_2;

    public static String ScriptsFileViewer_3;

    public static String ScriptsTreeViewerDragDropHandler_0;

    public static String ScriptsTreeViewerDragDropHandler_1;

    public static String ScriptsTreeViewerDragDropHandler_2;

    public static String ScriptsTreeViewerDragDropHandler_3;

    public static String ScriptsTreeViewerDragDropHandler_4;

    public static String ShowConsoleAction_0;
    
    public static String Confirm_Deps_Replacement_0;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
