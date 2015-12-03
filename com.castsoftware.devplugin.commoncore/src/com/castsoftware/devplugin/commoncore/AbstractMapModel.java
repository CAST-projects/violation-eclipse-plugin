package com.castsoftware.devplugin.commoncore;

import java.util.Map;

public abstract class AbstractMapModel extends AbstractModel
{

	protected Map<String, Object> itsMap;

	public void setMap(Map<String, Object> aMap)
	{
		itsMap = aMap;
	}

}
