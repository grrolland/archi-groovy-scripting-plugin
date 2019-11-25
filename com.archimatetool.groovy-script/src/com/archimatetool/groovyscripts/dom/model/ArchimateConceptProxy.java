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
package com.archimatetool.groovyscripts.dom.model;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.groovyscripts.ArchiScriptException;
import com.archimatetool.groovyscripts.commands.CommandHandler;
import com.archimatetool.groovyscripts.commands.DeleteFolderObjectCommand;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;

/**
 * Archimate Concept wrapper proxy
 * 
 * @author Phillip Beauvoir
 */
public abstract class ArchimateConceptProxy extends EObjectProxy {
    
    ArchimateConceptProxy(IArchimateConcept concept) {
        super(concept);
    }
    
    @Override
    protected IArchimateConcept getEObject() {
        return (IArchimateConcept)super.getEObject();
    }
    
    // Return this
    public ArchimateConceptProxy getConcept() {
        return this;
    }
    
    /**
     * Set the type of this concept with a new concept of class type, preserving all connecting relationships and diagram components
     * Sub-classes call this first.
     * @param type the Archimate type to replace with
     * @return
     */
    protected ArchimateConceptProxy setType(String type) {
        if(!StringUtils.isSet(type)) {
            return null;
        }
        
        // Check it's not already this type
        String className = ModelUtil.getCamelCase(type);
        if(getEObject().eClass().getName().equals(className)) {
            return null;
        }
        
        if(!ModelUtil.isAllowedSetType(getEObject(), type)) {
            throw new ArchiScriptException(NLS.bind(Messages.ArchimateConceptProxy_1, type));
        }
        
        return this;
    }
    
    protected EObjectProxyCollection outRels() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        for(IArchimateRelationship r : getEObject().getSourceRelationships()) {
            list.add(new ArchimateRelationshipProxy(r));
        }
        return list;
    }
    
    protected EObjectProxyCollection inRels() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        for(IArchimateRelationship r : getEObject().getTargetRelationships()) {
            list.add(new ArchimateRelationshipProxy(r));
        }
        return list;
    }
    
    protected EObjectProxyCollection objectRefs() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        
        if(getEObject().getArchimateModel() != null) {
            for(IDiagramModel dm : getEObject().getArchimateModel().getDiagramModels()) {
                for(IDiagramModelArchimateComponent dmc : DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(dm, getEObject())) {
                    list.add(EObjectProxy.get(dmc));
                }
            }
        }
        
        return list;
    }
    
    protected EObjectProxyCollection viewRefs() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        
        for(IDiagramModel dm : DiagramModelUtils.findReferencedDiagramsForArchimateConcept(getEObject())) {
        	list.add(EObjectProxy.get(dm));
        }
        
        return list;
    }

    @Override
    public void delete() {
        // Delete diagram instances first
        for(EObjectProxy proxy : objectRefs()) {
            proxy.delete();
        }
       
        // Delete all connecting relationships
        for(EObjectProxy proxy : inRels()) {
            proxy.delete();
        }
        
        for(EObjectProxy proxy : outRels()) {
            proxy.delete();
        }

        if(getEObject().getArchimateModel() != null) {
            CommandHandler.executeCommand(new DeleteFolderObjectCommand(getEObject()));
        }
      
    }

    interface Internal extends IReferencedProxy, IConnectableProxy {}
    
    @Override
    protected Object getInternal() {
        return new Internal() {
            @Override
            public EObjectProxyCollection outRels() {
                return ArchimateConceptProxy.this.outRels();
            }
            
            @Override
            public EObjectProxyCollection inRels() {
                return ArchimateConceptProxy.this.inRels();
            }
            
            @Override
            public EObjectProxyCollection viewRefs() {
                return ArchimateConceptProxy.this.viewRefs();
            }
            
            @Override
            public EObjectProxyCollection objectRefs() {
                return ArchimateConceptProxy.this.objectRefs();
            }
        };
    }

}
