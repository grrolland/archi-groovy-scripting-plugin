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
package com.archimatetool.groovyscripts.dom.ui;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.ui.services.EditorManager;


/**
 * Represents the "Browser" object
 */
public class Browser {
    
    private IBrowserEditor fBrowserEditor;
    
    public Browser() {
    }
    
    // Return new instance of Browser
    public Browser open() {
        return open("http://localhost", "Archi");  //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    // Return new instance of Browser
    public Browser open(String url) {
        return open(url, url);
    }
    
    // Return new instance of Browser
    public Browser open(String url, String title) {
        Browser browser = new Browser();
        browser.setBrowserEditor(url, title); 
        return browser;
    }

    public void setText(String html) {
        if(fBrowserEditor != null) {
            fBrowserEditor.getBrowser().setText(html, true);
        }
    }
    
    public void setURL(String url) {
        setBrowserEditor(url, url);
    }
    
    public void setTitle(String title) {
        setBrowserEditor(null, title);
    }
    
    public void close() {
        if(fBrowserEditor != null) {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            page.closeEditor(fBrowserEditor, false);
        }
    }

    private void setBrowserEditor(String url, String title) {
        if(!PlatformUI.isWorkbenchRunning()) {
            return;
        }
        
        if(fBrowserEditor == null) {
            BrowserEditorInput input = new BrowserEditorInput(url, title);
            fBrowserEditor = (IBrowserEditor)EditorManager.openEditor(input, IBrowserEditor.ID);
        }
        else {
            if(title == null) {
                title = fBrowserEditor.getEditorInput().getName();
            }
            BrowserEditorInput input = new BrowserEditorInput(url, title);
            fBrowserEditor.setBrowserEditorInput(input);
        }
    }
}
