package com.castsoftware.devplugin.querybytype;

import org.eclipse.core.runtime.CoreException;

public interface IQueryResultAction
{

	public void run() throws CoreException;
	public String getName();
}
