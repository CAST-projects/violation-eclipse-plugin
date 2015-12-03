/**
 * 
 */
package com.castsoftware.devplugin.commonui;

import org.eclipse.swt.graphics.Image;

import com.castsoftware.ds.entity.Metric;
import com.swtdesigner.ResourceManager;

public final class DiagImageProvider implements IImageProvider 
{
	public Image getImage(Object aObj) {
		if (aObj instanceof Metric) {
			Metric metric = (Metric) aObj;
			String thePath = metric != null && metric.getChildMetric().size() > 0 ? "icons/folder.png" : "icons/metric.png";
			Image img = ResourceManager.getPluginImage(Activator.getDefault(), thePath);
			return img;
		}
		return null;
	}
}