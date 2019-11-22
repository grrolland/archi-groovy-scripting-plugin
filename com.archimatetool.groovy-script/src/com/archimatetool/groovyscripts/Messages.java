package com.archimatetool.groovyscripts;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

    private static final String BUNDLE_NAME = "com.archimatetool.groovyscripts.messages"; //$NON-NLS-1$

    public static String WorkbenchNotRunningException_0;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    private Messages() {
    }
}
