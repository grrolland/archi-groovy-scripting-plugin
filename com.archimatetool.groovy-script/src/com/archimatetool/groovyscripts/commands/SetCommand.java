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
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * SetCommand
 * 
 * @author Phillip Beauvoir
 */
public class SetCommand extends ScriptCommand {
    private EStructuralFeature feature;
    private Object oldValue;
    private Object newValue;
    private EObject eObject;

    public SetCommand(EObject eObject, EStructuralFeature feature, Object newValue) {
        super("Script", eObject); //$NON-NLS-1$
        this.feature = feature;
        this.eObject = eObject;
        oldValue = eObject.eGet(feature);
        this.newValue = newValue;
    }
    
    @Override
    public void perform() {
        eObject.eSet(feature, newValue);
    }
    
    @Override
    public void undo() {
        eObject.eSet(feature, oldValue);
    }
    
    @Override
    public boolean canExecute() {
        return (newValue != null) ? !newValue.equals(oldValue)
                : (oldValue != null) ? !oldValue.equals(newValue)
                : false;
    }

    @Override
    public void dispose() {
        eObject = null;
    }
}