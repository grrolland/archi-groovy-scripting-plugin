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
