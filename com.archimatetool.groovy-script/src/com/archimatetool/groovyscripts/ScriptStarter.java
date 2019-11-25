/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.groovyscripts;

import java.io.File;
import java.io.IOException;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

public interface ScriptStarter {

	void start(ScriptEngine engine, File file) throws ScriptException, IOException;
}
