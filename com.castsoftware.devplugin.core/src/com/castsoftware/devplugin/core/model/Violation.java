package com.castsoftware.devplugin.core.model;

import java.util.List;

import com.castsoftware.devplugin.commoncore.AbstractMapModel;
import com.castsoftware.ds.entity.ComplexityType;

public class Violation extends AbstractMapModel {
	
	private boolean fullDetail;

	private String fullName;

	private String name;
	
	private int metricId;
	
	private int objectId;
	
	private int lastSnapId;
	
	private int prevSnapId;
	
	private ComplexityType complexity;
	
	private String critical;
	
	private String diagName;
	
	private String comment;

	private String newViolation;
	
	private Integer typeId;
	
	private String typeName;
	
	private Integer priority;
	
	private int moduleId;
	
	private String moduleName;
	
	private int applicationId;
	
	private String appName;
	
	private String objectStatus;
	
	private List<String> metricValues;
	
	public boolean isFullDetail() {
		return fullDetail;
	}

	public void setFullDetail(boolean fullDetail) {
		this.fullDetail = fullDetail;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullName()
	{
		return fullName;
	}
	
	public int getMetricId()
	{
		return metricId;
	}
	
	public void setMetricId(int metricId) {
		this.metricId = metricId;
	}

	public int getObjectId() {
		return objectId;
	}
	
	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

	public int getLastSnapId() {
		return lastSnapId;
	}

	public void setLastSnapId(int lastSnapId) {
		this.lastSnapId = lastSnapId;
	}

	public int getPrevSnapId() {
		return prevSnapId;
	}

	public void setPrevSnapId(int prevSnapId) {
		this.prevSnapId = prevSnapId;
	}

	public ComplexityType getComplexity()
	{
		return complexity;
	}
	
	public void setComplexity(ComplexityType complexity) {
		this.complexity = complexity;
	}

	public String getCritical()
	{
		return critical;
	}
	
	public String getDiagName()
	{
		return diagName;
	}
	
	public void setCritical(String crit) {
		this.critical = crit;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}

	public String getComment()
	{
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getViolationStatus()
	{
		return newViolation;
	}
	
	public void setViolationStatus(String newViolat)
	{
		newViolation = newViolat;
	}
	
	public Integer getTypeId()
	{
		return typeId;
	}
	
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName()
	{
		return moduleName;
	}
	
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * Added/Deleted/Changed and Unchanged object
	 */
	public String getObjectStatus()
	{
		return objectStatus;
	}
	
	public void setObjectStatus(String objectStatus) {
		this.objectStatus = objectStatus;
	}
	
	public List<String> getMetricValues() {
		return metricValues;
	}

	public void setMetricValues(List<String> metricValues) {
		this.metricValues = metricValues;
	}
	
	public String getAppName()
	{
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
}
