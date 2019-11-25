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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.groovyscripts.ArchiScriptException;
import com.archimatetool.groovyscripts.commands.CommandHandler;
import com.archimatetool.groovyscripts.commands.SetCommand;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;

/**
 * ArchimateDiagramModelProxy wrapper proxy
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramModelProxy extends DiagramModelProxy {
    
    ArchimateDiagramModelProxy(IArchimateDiagramModel dm) {
        super(dm);
    }
    
    /**
     * Add an Archimate element to an ArchiMate View and return the diagram object
     */
    public DiagramModelObjectProxy add(ArchimateElementProxy elementProxy, int x, int y, int width, int height) {
        return add(elementProxy, x, y, width, height, false);
    }
    
    /**
     * Add an Archimate element to an ArchiMate View and return the diagram object with nested option
     */
    public DiagramModelObjectProxy add(ArchimateElementProxy elementProxy, int x, int y, int width, int height, boolean autoNest) {
        return ModelFactory.addArchimateDiagramObject(getEObject(), elementProxy.getEObject(), x, y, width, height, autoNest);
    }
    
    /**
     * Add an Archimate connection between two diagram components and return the diagram connection
     */
    public DiagramModelConnectionProxy add(ArchimateRelationshipProxy relation, DiagramModelComponentProxy source, DiagramModelComponentProxy target) {
        if(!source.isArchimateConcept() || !target.isArchimateConcept()) {
            throw new ArchiScriptException(Messages.DiagramModelProxy_0);
        }
        
        // Ensure that source and target diagram components belong to this diagram model
        if(source.getEObject().getDiagramModel() != getEObject()) {
            throw new ArchiScriptException(Messages.ArchimateDiagramModelProxy_0);
        }
        if(target.getEObject().getDiagramModel() != getEObject()) {
            throw new ArchiScriptException(Messages.ArchimateDiagramModelProxy_1);
        }

        return ModelFactory.addArchimateDiagramConnection(relation.getEObject(), (IDiagramModelArchimateComponent)source.getEObject(),
                (IDiagramModelArchimateComponent)target.getEObject());
    }

    @Override
    protected IArchimateDiagramModel getEObject() {
        return (IArchimateDiagramModel)super.getEObject();
    }
    
    public Map<String, Object> getViewpoint() {
        IViewpoint vp = ViewpointManager.INSTANCE.getViewpoint(getEObject().getViewpoint());
        
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", vp.getID()); //$NON-NLS-1$
        map.put("name", vp.getName()); //$NON-NLS-1$
        
        return map;
    }
    
    public ArchimateDiagramModelProxy setViewpoint(String id) {
        IViewpoint vp = ViewpointManager.INSTANCE.getViewpoint(id);
        CommandHandler.executeCommand(new SetCommand(getEObject(), IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT, vp.getID()));
        return this;
    }
    
    public boolean isAllowedConceptForViewpoint(String conceptName) {
        EClass eClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(ModelUtil.getCamelCase(conceptName));
        if(eClass != null) {
            return ViewpointManager.INSTANCE.isAllowedConceptForDiagramModel(getEObject(), eClass);
        }
        return false;
    }
    
    @Override
    protected Object attr(String attribute) {
        switch(attribute) {
            case VIEWPOINT:
                return getViewpoint();
        }
        
        return super.attr(attribute);
    }
    
    @Override
    protected EObjectProxy attr(String attribute, Object value) {
        switch(attribute) {
            case VIEWPOINT:
                if(value instanceof String) {
                    return setViewpoint((String)value);
                }
                break;
        }
        
        return super.attr(attribute, value);
    }

}
