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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.groovyscripts.ArchiScriptPlugin;
import com.archimatetool.groovyscripts.dom.IArchiScriptDOMFactory;

/**
 * Selection dom object
 * 
 * Represents a collection of currently selected EObjects in the UI (models tree)
 * If Archi is not running an empty collection is returned
 * 
 * @author Phillip Beauvoir
 */
public class Selection implements IArchiScriptDOMFactory {
    
    @Override
    public Object getDOMroot() {
        EObjectProxyCollection list = new EObjectProxyCollection();
        
        if(PlatformUI.isWorkbenchRunning()) {
            ISelection selection = ArchiScriptPlugin.INSTANCE.getCurrentSelection();
            
            if(selection instanceof IStructuredSelection) {
                for(Object o : ((IStructuredSelection)selection).toArray()) {
                    
                    if(o instanceof EditPart) {
                        o = ((EditPart)o).getModel();
                    }
                    else if(o instanceof IAdaptable) {
                        o = ((IAdaptable)o).getAdapter(EObject.class);
                    }
                    
                    if(o instanceof EObject) {
                        EObjectProxy proxy = EObjectProxy.get((EObject)o);
                        if(proxy != null) {
                            list.add(proxy);
                        }
                    }
                }
            }
        }
        
        return list;
    }

}
