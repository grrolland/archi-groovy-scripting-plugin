/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.groovyscripts.views.scripts;

import java.io.File;

import org.eclipse.jface.action.Action;

import com.archimatetool.groovyscripts.IArchiScriptImages;
import com.archimatetool.groovyscripts.RunArchiScript;


/**
 * Run Script Action
 */
public class RunScriptAction extends Action {

    private File fFile;

    public RunScriptAction() {
        setImageDescriptor(IArchiScriptImages.ImageFactory.getImageDescriptor(IArchiScriptImages.ICON_RUN));
        setText(Messages.RunScriptAction_0);
        setToolTipText(Messages.RunScriptAction_1);
    }
    
    public void setFile(File file) {
        fFile = file;
        setEnabled(file != null && !file.isDirectory() && file.exists());
    }

    @Override
    public void run() {
        if(isEnabled()) {
            RunArchiScript script = new RunArchiScript(fFile);
            script.run();
        }
    }
}