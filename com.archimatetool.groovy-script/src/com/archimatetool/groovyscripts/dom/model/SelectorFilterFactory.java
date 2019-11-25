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

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;

/**
 * Selector Filter Factory
 * 
 * @author Phillip Beauvoir
 */
class SelectorFilterFactory {
    
    public static interface ISelectorFilter {
        boolean accept(EObject object);
        
        default boolean isSingle() {
            return false;
        }
    }
    
    private SelectorFilterFactory() {}
    
    static SelectorFilterFactory INSTANCE = new SelectorFilterFactory();

    public ISelectorFilter getFilter(String selector) {
        if(selector == null || "".equals(selector)) { //$NON-NLS-1$
            return null;
        }
        
        // All model concepts, diagram models, and folders
        if(selector.equals("*")) { //$NON-NLS-1$
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    return (object instanceof IArchimateConcept || object instanceof IDiagramModel
                            || object instanceof IFolder);
                }
            };
        }
        
        // All concepts
        else if(selector.equals(IModelConstants.CONCEPT)) {
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    object = getReferencedConcept(object);
                    return object instanceof IArchimateConcept;
                }
            };
        }
        
        // All elements
        else if(selector.equals(IModelConstants.ELEMENT)) {
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    object = getReferencedConcept(object);
                    return object instanceof IArchimateElement;
                }
            };
        }
        
        // All relationships
        else if(selector.equals(IModelConstants.RELATION) || selector.equals(IModelConstants.RELATIONSHIP)) {
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    object = getReferencedConcept(object);
                    return object instanceof IArchimateRelationship;
                }
            };
        }

        // All views
        else if(selector.equals(IModelConstants.VIEW)) {
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    return object instanceof IDiagramModel;
                }
            };
        }

        // Find single unique object by its ID
        else if(selector.startsWith("#") && selector.length() > 1) { //$NON-NLS-1$
            String id = selector.substring(1);
            
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    return object instanceof IIdentifier && id.equals(((IIdentifier)object).getId());
                }
                
                @Override
                public boolean isSingle() {
                    return true;
                }
            };
        }
        
        // Find all objects with given name
        else if(selector.startsWith(".") & selector.length() > 1) { //$NON-NLS-1$
            String name = selector.substring(1);
            
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    return (object instanceof INameable) && name.equals(((INameable)object).getName());
                }
            };
        }
        
        // Find all objects with given type (class) and name
        else if(selector.contains(".") && selector.length() > 2) { //$NON-NLS-1$
            String[] s = selector.split("\\.", 2); //$NON-NLS-1$
            
            if(s.length != 2) {
                return null;
            }
            
            String type = ModelUtil.getCamelCase(s[0]);
            String name = s[1];
            
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    object = getReferencedConcept(object);
                    return object.eClass().getName().equals(type) &&
                            (object instanceof INameable) &&
                            ((INameable)object).getName().equals(name);
                }
            };
        }

        // Class type of concept
        else {
            String type = ModelUtil.getCamelCase(selector);
            return new ISelectorFilter() {
                @Override
                public boolean accept(EObject object) {
                    object = getReferencedConcept(object);
                    return object.eClass().getName().equals(type);
                }
            };
        }
    }
    
    private EObject getReferencedConcept(EObject object) {
        if(object instanceof IDiagramModelArchimateComponent) {
            return ((IDiagramModelArchimateComponent)object).getArchimateConcept();
        }
        
        return object;
    }
}
