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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.groovyscripts.ArchiScriptException;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Model utility functions
 * 
 * @author Phillip Beauvoir
 */
public class Model {
    
    /**
     * Create a new model
     * @param modelName the name
     * @return the model
     */
    public ArchimateModelProxy create(String modelName) {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        model.setName(modelName);
        
        IArchiveManager archiveManager = IArchiveManager.FACTORY.createArchiveManager(model);
        model.setAdapter(IArchiveManager.class, archiveManager);
        
        // Don't add a CommandStack or other adapters. These will be created if openInUI() is called
        
        return new ArchimateModelProxy(model);
    }
    
    /**
     * Load and open a model
     * @param path
     * @return the model
     */
    public ArchimateModelProxy load(String path) {
        File file = new File(path);
        
        if(PlatformUI.isWorkbenchRunning()) {
            // Already open in UI
            for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                if(file.equals(model.getFile())) {
                    return new ArchimateModelProxy(model);
                }
            }
            
            // Load and Open it in UI
            IArchimateModel model = IEditorModelManager.INSTANCE.openModel(file);
            if(model != null) {
                return new ArchimateModelProxy(model);
            }
        }
        // No UI, else load from file
        else {
            IArchimateModel model = IEditorModelManager.INSTANCE.loadModel(file);
            if(model != null) {
                return new ArchimateModelProxy(model);
            }
        }
        
        throw new ArchiScriptException(NLS.bind(Messages.ArchimateModelProxy_2, path));
    }
    
    /**
     * Render a View as a String of BASE64 bytes
     * @param dmProxy The DiagramModelProxy
     * @param formatOne of "PNG", "BMP", "JPG", "JPEG",
     * @return a string encoded in BASE64
     * @throws IOException
     */
    public String renderViewAsBase64(DiagramModelProxy dmProxy, String format) throws IOException {
        return renderViewAsBase64(dmProxy, format, null);
    }
    
    /**
     * Render a View as a String of BASE64 bytes
     * @param dmProxy The DiagramModelProxy
     * @param format One of "PNG", "BMP", "JPG", "JPEG",
     * @param options can be scale and margin insets
     * @return a string encoded in BASE64
     * @throws IOException
     */
    public String renderViewAsBase64(DiagramModelProxy dmProxy, String format, Map<?, ?> options) throws IOException {
        if(dmProxy == null || format == null) {
            throw new ArchiScriptException("Null argument"); //$NON-NLS-1$
        }
        
        // Default options
        int scale = ModelUtil.getIntValueFromMap(options, "scale", 1); //$NON-NLS-1$
        int margin = ModelUtil.getIntValueFromMap(options, "margin", 10); //$NON-NLS-1$
        
        // Format
        int imgFormat = SWT.IMAGE_PNG;
        
        switch(format.toUpperCase()) {
            case "PNG": //$NON-NLS-1$
                imgFormat = SWT.IMAGE_PNG;
                break;

            case "BMP": //$NON-NLS-1$
                imgFormat = SWT.IMAGE_BMP;
                break;

            case "GIF": //$NON-NLS-1$
                imgFormat = SWT.IMAGE_GIF;
                break;

            case "JPG": //$NON-NLS-1$
            case "JPEG": //$NON-NLS-1$
                imgFormat = SWT.IMAGE_JPEG;
                break;

            default:
                break;
        }
        
        Image image = DiagramUtils.createImage(dmProxy.getEObject(), scale, margin);
        
        try {
            ImageLoader loader = new ImageLoader();
            loader.data = new ImageData[] { image.getImageData(ImageFactory.getImageDeviceZoom()) };
            
            try(ByteArrayOutputStream stream = new ByteArrayOutputStream(1024)) {
                loader.save(stream, imgFormat);
                
                Encoder encoder = Base64.getEncoder();
                return encoder.encodeToString(stream.toByteArray());
            }
        }
        finally {
            image.dispose();
        }
    }

    /**
     * @param relationshipType
     * @param sourceType
     * @param targetType
     * @return True if relationship type is allowed between source and target
     */
    public boolean isAllowedRelationship(String relationshipType, String sourceType, String targetType) {
        EClass relClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(ModelUtil.getCamelCase(relationshipType));
        EClass sourceClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(ModelUtil.getCamelCase(sourceType));
        EClass targetClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(ModelUtil.getCamelCase(targetType));
        
        if(relClass == null || sourceClass == null || targetClass == null) {
            throw new ArchiScriptException("Invalid type name."); //$NON-NLS-1$
        }
        
        return ArchimateModelUtils.isValidRelationship(sourceClass, targetClass, relClass);
    }
}
