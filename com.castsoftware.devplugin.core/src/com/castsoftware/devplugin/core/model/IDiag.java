package com.castsoftware.devplugin.core.model;

public interface IDiag {

	public abstract String getDescription(DiagDescName aK);

	public abstract int getID();

	public abstract int getMetricGroup();

	public abstract String getName();

	public abstract int hashCode();

	public abstract boolean isOwningChildren();

	public abstract Diagnostic getDiagnostic();

}