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

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.commandline.CommandLineState;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.tree.ITreeModelView;
import com.archimatetool.groovyscripts.ArchiScriptException;
import com.archimatetool.groovyscripts.ArchiScriptPlugin;
import com.archimatetool.groovyscripts.dom.IArchiScriptDOMFactory;
import com.archimatetool.model.IArchimateModel;

/**
 * Current model object
 * 
 * Represents the current model
 * This can be the selected model in focus if run in the UI, or the current model if run from the ACLI
 * It can be loaded from file, or created anew.
 * 
 * @author Phillip Beauvoir
 */
public class CurrentModel implements IArchiScriptDOMFactory {
    
    static ArchimateModelProxy INSTANCE = new ArchimateModelProxy(null) {
        @Override
        protected IArchimateModel getEObject() {
            // Throw this exception rather than a NPE if current model has not been set
            IArchimateModel model = super.getEObject();
            if(model == null) {
                throw new ArchiScriptException(Messages.CurrentModel_0);
            }
            return model;
        }
    };
    
    @Override
    public Object getDOMroot() {
        // Get and wrap the currently selected model in the UI if there is one
        // Note that this *can* be null as we need to initialise the CurrentModel instance in all cases
        if(PlatformUI.isWorkbenchRunning()) {
            IWorkbenchPart activePart = ArchiScriptPlugin.INSTANCE.getActivePart();
            
            // Fallback to tree
            if(activePart == null) {
                activePart = ViewManager.findViewPart(ITreeModelView.ID);
            }
            
            if(activePart != null) {
                INSTANCE.setEObject(activePart.getAdapter(IArchimateModel.class));
            }
        }
        // Else, if we are running in CLI mode, get the Current Model if there is one
        else {
            INSTANCE.setEObject(CommandLineState.getModel());
        }
        
        return INSTANCE;
    }
    
}
