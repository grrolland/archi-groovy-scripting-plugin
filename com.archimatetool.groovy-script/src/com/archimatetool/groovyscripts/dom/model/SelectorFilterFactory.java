/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
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
