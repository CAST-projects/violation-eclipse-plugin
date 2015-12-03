package com.castsoftware.devplugin.core.provider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.castsoftware.devplugin.core.Activator;

public class CentralObjectProviderRegistry {
	
	private static final String EXTENSION_ID = "com.castsoftware.devplugin.core.provider.Registrer";
	private Map<String,CentralObjectProvider> itsRegistry=Collections.synchronizedMap(new HashMap<String, CentralObjectProvider>());
	private static CentralObjectProviderRegistry sSingleton;
	private boolean itsIsRegistryLoaded;
	
	
	public static synchronized CentralObjectProviderRegistry getSSingleton() {
		if(sSingleton==null)
			sSingleton=new CentralObjectProviderRegistry();
		
		return sSingleton;
	}

	public CentralObjectProvider getProvider(String key) {
		readExtensions();
		return itsRegistry.get(key==null?"":key);
	}

	public CentralObjectProvider putProvider(String key, CentralObjectProvider value) {
		return itsRegistry.put(key==null?"":key, value);
	}
	
	private synchronized void readExtensions()
	{
		if(itsIsRegistryLoaded)
			return;
		
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_ID);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++)
		{
			IExtension ext=extensions[x];
			IConfigurationElement[] elements = ext.getConfigurationElements();
			for (int i = 0; i < elements.length; i++)
			{
				IConfigurationElement next = elements[i];
				if ("class".equals(next.getName()))
				{
					Registrer reg=null;
					try
					{
						reg = (Registrer) next.createExecutableExtension("name");
						reg.initializeProvider(this);
					} catch (CoreException e)
					{
						
						Platform.getLog(Activator.getDefault().getBundle()).log(new Status(IStatus.ERROR,Activator.PLUGIN_ID,e.getMessage()));
					}
				}
			}
		}
		itsIsRegistryLoaded=true;
	}


}
