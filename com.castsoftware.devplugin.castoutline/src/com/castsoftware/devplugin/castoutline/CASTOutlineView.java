package com.castsoftware.devplugin.castoutline;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.contentoutline.ContentOutline;

public class CASTOutlineView extends ContentOutline
{

	private static final String EXTENSION_ID = "com.castsoftware.devplugin.castoutline.CASTOutlineView";
	private Set<String> itsWorkbenchPartIDs = new HashSet<String>();

	@Override
	protected boolean isImportant(IWorkbenchPart aPart)
	{
		// return aPart instanceof DiagTreeView || aPart instanceof
		// MyViolationSelectionView;
		String theID = aPart.getSite().getId();
		boolean ret= itsWorkbenchPartIDs.contains(theID);
		
		return ret;
	}

	protected IWorkbenchPart getBootstrapPart()
	{
		IWorkbenchPage page = getSite().getPage();
		if (page != null)
		{
			IWorkbenchPart p = page.getActivePart();
			if (p != null && isImportant(p))
				return p;
		}

		return null;
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException
	{
		super.init(site, memento);
		readExtensions();
	}

	@Override
	public void init(IViewSite site) throws PartInitException
	{
		super.init(site);
		readExtensions();
	}

	private void readExtensions()
	{
		itsWorkbenchPartIDs = new HashSet<String>();

		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_ID);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++)
		{
			IConfigurationElement[] elements = extensions[x].getConfigurationElements();
			for (int i = 0; i < elements.length; i++)
			{
				IConfigurationElement next = elements[i];
				if ("part".equals(next.getName()))
				{
					String theID = next.getAttribute("id");

					itsWorkbenchPartIDs.add(theID);
				}
			}
		}
	}
}
