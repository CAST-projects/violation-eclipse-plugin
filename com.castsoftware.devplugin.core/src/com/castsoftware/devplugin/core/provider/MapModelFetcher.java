package com.castsoftware.devplugin.core.provider;

import static com.castsoftware.devplugin.core.provider.ProviderUtils.getWebservicesUrlAndCheck;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.castsoftware.devplugin.core.model.JAXBPluginFactory;
import com.castsoftware.devplugin.core.model.MetricHandler;
import com.castsoftware.devplugin.core.model.Violation;
import com.castsoftware.ds.entity.ApplicationEntity;
import com.castsoftware.ds.entity.BaseResponse;
import com.castsoftware.ds.entity.BaseTechnology;
import com.castsoftware.ds.entity.EntityType;
import com.castsoftware.ds.entity.ErrorElement;
import com.castsoftware.ds.entity.HelpResponse;
import com.castsoftware.ds.entity.Metric;
import com.castsoftware.ds.entity.MetricResponse;
import com.castsoftware.ds.entity.ModuleEntity;
import com.castsoftware.ds.entity.ObjectDetailInfo;
import com.castsoftware.ds.entity.ObjectResponse;
import com.castsoftware.ds.entity.ObjectValue;
import com.castsoftware.ds.entity.OrganizationEntity;
import com.castsoftware.ds.entity.PortfolioTechnoResponse;
import com.castsoftware.ds.entity.ProjectEntity;
import com.castsoftware.ds.entity.SnapshotInfo;
import com.castsoftware.ds.entity.SubTechnology;
import com.castsoftware.ds.entity.SystemEntity;
import com.castsoftware.ds.entity.TechnologyEntity;
import com.castsoftware.ds.entity.ViolationObjectResponse;
import com.castsoftware.ds.entity.ViolationShortInfo;

public class MapModelFetcher implements CentralObjectProvider
{
	public static final String NEW_VIOLATION_TEXT = "new violation";
	public static final String OLD_VIOLATION_TEXT = "old violation";
	public static final String CRITICAL_TEXT = "yes";
	public static final String NOT_CRITICAL_TEXT = "no";
	private String itsPortalURL;
	private String webservicesUrl;

	private List<ApplicationEntity> itsApplications;
	private List<TechnologyEntity> itsTechnologies;
	private Map<Integer, ProjectEntity> itsModulesDesc;
	private Map<Integer, ApplicationEntity> itsApplicationsLastSnap;
	private Map<Integer, BaseTechnology> itsTechnologiesDesc;
	private Map<Integer, Metric> itsMetricsShortDesc;
	private List<Metric> itsMetricsTree;
	
	public MapModelFetcher(String wsUrl, String aPortalURL)
	{
		super();
		itsPortalURL = aPortalURL;
		webservicesUrl = wsUrl;
	}

	public void setWebservicesUrl(String webservicesUrl) {
		this.webservicesUrl = webservicesUrl;
	}

	private synchronized List<ApplicationEntity> fetchAppsAndModules() throws ProviderException
	{
		List<ApplicationEntity> applications = new ArrayList<ApplicationEntity>();
		String urlString = getWebservicesUrlAndCheck(webservicesUrl) + "portfoliotree";
		try {
			Unmarshaller unMarshaller = new JAXBPluginFactory().getUnmarshallerPortfolioTechnoResp();

			URL url = new URL(urlString); 
			PortfolioTechnoResponse response = (PortfolioTechnoResponse)unMarshaller.unmarshal(url);
            
			boolean isResponseOK = checkResponseOKFromWebservices(response);
			if(!isResponseOK){
				throw new ProviderException("Exception while fetching data from web service :" + urlString);
			}
			if(response.getOrganization() != null)
			{
				List<OrganizationEntity> orgs = response.getOrganization();
				itsModulesDesc = new HashMap<Integer, ProjectEntity>();
				itsApplicationsLastSnap = new HashMap<Integer, ApplicationEntity>();
				
				for(OrganizationEntity org : orgs)
				{
					// corporate
					if(org != null && EntityType.CORPORATE.equals(org.getType()))
					{
						List<SystemEntity> systems = org.getSystem();
						for(SystemEntity sys : systems)
						{
							applications.addAll(sys.getApplication());
						}
						for(ApplicationEntity app : applications){
							Integer appId = app.getEntityId();
							List<ModuleEntity> modules = app.getModule();
							if(modules != null){
								for(ModuleEntity mod : modules){
									mod.setParentId(appId);
									itsModulesDesc.put(mod.getEntityId(), mod);
								}
							}
							List<SnapshotInfo> snapshots = app.getSnapshot();
							if(snapshots != null && snapshots.size() > 0){
								SnapshotInfo lastSnap = snapshots.get(0);
								itsApplicationsLastSnap.put(lastSnap.getSnapshotId(), app);
							}
						}
						return applications;
					}
				}
			}
		} catch (Throwable ex) {
			throw new ProviderException("Exception while fetching data from web service :" + urlString);
		}

		return applications;
	}
	
