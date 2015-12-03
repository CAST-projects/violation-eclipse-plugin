package com.castsoftware.devplugin.core.provider;

import java.util.List;

import com.castsoftware.devplugin.core.model.MetricHandler;
import com.castsoftware.devplugin.core.model.Violation;
import com.castsoftware.ds.entity.ApplicationEntity;
import com.castsoftware.ds.entity.BaseTechnology;
import com.castsoftware.ds.entity.Metric;
import com.castsoftware.ds.entity.ProjectEntity;
import com.castsoftware.ds.entity.TechnologyEntity;

public interface CentralObjectProvider {

	String getPortalURL();

	MetricHandler getDiagById(int id) throws ProviderException;
	
	List<ApplicationEntity> getAppsAndModules() throws ProviderException;

	List<TechnologyEntity> getTechnologiyTree() throws ProviderException;

	List<Violation> fetchLatestViolationsFromActionPlan(List<ProjectEntity> checkedAppModules, List<BaseTechnology> technos, MapModelFetcherNotification n) throws ProviderException;

	void setViolationDetails(Violation aViolation) throws ProviderException;

	List<Metric> getMetricTree() throws ProviderException;
}
