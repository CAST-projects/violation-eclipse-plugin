package com.castsoftware.devplugin.violationview;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.castsoftware.devplugin.core.model.Violation;

public class OpenviolationAction implements IObjectActionDelegate {

	protected Violation itsViolation;
	public OpenviolationAction() {
		// TODO Auto-generated constructor stub
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}

	public void run(IAction action) {
		ViolationOpenerManager.getSingleton().openViolation(itsViolation);
		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		
		itsViolation=null;
		
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection struct = (IStructuredSelection) selection;
			
			Object o=struct.getFirstElement();
			
			if (o instanceof Violation) {
				itsViolation = (Violation) o;
				
			}			
		}
		action.setEnabled(itsViolation!=null && ViolationOpenerManager.getSingleton().isViolationSupported(itsViolation));
	}

}
