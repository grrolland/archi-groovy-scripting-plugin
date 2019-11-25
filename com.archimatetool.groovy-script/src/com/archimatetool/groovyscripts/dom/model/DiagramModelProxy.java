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

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.groovyscripts.commands.CommandHandler;
import com.archimatetool.groovyscripts.commands.DeleteFolderObjectCommand;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;

/**
 * DiagramModel wrapper proxy
 * 
 * @author Phillip Beauvoir
 */
public abstract class DiagramModelProxy extends EObjectProxy {
    
    DiagramModelProxy(IDiagramModel dm) {
        super(dm);
    }
    
    /**
     * Create and add a diagram object and return the diagram object proxy
     */
    public DiagramModelObjectProxy createObject(String type, int x, int y, int width, int height) {
        return createObject(type, x, y, width, height, false);
    }
    
    /**
     * Create and add a diagram object and return the diagram object proxy with nested option
     */
    public DiagramModelObjectProxy createObject(String type, int x, int y, int width, int height, boolean autoNest) {
        return ModelFactory.createDiagramObject(getEObject(), type, x, y, width, height, autoNest);
    }
    
    @Override
    protected IDiagramModel getEObject() {
        return (IDiagramModel)super.getEObject();
    }
    
    /**
     * @return child node diagram objects of this diagram model
     */
    @Override
    protected EObjectProxyCollection children() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        
        if(getEObject() == null) {
            return list;
        }
        
        // Immediate children IDiagramModelObject
        for(IDiagramModelObject dmo : getEObject().getChildren()) {
            list.add(EObjectProxy.get(dmo));
        }
        
        // All connections
        for(Iterator<EObject> iter = getEObject().eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelConnection) {
                list.add(new DiagramModelConnectionProxy((IDiagramModelConnection)eObject));
            }
        }
        
        return list;
    }

    protected EObjectProxyCollection objectRefs() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        
        if(getEObject().getArchimateModel() != null) {
            for(IDiagramModel dm : getEObject().getArchimateModel().getDiagramModels()) {
                for(IDiagramModelReference ref : DiagramModelUtils.findDiagramModelReferences(dm, getEObject())) {
                    list.add(EObjectProxy.get(ref));
                }
            }
        }
        
        return list;
    }
    
    protected EObjectProxyCollection viewRefs() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        
        if(getEObject().getArchimateModel() != null) {
            for(IDiagramModel dm : getEObject().getArchimateModel().getDiagramModels()) {
                for(IDiagramModelReference ref : DiagramModelUtils.findDiagramModelReferences(dm, getEObject())) {
                    list.add(EObjectProxy.get(ref.getDiagramModel()));
                }
            }
        }
        
        return list;
    }
    
    @Override
    public void delete() {
        // Delete diagram references first
        for(EObjectProxy proxy : objectRefs()) {
            proxy.delete();
        }

        for(EObjectProxy child : children()) {
            if(child instanceof DiagramModelObjectProxy) { // As children() also contains connections don't delete them here
                child.delete();
            }
        }
        
        if(getEObject().getArchimateModel() != null) {
            if(PlatformUI.isWorkbenchRunning()) {
                EditorManager.closeDiagramEditor(getEObject()); // important!!
            }
            
            CommandHandler.executeCommand(new DeleteFolderObjectCommand(getEObject()));
        }
    }

    @Override
    protected Object getInternal() {
        return new IReferencedProxy() {
            @Override
            public EObjectProxyCollection objectRefs() {
                return DiagramModelProxy.this.objectRefs();
            }

            @Override
            public EObjectProxyCollection viewRefs() {
                return DiagramModelProxy.this.viewRefs();
            }
        };
    }
}
