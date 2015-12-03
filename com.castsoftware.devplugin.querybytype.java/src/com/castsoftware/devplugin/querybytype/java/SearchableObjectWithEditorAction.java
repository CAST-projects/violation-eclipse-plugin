package com.castsoftware.devplugin.querybytype.java;

import org.eclipse.jdt.core.IJavaElement;

public abstract class SearchableObjectWithEditorAction extends SearchableObject
{

	@Override
	protected AbstractQueryResultAction createAction(IJavaElement elt)
	{
		return new EditorQueryResultAction(elt);
	}

}
