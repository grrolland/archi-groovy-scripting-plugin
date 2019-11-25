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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.groovyscripts.RefreshUIHandler;
import com.archimatetool.model.IArchimateModel;

/**
 * CommandHandler
 * 
 * @author Phillip Beauvoir
 */
public class CommandHandler {
    
    private static Map<CommandStack, CompoundCommand> compoundcommands;
    
    public static void init() {
        compoundcommands = new HashMap<CommandStack, CompoundCommand>();
    }

    public static void executeCommand(ScriptCommand cmd) {
        if(!cmd.canExecute()) {
            return;
        }
        
        IArchimateModel model = cmd.getModel();
        CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        
        if(stack != null) {
            CompoundCommand compound = compoundcommands.get(stack);
            if(compound == null) {
                compound = new NonNotifyingCompoundCommand(Messages.CommandHandler_0);
                compoundcommands.put(stack, compound);
            }
            compound.add(cmd);
        }
        
        cmd.perform();
        
        // Take this opportunity to update the UI if set
        RefreshUIHandler.refresh();
    }

    public static void finalise(String scriptName) {
        if(compoundcommands == null) {
            return;
        }
        
        // This simply calls empty execute() methods since perform() has already been called, but puts the commmands on the stack
        for(Entry<CommandStack, CompoundCommand> e : compoundcommands.entrySet()) {
            e.getValue().setLabel(NLS.bind(Messages.CommandHandler_1, scriptName));
            e.getKey().execute(e.getValue());
        }
    }
    
    
}
