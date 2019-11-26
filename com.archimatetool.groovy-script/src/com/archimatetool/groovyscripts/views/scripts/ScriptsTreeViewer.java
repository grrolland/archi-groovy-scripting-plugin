/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.groovyscripts.views.scripts;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.groovyscripts.IArchiScriptImages;
import com.archimatetool.groovyscripts.ScriptFiles;
import com.archimatetool.groovyscripts.views.file.FileTreeViewer;


/**
 * Scripts File Tree Viewer
 */
public class ScriptsTreeViewer extends FileTreeViewer {

    /**
     * Constructor
     */
    public ScriptsTreeViewer(File rootFolder, Composite parent) {
        super(rootFolder, parent);
        
        // Drag support
        new ScriptsTreeViewerDragDropHandler(this);
    }
    
    @Override
    protected IBaseLabelProvider getUserLabelProvider() {
        return new ScriptsTreeLabelProvider(); 
    }
    
    
    // ===============================================================================================
	// ===================================== Label Provider ==========================================
	// ===============================================================================================

    protected class ScriptsTreeLabelProvider extends FileTreeLabelProvider {
        @Override
        public Image getImage(File file) {
            if(ScriptFiles.isScriptFile(file)) {
                return IArchiScriptImages.ImageFactory.getImage(IArchiScriptImages.ICON_GROOVY);
            }
            
            if(ScriptFiles.isLinkedFile(file)) {
                Image image = IArchiScriptImages.ImageFactory.getImage(IArchiScriptImages.ICON_GROOVY);
                
                try {
                    if(!ScriptFiles.resolveLinkFile(file).exists()) {
                        return IArchiScriptImages.ImageFactory.getOverlayImage(image,
                                IArchiScriptImages.ICON_LINK_WARN_OVERLAY, IDecoration.BOTTOM_RIGHT);
                    }
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
                
                return IArchiScriptImages.ImageFactory.getOverlayImage(image,
                        IArchiScriptImages.ICON_LINK_OVERLAY, IDecoration.BOTTOM_RIGHT);
            }
            
            return super.getImage(file);
        }
        
        @Override
        public String getText(File file) {
            if(ScriptFiles.isScriptFile(file) || ScriptFiles.isLinkedFile(file)) {
                return FileUtils.getFileNameWithoutExtension(file);
            }
            
            return super.getText(file);
        }
        
        @Override
        public String getToolTipText(Object element) {
            if(element instanceof File) {
                File file = (File)element;
                if(ScriptFiles.isLinkedFile(file)) {
                    try {
                        return ScriptFiles.resolveLinkFile(file).getAbsolutePath();
                    }
                    catch(IOException ex) {
                        ex.printStackTrace();
                    }
                }
                return file.getAbsolutePath();
            }
            
            return super.getToolTipText(element);
        }
    }
}
