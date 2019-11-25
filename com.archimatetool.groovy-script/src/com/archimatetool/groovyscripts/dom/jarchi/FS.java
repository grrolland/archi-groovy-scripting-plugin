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
package com.archimatetool.groovyscripts.dom.jarchi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Base64;

/**
 * File services
 * 
 * @author jbsarrodie
 */
public class FS {
    /**
     * Write text to file (using UTF-8)
     * @param path
     * @param text
     * @throws IOException
     */
    public void writeFile(String path, String text) throws IOException {
    	writeFile(path, text, "UTF-8"); //$NON-NLS-1$
    }
    
    /**
     * Write text to file
     * @param path
     * @param text
     * @param encoding use BASE64 to write a binary file
     * @throws IOException
     */
    public void writeFile(String path, String text, String encoding) throws IOException {
        if(encoding.equals("BASE64")) { //$NON-NLS-1$
            writeBinFile(path, text);
        }
        else {
            File file = new File(path);
            file.createNewFile();

            try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), encoding)) {
                writer.write(text);
                writer.flush();
            }
        }
    }

    /**
     * Write a binary file
     * @param path
     * @param base64text
     * @throws IOException
     */
    private void writeBinFile(String path, String base64text) throws IOException {
        File file = new File(path);
        file.createNewFile();
        
        try(FileOutputStream writer = new FileOutputStream(file)) {
            writer.write(Base64.getDecoder().decode(base64text));
            writer.flush();
        }
    }
}
