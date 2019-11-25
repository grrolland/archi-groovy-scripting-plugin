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
package com.archimatetool.groovyscripts.views.console;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.services.ViewManager;


/**
 * Redirect standard out to Console
 */
public class ConsoleOutput {
    
    private static PrintStream fOldOut, fOldErr;
    private static ConsoleView fConsole;
    
    /**
     * Start the console re-direction
     */
    public static void start() {
        fConsole = findConsoleViewer();
        
        // Redirect to new streams
        if(fConsole != null) {
            addStreams();
        }
    }
    
    /**
     * End the console re-direction
     */
    public static void end() {
        // Restore streams
        restoreStreams();
    }
    
    /**
     * Add new Streams
     */
    private static void addStreams() {
        // Save old streams
        fOldOut = System.out;
        fOldErr = System.err;
        
        try {
            PrintStream out = new PrintStream(new DumpStream(ColorFactory.get(0, 0, 255)), true);
            System.setOut(out);
            
            PrintStream err = new PrintStream(new DumpStream(ColorFactory.get(255, 0, 0)), true);
            System.setErr(err);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Restore the error streams
     */
    private static void restoreStreams() {
        if(fOldOut != null) {
            System.setOut(fOldOut);
        }
        if(fOldErr != null) {
            System.setErr(fOldErr);
        }
    }
    
    /**
     * @return The Console Viewer if it is active, else null
     */
    private static ConsoleView findConsoleViewer() {
        if(PlatformUI.isWorkbenchRunning()) {
            return (ConsoleView)ViewManager.findViewPart(ConsoleView.ID);
        }
        
        return null;
    }

    /**
     * An OutputStream that redirects all System output to the Console
     */
    private static class DumpStream extends OutputStream {
        StringBuffer buf;
        Color color;
        
        public DumpStream(Color color) {
            this.color = color;
        }
        
        @Override
        public void write(int b) {
            if(buf == null) {
                buf = new StringBuffer();
            }
            buf.append((char)(b & 255));
        }
        
        @Override
        public void flush() throws IOException {
            if(buf != null && fConsole != null) {
                Color oldColor = fConsole.getTextColor();
                fConsole.setTextColor(color);
                fConsole.append(buf.toString());
                fConsole.setTextColor(oldColor);
            }
            buf = null;
        }
    }

}
