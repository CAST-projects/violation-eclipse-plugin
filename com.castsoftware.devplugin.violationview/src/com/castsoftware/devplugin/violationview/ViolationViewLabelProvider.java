package com.castsoftware.devplugin.violationview;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.castsoftware.devplugin.commonui.PropertyTableLabelProvider;
import com.castsoftware.devplugin.core.model.Violation;

public class ViolationViewLabelProvider extends PropertyTableLabelProvider implements  ITableColorProvider
{

	public ViolationViewLabelProvider(TableViewer aTableViewer)
	{
		super(aTableViewer);
	}


	public Color getBackground(Object element, int columnIndex)
	{
		return null;
	}

	public Color getForeground(Object element, int columnIndex)
	{		
		if (ViolationOpenerManager.getSingleton().isViolationSupported((Violation) element)==false){
			return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
		} else {
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		}
	}

}
