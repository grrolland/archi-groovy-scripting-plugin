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

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IFolder;

/**
 * AddElementCommand
 * 
 * @author Phillip Beauvoir
 */
public class AddRelationshipCommand extends ScriptCommand {
   
    private IFolder parent;
    private IArchimateRelationship relationship;
    private IArchimateConcept source, target;

    public AddRelationshipCommand(IFolder parent, IArchimateRelationship relationship, IArchimateConcept source, IArchimateConcept target) {
        super("add", parent.getArchimateModel()); //$NON-NLS-1$
        this.relationship = relationship;
        this.source = source;
        this.target = target;
        this.parent = parent;
    }
    
    @Override
    public void undo() {
        relationship.disconnect();
        parent.getElements().remove(relationship);
    }

    @Override
    public void perform() {
        relationship.connect(source, target);
        parent.getElements().add(relationship);
    }
    
    @Override
    public void dispose() {
        parent = null;
        relationship = null;
    }
}
