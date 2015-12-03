package com.castsoftware.devplugin.cbprefpage;

import com.castsoftware.devplugin.core.provider.CentralObjectProviderRegistry;
import com.castsoftware.devplugin.core.provider.MapModelFetcher;
import com.castsoftware.devplugin.core.provider.Registrer;

public class ProviderRegistrer implements Registrer
{

	public void initializeProvider(CentralObjectProviderRegistry aCentralObjectProviderRegistry)
	{
		CASTPrefStore st=CASTPrefStore.getInstance();
		MapModelFetcher f=new MapModelFetcher(st.getWebServicesURL(), st.getPortalURL());
		
		aCentralObjectProviderRegistry.putProvider(null, f);
	}

}
