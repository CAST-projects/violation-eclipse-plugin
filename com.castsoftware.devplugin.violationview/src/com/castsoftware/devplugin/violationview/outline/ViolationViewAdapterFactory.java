package com.castsoftware.devplugin.violationview.outline;

import org.eclipse.core.runtime.IAdapterFactory;

import com.castsoftware.devplugin.core.provider.CentralObjectProvider;
import com.castsoftware.devplugin.violationview.ViolationView;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public class ViolationViewAdapterFactory implements IAdapterFactory
{

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object aAdaptableObject, Class aAdapterType)
	{
		if (aAdaptableObject instanceof ViolationView)
		{
			ViolationView view = (ViolationView) aAdaptableObject;
			if(aAdapterType == IContentOutlinePage.class)
			{
				CentralObjectProvider p=view.getProvider();
				return new ViolationOutline(p);
			}
			
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList()
	{
		return new Class[]{ViolationView.class};
	}

}
