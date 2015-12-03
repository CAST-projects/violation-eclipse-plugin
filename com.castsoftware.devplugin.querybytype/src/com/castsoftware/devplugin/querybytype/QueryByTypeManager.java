package com.castsoftware.devplugin.querybytype;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * Singleton class used to manage the extensions available to handle
 * queries to display specific artifacts' source code.
 * 
 * @author CDO
 * @author JAR
 *
 */
public class QueryByTypeManager {
	private Map<Integer, IQueryByType> itsContributions = new HashMap<Integer, IQueryByType>();
	private static final String LANGUAGE_EXTENSION_ID="com.castsoftware.devplugin.querybytype";
	private boolean itsLanguageExtensionsAreRead;

	
	private static final String SUBSCRIBER_EXTENSION_ID = "com.castsoftware.devplugin.querybytype.notifyresults";
	private boolean itsSubscriberExtensionsAreRead;
	private List<ISubscriber> itsSubcribers= new LinkedList<ISubscriber>();

	private static QueryByTypeManager sSingleton;
	
	/**
	 * Method to be used to instantiate this class (as this class should not 
	 * be instantiated more than once).
	 * 
	 * @return	QueryByTypeManager
	 */	
	public static synchronized QueryByTypeManager getSingleton() {
		if(sSingleton==null)
			sSingleton = new QueryByTypeManager();
		return sSingleton;
	}

	/**
	 * Constructor.
	 * Made private as instantiation should only be done via getSingleton().
	 * 
	 */ 
	private QueryByTypeManager() {
		super();
	}

	/**
	 * Method used to discover the extensions currently available
	 * (classes to handle specific artifacts like Java Methods, Fields, etc.)
	 * 
	 */
	private synchronized void readLanguageExtensions()
	{		
		if(itsLanguageExtensionsAreRead)
			return;
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(LANGUAGE_EXTENSION_ID);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++)
		{
			IExtension ext=extensions[x];
			IConfigurationElement[] elements = ext.getConfigurationElements();
			for (int i = 0; i < elements.length; i++)
			{
				IConfigurationElement next = elements[i];
				if ("castobjecttype".equals(next.getName()))
				{
					IQueryByType  reg=null;
					try
					{
						reg = (IQueryByType) next.createExecutableExtension("class");
						Integer objetTypeId = new Integer(next.getAttribute("id"));
						
						itsContributions.put(objetTypeId,reg);
						
					} catch (CoreException e)
					{
						Platform.getLog(Activator.getDefault().getBundle()).
							log(new Status(Status.ERROR,Activator.PLUGIN_ID,e.getMessage()));
					}
				}
			}
		}
		itsLanguageExtensionsAreRead=true;
	}

	
	private synchronized void readResultSubscribersExtensions()
	{		
		if(itsSubscriberExtensionsAreRead)
			return;
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(SUBSCRIBER_EXTENSION_ID);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++)
		{
			IExtension ext=extensions[x];
			IConfigurationElement[] elements = ext.getConfigurationElements();
			for (int i = 0; i < elements.length; i++)
			{
				IConfigurationElement next = elements[i];
				if ("subscriber".equals(next.getName()))
				{
					ISubscriber  reg=null;
					try
					{
						reg = (ISubscriber) next.createExecutableExtension("class");
						itsSubcribers.add(reg);
						
					} catch (CoreException e)
					{
						Platform.getLog(Activator.getDefault().getBundle()).
							log(new Status(Status.ERROR,Activator.PLUGIN_ID,e.getMessage()));
					}
				}
			}
		}
		itsSubscriberExtensionsAreRead=true;
	}

	/**
	 * Call the openEditor() method from the class corresponding
	 * to the artifact type (if any extension matching) for which 
	 * we need to display the code.
	 * 
	 * @param	aType			The type of artifact which we want to display the source code (e.g.:"Java Method"
	 * @param	anObjectName	The full name of the artifact which we want to display the source code
	 * @throws CoreException 
	 * 
	 */
 	public int openEditorFor(Integer aType,String anObjectName) throws CoreException
	{
		int nbMatchFound=-1;
 		readLanguageExtensions();
		IQueryByType qbt = itsContributions.get(aType);
		
		if(qbt != null)
		{
			List<IQueryResultAction> result=qbt.openEditorFor(anObjectName);
			
			readResultSubscribersExtensions();

			for(ISubscriber sub:itsSubcribers)
			{
				sub.resultsChanged(result);
			}
			
			if (result==null || result.size()==0) {
				MessageDialog.openWarning(null, 
						"Source Code Not Found",						
						"The source code for this violation cannot be found.\r\n\r\n" +
						"Its project might not be available in the current workspace."
						);
		  
			}
			else
				result.get(0).run();
		}
//		else {
//			MessageDialog.openWarning(null, "Source Code Display", "The source code for a "+aType+"\r\n"+
//					"The source code for '"+anObjectName+"' cannot be displayed." );
//		}
		return nbMatchFound;
		
	}
 	
 	public boolean isViolationSupported(Integer aType,String anObjectName)
	{
		readLanguageExtensions();
		IQueryByType qbt=itsContributions.get(aType);
		
		if(qbt == null)
		{
			return false;
		} else {
			qbt=null;
			return true;
		}
				
	}

}
