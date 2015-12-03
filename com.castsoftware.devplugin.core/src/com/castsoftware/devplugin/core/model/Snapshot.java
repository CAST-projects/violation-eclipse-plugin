package com.castsoftware.devplugin.core.model;

import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import com.castsoftware.devplugin.commoncore.AbstractMapModel;

public class Snapshot extends AbstractMapModel {

	private int itsID;

	protected String itsName;

	private int itsStatus;

	private Date itsDate;

	private int dogetID() {
		return ((Number) itsMap.get("SNAPSHOT_ID")).intValue();
	}

	private String dogetName() {
		return itsMap.get("SNAPSHOT_NAME").toString();
	}

	@Override
	public void setMap(Map<String, Object> map) {
		super.setMap(map);
		itsID = dogetID();
		itsName = dogetName();
		itsDate = (Date) itsMap.get("SNAPSHOT_DATE");
	}

	public int getID() {
		return itsID;
	}

	public String getName() {
		return itsName;
	}

	public int getStatus() {
		return itsStatus;
	}


	public Date getDate() {
		return itsDate;
	}

	public String getDateString() {
		String ret = DateFormat.getDateTimeInstance().format(itsDate);

		return ret;
	}

}
