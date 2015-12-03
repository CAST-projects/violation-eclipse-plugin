package com.castsoftware.devplugin.core.model;

import java.util.List;

import com.castsoftware.ds.entity.Description;
import com.castsoftware.ds.entity.Metric;
import com.castsoftware.ds.entity.MetricDescType;

public class MetricHandler {

	private static final Integer ONE = new Integer(1);
	private Metric metric;
	
	public Metric getMetric() {
		return metric;
	}

	public MetricHandler(Metric met) {
		metric = met;
	}
	
	public Integer getMetricId()
	{
		return metric.getMetricId();
	}
	
	public boolean isCritical()
	{
		if(ONE.equals(metric.getCritical())){
			return true;
		}
		return false;
	}
	
	public String getRationale()
	{
		return getDescription(MetricDescType.RATIONALE);
	}
	
	public String getReference()
	{
		return getDescription(MetricDescType.REFERENCE);
	}
	
	public String getDescription()
	{
		return getDescription(MetricDescType.DESCRIPTION);
	}
	
	public String getRemediation()
	{
		return getDescription(MetricDescType.REMEDIATION);
	}
	
	public String getSample()
	{
		return getDescription(MetricDescType.SAMPLE);
	}
	
	public String getRemediationSample()
	{
		return getDescription(MetricDescType.REMEDIATION_SAMPLE);
	}
	
	public String getOutput()
	{
		return getDescription(MetricDescType.OUTPUT);
	}
	
	public String getTotal()
	{
		return getDescription(MetricDescType.TOTAL);
	}
	
	private String getDescription(MetricDescType type)
	{
		if(type == null){
			return null;
		}
		List<Description> descriptions = metric.getDescription();
		for(Description desc : descriptions)
		{
			if(desc != null && type.equals(desc.getMetricDescType()))
			{
				return desc.getMetricDesc();
			}
		}
		return null;
	}
}
