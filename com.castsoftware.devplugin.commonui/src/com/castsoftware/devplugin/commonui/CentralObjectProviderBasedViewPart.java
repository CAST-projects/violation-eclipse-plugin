package com.castsoftware.devplugin.commonui;

import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.castsoftware.devplugin.core.provider.CentralObjectProvider;
import com.castsoftware.devplugin.core.provider.CentralObjectProviderRegistry;

public abstract class CentralObjectProviderBasedViewPart extends ViewPart{
	
	private CentralObjectProvider itsProvider;

	protected void initFromString(String secondaryId) {
		CentralObjectProviderRegistry reg=CentralObjectProviderRegistry.getSSingleton();
		itsProvider=reg.getProvider(secondaryId);
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		initFromString(site.getSecondaryId());
	}

	public CentralObjectProvider getProvider() {
		if(itsProvider==null)
		{
			CentralObjectProviderRegistry reg=CentralObjectProviderRegistry.getSSingleton();
			itsProvider=reg.getProvider(null);

		}
		return itsProvider;
	}

}
