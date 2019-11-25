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

import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;

/**
 * DeleteDiagramModelObjectCommand
 * 
 * @author Phillip Beauvoir
 */
public class DeleteDiagramModelObjectCommand extends ScriptCommand {
    
    private int index;
    private IDiagramModelContainer parent;
    private IDiagramModelObject eObject;

    public DeleteDiagramModelObjectCommand(IDiagramModelObject eObject) {
        super("delete", eObject); //$NON-NLS-1$
        parent = (IDiagramModelContainer)eObject.eContainer();
        this.eObject = eObject;
    }

    @Override
    public void undo() {
        // Add the Child at old index position
        if(index != -1) { // might have already been deleted by another process
            parent.getChildren().add(index, eObject);
        }
    }

    @Override
    public void perform() {
        // Ensure index is stored just before execute because if this is part of a composite delete action
        // then the index positions will have changed
        index = parent.getChildren().indexOf(eObject); 
        if(index != -1) { // might be already be deleted from Command in CompoundCommand
            parent.getChildren().remove(eObject);
        }
    }
    
    @Override
    public void dispose() {
        eObject = null;
        parent = null;
    }
}
