/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
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
