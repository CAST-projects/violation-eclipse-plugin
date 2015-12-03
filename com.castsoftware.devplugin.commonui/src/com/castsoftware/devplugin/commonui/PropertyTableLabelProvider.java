package com.castsoftware.devplugin.commonui;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TableColumn;

import com.castsoftware.devplugin.commoncore.ReflectionMgr;

public class PropertyTableLabelProvider extends BaseLabelProvider implements ITableLabelProvider
{
	private TableViewer itsTableViewer;
	
	public PropertyTableLabelProvider(TableViewer aTableViewer)
	{
		super();
		itsTableViewer = aTableViewer;
	}

	public Image getColumnImage(Object aElement, int aColumnIndex)
	{
		return null;
	}

	public String getColumnText(Object aElement, int aColumnIndex)
	{
		TableColumn tc=itsTableViewer.getTable().getColumns()[aColumnIndex];
		String prop=(String)tc.getData("property");
		
		if(prop!=null)
		{
			Object o=ReflectionMgr.getSingleton().findObjectKeyPathValue(aElement, prop);
			if(o != null)
				return o.toString();
		}
		return null;
	}

}
