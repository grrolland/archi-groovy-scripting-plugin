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

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.groovyscripts.commands.CommandHandler;
import com.archimatetool.groovyscripts.commands.DeleteFolderObjectCommand;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IFolder;

/**
 * DiagramModel wrapper proxy
 * 
 * @author Phillip Beauvoir
 */
public class FolderProxy extends EObjectProxy {
    
    FolderProxy(IFolder folder) {
        super(folder);
    }
    
    @Override
    protected IFolder getEObject() {
        return (IFolder)super.getEObject();
    }
    
    @Override
    protected EObjectProxyCollection children() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        
        for(IFolder folder : getEObject().getFolders()) {
            list.add(EObjectProxy.get(folder));
        }

        for(EObject eObject : getEObject().getElements()) {
            list.add(EObjectProxy.get(eObject));
        }
        
        return list;
    }
    
    @Override
    public EObjectProxy setName(String name) {
        // Can only rename user folders
        if(isUserFolder()) {
            super.setName(name);
        }
        
        return this;
    }
    
    /**
     * Create a sub-folder
     * @param name
     * @return
     */
    public FolderProxy createFolder(String name) {
        return ModelFactory.createFolder(getEObject(), name);
    }
    
    /**
     * Add conceptProxy to this folder.
     * If conceptProxy already has a parent then conceptProxy is moved to this folder
     * throws ArchiScriptException if incorrect folder type
     * @param conceptProxy
     * @return
     */
    public FolderProxy add(ArchimateConceptProxy conceptProxy) {
        ModelFactory.addObject(getEObject(), conceptProxy.getEObject());
        return this;
    }
    
    /**
     * Add diagramProxy to this folder.
     * If diagramProxy already has a parent then diagramProxy is moved to this folder
     * throws ArchiScriptException if incorrect folder type
     * @param diagramProxy
     * @return
     */
    public FolderProxy add(DiagramModelProxy diagramProxy) {
        ModelFactory.addObject(getEObject(), diagramProxy.getEObject());
        return this;
    }
    
    /**
     * Add folderProxy to this folder.
     * If folderProxy already has a parent then folderProxy is moved to this folder
     * throws ArchiScriptException if incorrect folder type
     * @param folderProxy
     * @return
     */
    public FolderProxy add(FolderProxy folderProxy) {
        ModelFactory.addFolder(getEObject(), folderProxy.getEObject());
        return this;
    }

    @Override
    public void delete() {
        if(!isUserFolder()) {
            return;
        }

        for(EObjectProxy child : children()) {
            child.delete();
        }
        
        if(getEObject().getArchimateModel() != null) {
            CommandHandler.executeCommand(new DeleteFolderObjectCommand(getEObject()));
        }
    }
    
    private boolean isUserFolder() {
        return getEObject().getType() == FolderType.USER;
    }
}
