package com.castsoftware.devplugin.diagtreeview;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import com.castsoftware.devplugin.commonui.CentralObjectProviderBasedViewPart;
import com.castsoftware.devplugin.commonui.DiagImageProvider;
import com.castsoftware.devplugin.commonui.IImageProvider;
import com.castsoftware.devplugin.core.provider.ProviderException;
import com.castsoftware.ds.entity.Metric;

public class DiagTreeView extends CentralObjectProviderBasedViewPart  {

	@Override
	public void createPartControl(Composite parent) {
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, "com.castsoftware.devplugin.help.CAST_diagnostic_tree");
		
		PatternFilter patternFilter = new PatternFilter();
		FilteredTree filteredTree = new FilteredTree(parent,
		   SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL,
		   patternFilter);
		filteredTree.setInitialText("type your filter text here");
		
		final TreeViewer treeViewer = filteredTree.getViewer();
		final IImageProvider sImageProvider = new DiagImageProvider();
		
		// Assign the label provider
		treeViewer.setLabelProvider(new LabelProvider()
		{
			@Override
			public Image getImage(Object element) {
				return sImageProvider.getImage(element);
			}
			
			@Override
			public String getText(Object aElement)
			{
				if(aElement == null){
					return null;
				}
				return ((Metric)aElement).getName();
			}
		});
		// Assign the content provider
		treeViewer.setContentProvider(new DiagTreeProvider());
		try {
			treeViewer.setInput(getProvider().getMetricTree().toArray());
		} catch(ProviderException e){
			MessageDialog.openError(getSite().getShell(), "Exception", e.getMessage());
		}
	}

	@Override
	public void setFocus() {
	}
}
