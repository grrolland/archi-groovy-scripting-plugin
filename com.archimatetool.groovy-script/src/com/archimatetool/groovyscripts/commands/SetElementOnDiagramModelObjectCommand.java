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

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;

/**
 * Set the Diagram Model Object's IArchimateElement to the given element
 * 
 * @author Phillip Beauvoir
 */
public class SetElementOnDiagramModelObjectCommand extends ScriptCommand {
   
    private IArchimateElement element;
    private IDiagramModelArchimateObject dmo;
    private IArchimateElement oldElement;
    private IDiagramModelContainer parent;
    private int index;
    
    // The figure type can be as set in user preferences
    private boolean setDefaultFigureType;
    private int oldType;
    private int newType;

    /**
     * @param element The element to set on the dmo
     * @param dmo The dmo to set the element on
     * @param setDefaultFigureType if true set this diagram model object's figure type as set in user Preferences
     */
    public SetElementOnDiagramModelObjectCommand(IArchimateElement element, IDiagramModelArchimateObject dmo, boolean setDefaultFigureType) {
        super("setConcept", element.getArchimateModel()); //$NON-NLS-1$
        
        this.element = element;
        this.dmo = dmo;
        oldElement = dmo.getArchimateElement();
        
        this.setDefaultFigureType = setDefaultFigureType;
        oldType = dmo.getType();
        newType = Preferences.STORE.getInt(IPreferenceConstants.DEFAULT_FIGURE_PREFIX + element.eClass().getName());
        
        // Store current state
        parent = (IDiagramModelContainer)dmo.eContainer();
        index = parent.getChildren().indexOf(dmo);
    }
    
    @Override
    public void perform() {
        // Remove the dmo in case it is open in the UI with listeners attached to the underlying concept
        // This will effectively remove the concept listener from the Edit Part
        parent.getChildren().remove(dmo);
        
        // Have to remove referenced dmo first
        // TODO: Remove this line because in Archi 4.5 and greater this is done in dmo.setArchimateElement(element)
        oldElement.getReferencingDiagramObjects().remove(dmo);
        
        // Set it
        dmo.setArchimateElement(element);
        
        // Set figure type
        if(setDefaultFigureType) {
            dmo.setType(newType);
        }
        
        // And re-attach which will also update the UI
        parent.getChildren().add(index, dmo);
    }
    
    @Override
    public void undo() {
        // Remove the dmo in case it is open in the UI with listeners attached to the underlying concept
        // This will effectively remove the concept listener from the Edit Part
        parent.getChildren().remove(dmo);
        
        // Set it back
        dmo.setArchimateElement(oldElement);
        
        // Set figure type
        if(setDefaultFigureType) {
            dmo.setType(oldType);
        }
        
        // And re-attach which will also update the UI
        parent.getChildren().add(index, dmo);
    }

    @Override
    public void dispose() {
        element = null;
        dmo = null;
        oldElement = null;
        parent = null;
    }
}
