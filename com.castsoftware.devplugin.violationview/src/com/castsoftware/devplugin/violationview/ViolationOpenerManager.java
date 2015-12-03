package com.castsoftware.devplugin.violationview;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;

import com.castsoftware.devplugin.core.model.Violation;

public class ViolationOpenerManager
{

	private static ViolationOpenerManager singleton;
	private ViolationOpener itsEditorOpener;
	private boolean itsIsRegistryLoaded;
	private static final String EXTENSION_ID = "com.castsoftware.devplugin.violationview";

	private ViolationOpenerManager()
	{
		readExtensions();
	}

	public static synchronized ViolationOpenerManager getSingleton()
	{
		if (singleton == null)
			singleton = new ViolationOpenerManager();

		return singleton;
	}

	private synchronized void readExtensions()
	{
		if (itsIsRegistryLoaded)
			return;

		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(EXTENSION_ID);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++)
		{
			IExtension ext = extensions[x];
			IConfigurationElement[] elements = ext.getConfigurationElements();
			for (int i = 0; i < elements.length; i++)
			{
				IConfigurationElement next = elements[i];
				if ("dblClick".equals(next.getName()))
				{
					try
					{
						itsEditorOpener = (ViolationOpener) next.createExecutableExtension("class");
					}
					catch (CoreException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		itsIsRegistryLoaded = true;
	}

	public boolean hasEditorOpener()
	{
		return itsEditorOpener != null;
	}

	public boolean isViolationSupported(Violation v)
	{
		return itsEditorOpener.isViolationSupported(v);
	}

	public void openViolation(Violation v)
	{
		itsEditorOpener.openViolation(v);
	}

	public Violation getSelectedViolation(ISelection sel)
	{
		if (sel instanceof IStructuredSelection)
		{
			IStructuredSelection struct = (IStructuredSelection) sel;
			Object o = struct.getFirstElement();

			if (o instanceof Violation)
			{
				Violation v = (Violation) o;

				return v;
			}
		}
		return null;

	}

	void openViolationFromEvent(OpenEvent aEvent)
	{
		ISelection sel = aEvent.getSelection();
		openViolationFromSelection(sel);
	}

	void openViolationFromSelection(ISelection sel)
	{

		Violation v = getSelectedViolation(sel);

		if (v != null && itsEditorOpener != null)
			itsEditorOpener.openViolation(v);
	}

}
