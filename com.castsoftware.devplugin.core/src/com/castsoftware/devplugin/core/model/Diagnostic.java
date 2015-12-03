package com.castsoftware.devplugin.core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.castsoftware.devplugin.commoncore.AbstractMapModel;


public class Diagnostic extends AbstractMapModel implements IDiag{

	protected int itsID;
	protected Map<DiagDescName, String> itsDescriptions = new HashMap<DiagDescName, String>();
	
	protected List<Diagnostic> itsChildrenList = new LinkedList<Diagnostic>();
	protected Diagnostic[] itsChildrenArray;
	protected Diagnostic itsParent;
	protected List<Diagnostic> itsParentList = new ArrayList<Diagnostic>();
	protected boolean itsIsCritical;


	public boolean isCritical()
	{
		return itsIsCritical;
	}

	public void addCriticalFlag(boolean flg)
	{
		itsIsCritical |= flg;
	}
	
	public void setID(int aId) {
		itsID = aId;
	}

	public int getID() {
		return itsID;
	}


	public void addDescription(DiagDescName k,String v)
	{
		itsDescriptions.put(k, v);
	}
	
	public String getDescription(DiagDescName k)
	{
		return itsDescriptions.get(k);
	}
	
	public String getDescription()
	{
		return getDescription(DiagDescName.Description);
	}
	public String getOutput()
	{
		return getDescription(DiagDescName.Output);
	}
	public String getRationale()
	{
		return getDescription(DiagDescName.Rationale);
	}
	public String getReference()
	{
		return getDescription(DiagDescName.Reference);
	}
	public String getRemediation()
	{
		return getDescription(DiagDescName.Remediation);
	}
	public String getRemediationSample()
	{
		return getDescription(DiagDescName.RemediationSample);
	}
	public String getSample()
	{
		return getDescription(DiagDescName.Sample);
	}
	public String getTotal()
	{
		return getDescription(DiagDescName.Total);
	}
	
	@Override
	public void setMap(Map<String, Object> aMap) {
		super.setMap(aMap);
		setID(((Number) aMap.get("METRIC_ID")).intValue());
	}



	public void computeChildrenArray() {
		itsChildrenArray = new Diagnostic[itsChildrenList.size()];
		int i = 0;
		for (Diagnostic c : itsChildrenList) {
			itsChildrenArray[i++] = c;
		}
	}
	
	

	public Diagnostic[] getChildrenArray() {
		return itsChildrenArray;
	}


	public List<Diagnostic> getChildrenList() {
		return itsChildrenList;
	}

	public void addChild(Diagnostic aChild) {
		itsChildrenList.add(aChild);
	}

	public void removeChild(Diagnostic aChild) {
		itsChildrenList.remove(aChild);
	}

	public String getName() {
		return itsMap.get("METRIC_NAME").toString();
	}

	public int getMetricGroup() {
		return ((Number) itsMap.get("METRIC_GROUP")).intValue();
	}

	public Diagnostic getParent() {
		return itsParent;
	}

	public void addParent(Diagnostic aParent) {
		if(itsParentList.size()==0)
			itsParent = aParent;
		else
			itsParent=null;
		
		itsParentList.add(aParent);
	}

	public boolean isOwningChildren() {
		return getMetricGroup() != 1;
	}

	@Override
	public int hashCode() {
		return itsID;
	}

	public Diagnostic getDiagnostic() {
		return this;
	}
}
