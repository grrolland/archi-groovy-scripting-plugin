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

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.groovyscripts.commands.CommandHandler;
import com.archimatetool.groovyscripts.commands.SetCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelNote;

/**
 * Diagram Model Note wrapper proxy
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelNoteProxy extends DiagramModelObjectProxy {
    
    DiagramModelNoteProxy(IDiagramModelNote object) {
        super(object);
    }
    
    @Override
    protected IDiagramModelNote getEObject() {
        return (IDiagramModelNote)super.getEObject();
    }
    
    public void setText(String text) {
        CommandHandler.executeCommand(new SetCommand(getEObject(),
                IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, StringUtils.safeString(text)));
    }
    
    public String getText() {
        return getEObject().getContent();
    }
    
    @Override
    protected Object attr(String attribute) {
        switch(attribute) {
            case TEXT:
                return getText();
        }
        
        return super.attr(attribute);
    }
    
    @Override
    protected EObjectProxy attr(String attribute, Object value) {
        switch(attribute) {
            case TEXT:
                if(value instanceof String) {
                    setText((String)value);
                }
                break;
        }
        
        return super.attr(attribute, value);
    }
    
    
}
