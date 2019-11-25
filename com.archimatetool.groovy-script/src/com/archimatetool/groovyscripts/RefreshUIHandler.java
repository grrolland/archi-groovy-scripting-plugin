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
package com.archimatetool.groovyscripts;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.groovyscripts.preferences.IPreferenceConstants;

/**
 * RefreshUIHandler
 * 
 * @author Phillip Beauvoir
 */
public class RefreshUIHandler {
    
    // Refresh UI at set interval
    private static final int refreshInterval = 100;

    private static long time = 0L;

    public static void init() {
        if(!shouldRun()) {
            return;
        }
        
        // Disable UI
        setShellEnabled(false);

        // Current time
        time = System.currentTimeMillis();
    }
    
    public static void refresh() {
        if(!shouldRun()) {
            return;
        }
        
        // Not enough refresh interval time has passed
        if(System.currentTimeMillis() - time < refreshInterval) {
            return;
        }
        
        // Update UI thread
        try {
            if(Display.getCurrent() != null) {
                while(Display.getCurrent().readAndDispatch());
            }
        }
        catch(Exception ex) {
            setShellEnabled(true);
            ex.printStackTrace();
        }
        finally {
            time = System.currentTimeMillis();
        }
    }
    
    public static void finalise() {
        if(shouldRun()) {
            setShellEnabled(true);
        }
    }
    
    /**
     * Disable/Enable Application Shell and Menu Bar so user doesn't edit models
     */
    private static void setShellEnabled(boolean enabled) {
        if(Display.getCurrent() != null) {
            if(Display.getCurrent().getSystemMenu() != null) { // Mac
                Display.getCurrent().getSystemMenu().setEnabled(enabled);
            }
            
            for(Shell shell : Display.getCurrent().getShells()) {
                shell.setEnabled(enabled);
                if(shell.getMenuBar() != null) { // Mac/Linux will have menu bar enabled
                    shell.getMenuBar().setEnabled(enabled);
                }
            }
        }
    }

    private static boolean shouldRun() {
        return PlatformUI.isWorkbenchRunning() &&
                ArchiScriptPlugin.INSTANCE.getPreferenceStore().getBoolean(IPreferenceConstants.PREFS_REFRESH_UI_WHEN_RUNNING_SCRIPT);
    }
}
