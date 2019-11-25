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

import java.util.List;

import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;

/**
 * RemovePropertiesCommand
 * 
 * @author Phillip Beauvoir
 */
public class RemovePropertiesCommand extends ScriptCommand {
   
    private IProperties eObject;
    private List<IProperty> toRemove;

    public RemovePropertiesCommand(IProperties eObject, List<IProperty> toRemove) {
        super("properties", eObject); //$NON-NLS-1$
        this.eObject = eObject;
        this.toRemove = toRemove;
    }

    @Override
    public void undo() {
        eObject.getProperties().addAll(toRemove);
    }

    @Override
    public void perform() {
        eObject.getProperties().removeAll(toRemove);
    }
    
    @Override
    public boolean canExecute() {
        return toRemove != null && !toRemove.isEmpty();
    }
    
    @Override
    public void dispose() {
        toRemove = null;
        eObject = null;
    }
}
