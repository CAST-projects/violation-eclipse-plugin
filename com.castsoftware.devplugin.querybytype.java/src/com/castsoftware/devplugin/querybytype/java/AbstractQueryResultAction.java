package com.castsoftware.devplugin.querybytype.java;

import org.eclipse.jdt.core.IJavaElement;

import com.castsoftware.devplugin.querybytype.IQueryResultAction;

public abstract class AbstractQueryResultAction implements IQueryResultAction
{
	private IJavaElement itsElement;

	public AbstractQueryResultAction(IJavaElement itsElmement)
	{
		super();
		this.itsElement = itsElmement;
	}

	public String getName()
	{
		return itsElement.getElementName()+" in "+itsElement.getPath().toOSString();
	}

	public IJavaElement getElement()
	{
		return itsElement;
	}

	
}
