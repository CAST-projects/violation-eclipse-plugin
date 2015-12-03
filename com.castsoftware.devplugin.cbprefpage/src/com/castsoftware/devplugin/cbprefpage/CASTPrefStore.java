package com.castsoftware.devplugin.cbprefpage;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.dialogs.PreferencesUtil;


final public class CASTPrefStore 
{
	private static final String INITIALIZED = "INITIALIZED";
	private static CASTPrefStore instance = null;
	private IPreferenceStore store ;

	protected CASTPrefStore()
	{
		store = Activator.getDefault().getPreferenceStore();

		store.setDefault(INITIALIZED, false);

	}

	public static synchronized CASTPrefStore getInstance()
	{
		if (instance == null)
		{
			instance = new CASTPrefStore();
		}
		return instance;
	}

	private void haveUserInitialize()
	{
		if(store.getBoolean(INITIALIZED))
			return;
		String[] pages=new String[]{CASTPreferenceCenBaseConnection.class.getName()};
		PreferenceDialog dlg=PreferencesUtil.createPreferenceDialogOn(null, pages[0], pages, null);
	    
	    dlg.open();
	}
	
	public String getString(String aName)
	{
		haveUserInitialize();
		return store.getString(aName);
	}

	public String getWebServicesURL() {
		return getString(CASTPreferenceConstants.CAST_WS_URL);
	}
	public void setWebServicesURL(String wsUrl) {
		store.setValue(CASTPreferenceConstants.CAST_WS_URL, wsUrl);
	}

	public String getPortalURL(){
		return getString(CASTPreferenceConstants.CAST_PORTAL_URL);
	}
	
	public void setPortalURL(String portalURL){
	store.setValue(CASTPreferenceConstants.CAST_PORTAL_URL, portalURL);
	}

	public boolean isInitialized(){
		return store.getBoolean(INITIALIZED);
	}

	public void setInitialized(boolean b)
	{
		store.setValue(INITIALIZED,b);
	}
}
