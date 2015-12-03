package com.castsoftware.devplugin.commonui;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckable;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class TreeViewerLeafSelectionManager implements ICheckStateListener {

	public void checkStateChanged(CheckStateChangedEvent aEvent) {
		ICheckable theCheckable=aEvent.getCheckable();

		if (theCheckable instanceof CheckboxTreeViewer) {
			CheckboxTreeViewer viewer = (CheckboxTreeViewer) theCheckable;
			viewer.getControl().setRedraw(false);
			try
			{
			Object elt=aEvent.getElement();
			boolean state=aEvent.getChecked();

			viewer.setGrayChecked(elt, false);
			viewer.setChecked(elt, state);

			changeChildrenState(viewer,elt,state);
			changeParentState(viewer,elt,state);
			}
			finally
			{
				viewer.getControl().setRedraw(true);
			}
		}

	}

	private void changeParentState(CheckboxTreeViewer aViewer, Object aElt,	boolean aState) {
		boolean grayed=aViewer.getGrayed(aElt);
		ITreeContentProvider theProvider=(ITreeContentProvider) aViewer.getContentProvider();
		Object parent=theProvider.getParent(aElt);

		if(parent != null)
		{
			if(grayed)
			{
				aViewer.setChecked(parent, false);
				aViewer.setGrayChecked(parent, true);
			}
			else
			{
				Object[] children=theProvider.getChildren(parent);
				boolean allTheSame=true;
				
				for(Object o:children)
				{
					boolean g=aViewer.getGrayed(o);
					boolean s=aViewer.getChecked(o);
					
					if(g || (s != aState))
					{
						allTheSame=false;
						break;
					}
				}
				if(allTheSame)
				{
					aViewer.setGrayChecked(parent, false);
					aViewer.setChecked(parent, aState);					
				}
				else
				{
					aViewer.setChecked(parent, false);
					aViewer.setGrayChecked(parent, true);
				}
			}
			changeParentState(aViewer,parent,aState);
		}

	}

	private void changeChildrenState(CheckboxTreeViewer viewer, Object aElt,boolean aState) 
	{
		ITreeContentProvider theProvider=(ITreeContentProvider) viewer.getContentProvider();
		if(theProvider.hasChildren(aElt))
		{
			Object[] children= theProvider.getChildren(aElt);
			if(children!=null)
			{
				for(Object o:children)
				{
					viewer.setGrayChecked(o, false);
					viewer.setChecked(o, aState);
					changeChildrenState(viewer, o, aState);
				}
			}
		}
	}
}