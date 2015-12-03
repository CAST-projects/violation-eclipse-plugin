package com.castsoftware.devplugin.core.model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.castsoftware.ds.entity.MetricResponse;
import com.castsoftware.ds.entity.ObjectResponse;
import com.castsoftware.ds.entity.PortfolioTechnoResponse;
import com.castsoftware.ds.entity.ViolationObjectResponse;

public class JAXBPluginFactory {

	private Unmarshaller unmarshallViolationResp; 
	
	private Unmarshaller unmarshallPortfolioResp;
	
	private Unmarshaller unmarshallMetricResp;
	
	private Unmarshaller unmarshallObject;
	
	public Unmarshaller getUnmarshallerViolationResp() 
	{
		if(unmarshallViolationResp != null) 
		{
			return unmarshallViolationResp;
		}
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance(ViolationObjectResponse.class);
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			return unMarshaller;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Unmarshaller getUnmarshallerPortfolioTechnoResp() 
	{
		if(unmarshallPortfolioResp != null) 
		{
			return unmarshallPortfolioResp;
		}
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance(PortfolioTechnoResponse.class);
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			return unMarshaller;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Unmarshaller getUnmarshallerMetricResp() 
	{
		if(unmarshallMetricResp != null) 
		{
			return unmarshallMetricResp;
		}
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance(MetricResponse.class);
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			return unMarshaller;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Unmarshaller getUnmarshallerObjectResp() 
	{
		if(unmarshallObject != null) 
		{
			return unmarshallObject;
		}
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance(ObjectResponse.class);
			Unmarshaller unMarshaller = jaxbContext.createUnmarshaller();
			return unMarshaller;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
