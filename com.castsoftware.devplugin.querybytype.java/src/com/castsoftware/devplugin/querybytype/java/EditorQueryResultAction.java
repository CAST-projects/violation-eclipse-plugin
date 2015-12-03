package com.castsoftware.devplugin.querybytype.java;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.ui.IEditorPart;

public class EditorQueryResultAction extends AbstractQueryResultAction
{

	public EditorQueryResultAction(IJavaElement itsElmement)
	{
		super(itsElmement);
		// TODO Auto-generated constructor stub
	}

	public void run() throws CoreException
	{
		ICompilationUnit cu = ((IMember) getElement()).getCompilationUnit();
		IEditorPart javaEditor;
		javaEditor = JavaUI.openInEditor(cu);
		JavaUI.revealInEditor(javaEditor, getElement());
	}

}
