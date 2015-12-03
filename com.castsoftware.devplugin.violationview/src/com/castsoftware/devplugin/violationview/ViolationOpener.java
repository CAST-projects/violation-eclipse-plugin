package com.castsoftware.devplugin.violationview;

import com.castsoftware.devplugin.core.model.Violation;

public interface ViolationOpener
{
	public void openViolation(Violation v);
	public boolean isViolationSupported(Violation v);
}
