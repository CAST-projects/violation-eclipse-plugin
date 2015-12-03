package com.castsoftware.devplugin.diagtreeview;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.castsoftware.ds.entity.Metric;

public class DiagTreeProvider implements ITreeContentProvider {

	public Object[] getChildren(Object element) {
		if(element == null){
			return null;
		}
		return ((Metric) element).getChildMetric().toArray();
	}

	public Object getParent(Object element) {
		//return ((Node) element).getParent();
		return null;
	}

	public boolean hasChildren(Object element) {
		if(element != null){
			List<Metric> childs = ((Metric)element).getChildMetric();
			if(childs.size() > 0){
				return true;
			}
		}
		return false;
	}

	public Object[] getElements(Object inputElement) {
		return (Object[]) inputElement;
	}

	public void dispose() {
		// unused
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// unused
	}
}