	public synchronized List<TechnologyEntity> fetchTechnologies() throws ProviderException
	{
		List<TechnologyEntity> technos = new ArrayList<TechnologyEntity>();
		String urlString = getWebservicesUrlAndCheck(webservicesUrl) + "technotree";
		try {
			Unmarshaller unMarshaller = new JAXBPluginFactory().getUnmarshallerPortfolioTechnoResp();

			URL url = new URL(urlString); 
			PortfolioTechnoResponse response = (PortfolioTechnoResponse)unMarshaller.unmarshal(url);
			boolean isResponseOK = checkResponseOKFromWebservices(response);
			if(!isResponseOK){
				throw new ProviderException("Exception while fetching data from web service :" + urlString);
			}
			if(response.getTechnology() != null)
			{
				technos = response.getTechnology();
				itsTechnologiesDesc = new HashMap<Integer, BaseTechnology>();
				for(TechnologyEntity techno : technos){
					TechnologyEntity technoDesc = new TechnologyEntity();
					technoDesc.setTechnoId(techno.getTechnoId());
					technoDesc.setName(techno.getName());
					Integer technoId = techno.getTechnoId();
					itsTechnologiesDesc.put(techno.getTechnoId(), technoDesc);
					List<SubTechnology> subTechnos = techno.getSubTechno();
					if(subTechnos != null && techno.getSubTechno().size() > 0){
						for(SubTechnology subTechno : subTechnos){
							subTechno.setParentId(technoId);
							itsTechnologiesDesc.put(subTechno.getTechnoId(), subTechno);
						}
					}
				}
			}
		} catch (Throwable ex) {
			throw new ProviderException("Exception while fetching data from web service :" + urlString);
		}

		return technos;
	}
	
