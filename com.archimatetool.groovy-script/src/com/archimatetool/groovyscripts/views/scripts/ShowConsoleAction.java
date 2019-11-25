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

import org.eclipse.jface.action.Action;

import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.groovyscripts.IArchiScriptImages;
import com.archimatetool.groovyscripts.views.console.ConsoleView;

/**
 * Show Console Action
 */
public class ShowConsoleAction extends Action {

    public ShowConsoleAction() {
        setImageDescriptor(IArchiScriptImages.ImageFactory.getImageDescriptor(IArchiScriptImages.ICON_CONSOLE));
        setText(Messages.ShowConsoleAction_0);
        setToolTipText(Messages.ShowConsoleAction_0);
    }
    
    @Override
    public void run() {
        ViewManager.showViewPart(ConsoleView.ID, true);
    }
}