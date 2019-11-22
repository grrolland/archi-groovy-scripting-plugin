package com.archimatetool.groovyscripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import com.archimatetool.groovyscripts.preferences.IPreferenceConstants;

public class DependenciesResolver {

	private Invoker invoker = new DefaultInvoker();
	
	private void init() {
        invoker.setMavenHome(getMavenHome());
	}
	
	private File getMavenHome() {
		URL mavenHome = FileLocator.find(ArchiScriptPlugin.INSTANCE.getBundle(), new Path("tools/maven"), null);
        try {
			mavenHome = FileLocator.toFileURL(mavenHome);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
        return new File(mavenHome.getPath());
	}
	
	private File getPOM() {
		URL mavenPom = FileLocator.find(ArchiScriptPlugin.INSTANCE.getBundle(), new Path("tools/pom.xml"), null);
        try {
			mavenPom = FileLocator.toFileURL(mavenPom);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
        return new File(mavenPom.getPath());
	}
	
	private String getLibPath() {
		return ArchiScriptPlugin.INSTANCE.getPreferenceStore().getString(IPreferenceConstants.PREFS_SCRIPTS_FOLDER) + File.separator +".libs";
	}
	
	private String getDepsPath() {
		return ArchiScriptPlugin.INSTANCE.getPreferenceStore().getString(IPreferenceConstants.PREFS_SCRIPTS_FOLDER) + File.separator +".deps";
	}
	
	
	private InvocationRequest createInvocationRequest(String stringGAV) {
		
		String[] gav = stringGAV.split(":");
		String group = gav[0];
		String artifact = gav[1];
		String version = gav[2];
		
		InvocationRequest request = new DefaultInvocationRequest();
        request.setGoals(Collections.singletonList("dependency:copy-dependencies"));
        Properties props = new Properties();
        props.setProperty("outputDirectory", getLibPath());
        props.setProperty("c_group", group);
        props.setProperty("c_artifact", artifact);
        props.setProperty("c_version", version);
        request.setProperties(props);
        request.setBatchMode(true);
        request.setPomFile(getPOM());
		
		return request;
	}
	
	
	private List<String> readDepsFile() {
		
		final List<String> deps = new ArrayList<String>();
		
		BufferedReader depsFile = null;
		try {
			depsFile = new BufferedReader(new FileReader(new File(getDepsPath())));
			
			for(String line = depsFile.readLine(); line != null; line = depsFile.readLine())
			{
				if (line.contains("#")) {
					if (line.startsWith("#"))
					{
						line = "";
					}
					else
					{
						line = line.split("#")[0];	
					}
				}
				if (line.split(":").length == 3)
				{
					deps.add(line.strip());
				}
			}
			
		} catch (IOException e) {
		}
		finally {
			if (depsFile != null) {
				try {
					depsFile.close();
				} catch (IOException e) {
				}
			}
		}
		
		return deps;
	}
	
	public void resolveDepencencies() {
		init();
		readDepsFile().forEach(s -> {
			try {
				invoker.execute(createInvocationRequest(s));
			} catch (MavenInvocationException e) {
				e.printStackTrace();
			}
		});
	}
	
	public List<URL> getDependenciesURL() {
		final List<URL> urls = new ArrayList<URL>();
		File libPath = new File(getLibPath());
		if (libPath.exists()) {
			Arrays.asList(libPath.listFiles((dir, name) -> name.endsWith(".jar"))).forEach(jar -> {
				try {
					urls.add(jar.toURI().toURL());
			    } catch (MalformedURLException e) {
			    	e.printStackTrace();
				}});		
		}
		return urls;
	}
	
	
}