	public synchronized List<Violation> fetchLatestViolationsFromActionPlan(List<ProjectEntity> checkedAppModules, 
			List<BaseTechnology> technos, MapModelFetcherNotification n) throws ProviderException
	{
		List<Violation> violations = new ArrayList<Violation>();
		if(checkedAppModules == null || checkedAppModules.size() < 1) {
			throw new ProviderException("One module or application must be chosen");
		}
		String appModuleStr = "";
		for(ProjectEntity module : checkedAppModules){
			int appModId = module.getEntityId();
			if(appModuleStr.length() == 0){
				appModuleStr = appModId + "";
			} else {
				appModuleStr = appModuleStr + "_" + appModId;
			}
		}
		String urlString = getWebservicesUrlAndCheck(webservicesUrl) + "actionplan/" + appModuleStr;
		
		String technoStr = "";
		if(technos != null){
			for(BaseTechnology techno : technos){
				Integer technoId = techno.getTechnoId();
				if(technoStr.length() == 0){
					technoStr = technoId + "";
				} else {
					technoStr = technoStr + "_" + technoId;
				}
			}
		}
		if(technoStr.length() > 0){
			urlString = urlString + "/techno/" + technoStr;
		}
		// get all metric descriptions
		getAppsAndModules();
		fecthAllMetricShortDescription();
		getTechnologiyTree();
		
		try {
			Unmarshaller unMarshaller = new JAXBPluginFactory().getUnmarshallerViolationResp();

			URL url = new URL(urlString); 
			ViolationObjectResponse response = (ViolationObjectResponse)unMarshaller.unmarshal(url);
            Integer metricId = null;
			boolean isResponseOK = checkResponseOKFromWebservices(response);
			if(!isResponseOK){
				throw new ProviderException("Exception while fetching data from web service :" + urlString);
			}
			if(response.getViolationObjects() != null) {
				// set the last snapshotId

				List<ViolationShortInfo> allViolation = response.getViolationObjects().getObject();
				Integer oneInteger = new Integer(1);
				for(ViolationShortInfo violationSnap : allViolation) {
					Violation vl = new Violation();
					vl.setFullName(violationSnap.getFullName());
					metricId = violationSnap.getMetricId();
					vl.setMetricId(metricId);
					Metric metric = itsMetricsShortDesc.get(metricId);
					if(metric != null) {
						vl.setDiagName(metric.getName());
						if(oneInteger.equals(metric.getCritical())){
							vl.setCritical(CRITICAL_TEXT);							
						} else {
							vl.setCritical(NOT_CRITICAL_TEXT);
						}
					}
					
					vl.setComment(violationSnap.getCommentAp());
					vl.setObjectId(violationSnap.getObjectId());
					Integer snapshotId = violationSnap.getSnapshotId();
					vl.setLastSnapId(snapshotId);
					ApplicationEntity application = itsApplicationsLastSnap.get(snapshotId);
					if(application != null){
						vl.setApplicationId(application.getEntityId());
						vl.setAppName(application.getName());
					}
					vl.setPriority(violationSnap.getPriority());
					Integer moduleId = violationSnap.getModuleId();
					vl.setModuleId(moduleId);
					ProjectEntity modOrAppDesc = itsModulesDesc.get(moduleId);
					if(modOrAppDesc != null){
						vl.setModuleName(modOrAppDesc.getName());
					}
					
					if(violationSnap.getLastObjId() == null){
						vl.setViolationStatus(NEW_VIOLATION_TEXT);
					} else {
						vl.setViolationStatus(OLD_VIOLATION_TEXT);
					}
					
					// technology type
					if(violationSnap.getTypeId() != null && itsTechnologiesDesc != null){
						vl.setTypeId(violationSnap.getTypeId());
						BaseTechnology techno = itsTechnologiesDesc.get(violationSnap.getTypeId());
						if(techno != null){
							vl.setTypeName(techno.getName());							
						}
					}
					violations.add(vl);
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			throw new ProviderException("Exception while fetching data from web service:" + urlString);
		}
		return violations;
	}
	
	private synchronized void fecthAllMetricShortDescription() throws ProviderException {
		if(itsMetricsShortDesc == null){
			itsMetricsShortDesc = new HashMap<Integer, Metric>();
			String urlString = getWebservicesUrlAndCheck(webservicesUrl) + "metricdesc";
			try {
				Unmarshaller unMarshaller = new JAXBPluginFactory().getUnmarshallerMetricResp();
	
				URL url = new URL(urlString); 
				MetricResponse response = (MetricResponse)unMarshaller.unmarshal(url);
				boolean isResponseOK = checkResponseOKFromWebservices(response);
				if(!isResponseOK){
					throw new ProviderException("Exception while fetching data from web service :" + urlString);
				}
				if(response.getMetrics() != null)
				{
					List<Metric> metrics = response.getMetrics().getMetric();
					for(Metric met : metrics)
					{
						itsMetricsShortDesc.put(met.getMetricId(), met);
					}
				}
			} catch (Throwable ex) {
				throw new ProviderException("Exception while fetching data from web service :" + urlString);
			}
		}
	}
	
	private List<Metric> fetchMetricsTree() throws ProviderException {
		List<Metric> metrics = new ArrayList<Metric>();
		String urlString = getWebservicesUrlAndCheck(webservicesUrl) + "metrictree";
		try {
			Unmarshaller unMarshaller = new JAXBPluginFactory().getUnmarshallerMetricResp();

			URL url = new URL(urlString); 
			MetricResponse response = (MetricResponse)unMarshaller.unmarshal(url);
			boolean isResponseOK = checkResponseOKFromWebservices(response);
			if(!isResponseOK){
				throw new ProviderException("Exception while fetching data from web service :" + urlString);
			}
			if(response.getMetrics() != null)
			{
				metrics = response.getMetrics().getMetric();
			}
		} catch (Throwable ex) {
			throw new ProviderException("Exception while fetching data from web service :" + urlString);
		}

		return metrics;
	}

	public synchronized void setViolationDetails(Violation violation) throws ProviderException
	{
		if(violation.isFullDetail()){
			return;
		}
		int metricId = violation.getMetricId();
		int objectId = violation.getObjectId();

		String urlString = getWebservicesUrlAndCheck(webservicesUrl) + "object/" + objectId 
			+ "/snapshot/" + violation.getLastSnapId() + "/metric/" + metricId;

		try {
			Unmarshaller unMarshaller = new JAXBPluginFactory().getUnmarshallerObjectResp();

			URL url = new URL(urlString); 
			ObjectResponse response = (ObjectResponse)unMarshaller.unmarshal(url);
			boolean isResponseOK = checkResponseOKFromWebservices(response);
			
			if(isResponseOK && response.getObjectDetail() != null)
			{
				ObjectDetailInfo detail = response.getObjectDetail();
				violation.setName(detail.getName());
				violation.setComplexity(detail.getComplexity());
				Integer checksum = detail.getChecksum();
				Integer prevChecksum = detail.getPrevChecksum();
				// object status
				if(checksum != null && prevChecksum != null){
					if(checksum.equals(prevChecksum)){
						violation.setObjectStatus("unchanged object");
					} else {
						violation.setObjectStatus("updated object");
					}
				} else if(checksum != null && prevChecksum == null){
					violation.setObjectStatus("added object");
				} 
				// object metric values or fullName
				if(detail != null && detail.getObjValues() != null){
					List<String> values = new ArrayList<String>();
					List<ObjectValue> objValues = detail.getObjValues();
					for(ObjectValue objVal : objValues){
						if(objVal != null && objVal.getValueString() != null){
							values.add(objVal.getValueString());
						} else if(objVal != null && objVal.getValueFloat() != null){
							values.add(objVal.getValueFloat() + "");
						}							
					}
					violation.setMetricValues(values);
				}
				violation.setFullDetail(true);
			}
			
		} catch (Exception ex) {
			System.out.println("Exception while fetching data from web service :" + urlString);
			violation.setObjectStatus("Can't fetching details violation");
		}
	}

	public MetricHandler getDiagById(int anID) throws ProviderException
	{
		Metric metric = itsMetricsShortDesc.get(new Integer(anID));
		if(metric == null){
			return null;
		}
		return new MetricHandler(metric);
	}

	public String getPortalURL()
	{
		return itsPortalURL;
	}

	public static boolean test(String urlWS) throws ProviderException
	{
		try {
			URL url = new URL(urlWS);
		    HttpURLConnection.setFollowRedirects(false);
			Unmarshaller unMarshaller = new JAXBPluginFactory().getUnmarshallerPortfolioTechnoResp();
			PortfolioTechnoResponse response = (PortfolioTechnoResponse)unMarshaller.unmarshal(url);
			
			return checkResponseOKFromWebservices(response);

		} catch(MalformedURLException e){
			e.printStackTrace();
		} catch(JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}

	public synchronized List<ApplicationEntity> getAppsAndModules() throws ProviderException
	{
		if (itsApplications == null)
			itsApplications = fetchAppsAndModules();
		return itsApplications;

	}

	public synchronized List<TechnologyEntity> getTechnologiyTree() throws ProviderException
	{
		if (itsTechnologies == null)
			itsTechnologies = fetchTechnologies();
		return itsTechnologies;
	}

	public synchronized List<Metric> getMetricTree() throws ProviderException {
		if (itsMetricsTree == null)
			itsMetricsTree = fetchMetricsTree();
		return itsMetricsTree;
	}
	
	private static boolean checkResponseOKFromWebservices(BaseResponse response){
	    if(response != null){
	    	HelpResponse help = response.getHelpResponse();
	    	if(help != null && help.getErrors() != null){
	    		 List<ErrorElement> errors = help.getErrors().getError();
	    		 if(errors.size() > 0){
	    			 return false;
	    		 }
	    	}
	    	return true;
	    }
	    return false;
	}
}
