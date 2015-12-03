package com.castsoftware.devplugin.querybytype.java;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.IPackagesViewPart;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IViewPart;

public class ExplorerQueryResultAction extends AbstractQueryResultAction
{

	public ExplorerQueryResultAction(IJavaElement itsElmement)
	{
		super(itsElmement);
	}

	public void run() throws CoreException
	{
		IViewPart thePart=Activator.getWorkbenchPage().showView(JavaUI.ID_PACKAGES);
		if (thePart != null){
			((IPackagesViewPart) thePart).selectAndReveal(getElement());
		}
	}

}
