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
package com.archimatetool.groovyscripts.commands;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IFolder;

/**
 * DeleteFolderObjectCommand
 * 
 * @author Phillip Beauvoir
 */
public class DeleteFolderObjectCommand extends ScriptCommand {

    private int index;
    private IFolder parent;
    private EObject eObject;

    public DeleteFolderObjectCommand(EObject eObject) {
        super("delete", eObject); //$NON-NLS-1$
        parent = (IFolder)eObject.eContainer();
        this.eObject = eObject;
    }

    @Override
    public void undo() {
        if(index != -1) { // might be already be deleted from Command in CompoundCommand
            if(eObject instanceof IFolder) {
                parent.getFolders().add(index, (IFolder)eObject);
            }
            else {
                parent.getElements().add(index, eObject);
            }
        }
    }

    @Override
    public void perform() {
        // Ensure index is stored just before execute because if this is part of a composite delete action
        // then the index positions will have changed
        
        if(eObject instanceof IFolder) {
            index = parent.getFolders().indexOf(eObject);
            if(index != -1) { // might be already be deleted from Command in CompoundCommand
                parent.getFolders().remove(eObject);
            }
        }
        else {
            index = parent.getElements().indexOf(eObject); 
            if(index != -1) { // might be already be deleted from Command in CompoundCommand
                parent.getElements().remove(eObject);
            }
        }
    }
    
    @Override
    public void dispose() {
        eObject = null;
        parent = null;
    }

}
