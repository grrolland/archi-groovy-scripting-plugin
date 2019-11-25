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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;

import com.archimatetool.model.IArchimateModel;

/**
 * ScriptCommand
 * 
 * @author Phillip Beauvoir
 */
public abstract class ScriptCommand extends Command {
    private IArchimateModel model;

    protected ScriptCommand(String name, IArchimateModel model) {
        super(name);
        this.model = model;
    }

    protected ScriptCommand(String name, EObject eObject) {
        super(name);
        setModel(eObject);
    }

    protected ScriptCommand(String name) {
        super(name);
    }
    
    protected void setModel(EObject eObject) {
        while(!(eObject instanceof IArchimateModel) && eObject != null) {
            eObject = eObject.eContainer();
        }
        model = (IArchimateModel)eObject;
    }
    
    public IArchimateModel getModel() {
        return model;
    }

    public abstract void perform();
    
    @Override
    public final void execute() {
        // Do nothing. Do not use! Use perform() instead.
    }
    
    @Override
    public void redo() {
        perform();
    }
    
    @Override
    public void dispose() {
        model = null;
    }
}