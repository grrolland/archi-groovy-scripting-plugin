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

import java.util.Arrays;
import java.util.List;

/**
 * Interface for model constants
 * 
 * @author Phillip Beauvoir
 */
interface IModelConstants {

    String ID = "id"; //$NON-NLS-1$
    String NAME = "name"; //$NON-NLS-1$
    String DOCUMENTATION = "documentation"; //$NON-NLS-1$
    String PURPOSE = "purpose"; //$NON-NLS-1$
    String TYPE = "type"; //$NON-NLS-1$
    
    String CONCEPT = "concept"; //$NON-NLS-1$
    String ELEMENT = "element"; //$NON-NLS-1$
    String RELATION = "relation"; //$NON-NLS-1$
    String RELATIONSHIP = "relationship"; //$NON-NLS-1$
    String VIEW = "view"; //$NON-NLS-1$
    String VIEWPOINT = "viewpoint"; //$NON-NLS-1$
    
    String SOURCE = "source"; //$NON-NLS-1$
    String TARGET = "target"; //$NON-NLS-1$
    
    String RELATIVE_BENDPOINTS = "relativeBendpoints"; //$NON-NLS-1$
    String START_X = "startX"; //$NON-NLS-1$
    String START_Y = "startY"; //$NON-NLS-1$
    String END_X = "endX"; //$NON-NLS-1$
    String END_Y = "endY"; //$NON-NLS-1$
    
    String BOUNDS = "bounds"; //$NON-NLS-1$
    
    String FILL_COLOR = "fillColor";  //$NON-NLS-1$
    
    String FONT_COLOR = "fontColor";  //$NON-NLS-1$
    String FONT_SIZE = "fontSize";  //$NON-NLS-1$
    String FONT_STYLE = "fontStyle";  //$NON-NLS-1$
    String FONT_NAME = "fontName";  //$NON-NLS-1$
    
    String LINE_COLOR = "lineColor";  //$NON-NLS-1$
    String LINE_WIDTH = "lineWidth";  //$NON-NLS-1$
    
    String OPACITY = "opacity"; //$NON-NLS-1$
    
    String FIGURE_TYPE = "figureType"; //$NON-NLS-1$
    
    String TEXT = "text"; //$NON-NLS-1$
    
    String ACCESS_TYPE = "access-type";  //$NON-NLS-1$
    List<String> ACCESS_TYPES_LIST = Arrays.asList(new String[] {
            "write", //$NON-NLS-1$
            "read", //$NON-NLS-1$
            "access", //$NON-NLS-1$
            "readwrite" //$NON-NLS-1$
    });
    
    String INFLUENCE_STRENGTH = "influence-strength"; //$NON-NLS-1$
}
