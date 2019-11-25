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
package com.archimatetool.groovyscripts.views.file;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;


/**
 * "New Folder" Dialog
 */
public class NewFolderDialog {
	
    /**
     * Owner Shell
     */
    private Shell fShell;
    
    /**
     * Parent Folder
     */
    private File fParentFolder;
    
    /**
     * The new folder
     */
    private File fNewFolder;
    
	/**
	 * Constructor
	 * @param parentFolder Parent File to add to
	 */
	public NewFolderDialog(Shell shell, File parentFolder) {
	    fShell = shell;
	    fParentFolder = parentFolder;
	}
	
    /**
     * @return The new File or null if not set
     */
    public File getFolder() {
        return fNewFolder;
    }
	
    /**
     * Throw up a dialog asking for a Resource Group name
     * @return True if the user entered valid input, false if cancelled
     */
    public boolean open() {
        InputDialog dialog = new InputDialog(fShell,
                Messages.NewFolderDialog_0,
                Messages.NewFolderDialog_1,
                "", //$NON-NLS-1$
                new InputValidator());
        
        int code = dialog.open();
        
        if(code == Window.OK) {
            String s = dialog.getValue();
            fNewFolder = new File(fParentFolder, s);
            return true;
        }
        else {
            return false;
        }
    }
    
    /**
     * Validate user input
     */
    private class InputValidator implements IInputValidator {
        
        @Override
        public String isValid(String newText) {
            if("".equals(newText.trim())) { //$NON-NLS-1$
                return Messages.NewFolderDialog_2;
            }
            
            File folder = new File(fParentFolder, newText);
            if(folder.exists()) {
                return Messages.NewFolderDialog_3;
            }

            // This will ensure non-legal filename characters are disallowed
            try {
                FileSystems.getDefault().getPath(newText);
            }
            catch(InvalidPathException ex) {
                return Messages.NewFolderDialog_4;
            }

            return null;
        }
    }
}
