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
package com.archimatetool.groovyscripts.views.file;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;

/**
 * EditorInput that stores a Path.
 */
public class PathEditorInput implements IPathEditorInput {
	
    /**
	 * The File
	 */
	private File fFile;
	
	/**
	 * The Path
	 */
	private IPath fPath;
	
	/**
	 * Creates an editor input based of the given path resource.
	 *
	 * @param path the IPath
	 */
	public PathEditorInput(IPath path) {
		if(path == null) {
			throw new IllegalArgumentException();
		}
		fPath = path;
		fFile = fPath.toFile();
	}
	
	/**
	 * Creates an editor input based of the given file resource.
	 *
	 * @param file the file
	 */
	public PathEditorInput(File file) {
		if(file == null) {
			throw new IllegalArgumentException();
		}
		fFile = file;
		fPath = new Path(file.getAbsolutePath());
	}

	@Override
    public int hashCode() {
		return fFile.hashCode();
	}
	
	@Override
    public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof PathEditorInput)) {
			return false;
		}
		
		PathEditorInput other = (PathEditorInput)obj;
		
		return fFile.equals(other.fFile);
	}
	
	@Override
    public boolean exists() {
		return fFile.exists();
	}
	
	@Override
    public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry().getImageDescriptor(fFile.toString());
	}
	
	@Override
    public String getName() {
		return fFile.getName();
	}
	
	@Override
    public String getToolTipText() {
		return fFile.toString();
	}
	
	@Override
    public IPath getPath() {
		return fPath;
	}

	/**
	 * @return The File
	 */
	public File getFile() {
		return fFile;
	}

	@Override
    public <T> T getAdapter(Class<T> adapter) {
		return null;
	}

	@Override
    public IPersistableElement getPersistable() {
		// no persistence
		return null;
	}
}
