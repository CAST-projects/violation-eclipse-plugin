package com.castsoftware.devplugin.commonui;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;

public abstract class MyAbstractPage extends Page {

	private ISelectionListener itsSelListener;

	public MyAbstractPage() {
		super();
		ISelectionService theService =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();


		theService.addPostSelectionListener(itsSelListener = new ISelectionListener(){
			public void selectionChanged(IWorkbenchPart aPart,ISelection aSelection) {
				handleSelection(aSelection);
			}}
		);
		handleSelection(theService.getSelection());
	}

	protected abstract void handleSelection(ISelection aSelection) ;
	
	public void dispose() {
		ISelectionService theService =  PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();
		theService.removePostSelectionListener(itsSelListener);
		super.dispose();
	}


	public void addSelectionChangedListener(ISelectionChangedListener aListener) {

	}
	public void setSelection(ISelection aSelection) {}

	public ISelection getSelection() {
		return null;
	}

	public void removeSelectionChangedListener(
			ISelectionChangedListener aListener) {

	}

}
