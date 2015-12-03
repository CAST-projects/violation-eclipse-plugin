package com.castsoftware.devplugin.core.model;

import java.util.ArrayList;
import java.util.List;
import com.castsoftware.devplugin.commoncore.AbstractMapModel;
import com.castsoftware.ds.entity.ApplicationEntity;

public class DSSSystem extends AbstractMapModel {

	protected List<ApplicationEntity> itsApps=new ArrayList<ApplicationEntity>();
		
	public String getName()
	{
		return itsMap.get("SYST_NAME").toString();
	}
	
	public int getObjectID() {
		return ((Number)itsMap.get("SYST_ID")).intValue();
	}

	
	public void addApplication(ApplicationEntity aO) {
		itsApps.add(aO);
	}

	public Object[] getApplicationsArray() {
		return itsApps.toArray();
	}
	

}
