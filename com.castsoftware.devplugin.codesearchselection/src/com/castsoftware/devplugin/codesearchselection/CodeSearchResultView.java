package com.castsoftware.devplugin.codesearchselection;


import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.castsoftware.devplugin.querybytype.IQueryResultAction;
import com.swtdesigner.ResourceManager;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class CodeSearchResultView extends ViewPart {
	private TableViewer viewer;
	private Action itsDoubleClickAction;
	public final static String ID="com.castsoftware.devplugin.codesearchselection.CodeSearchResultView";

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			if (obj instanceof IQueryResultAction)
			{
				IQueryResultAction action = (IQueryResultAction) obj;
				return action.getName();
				
			}
			return null;
		}
		public Image getColumnImage(Object obj, int index) {
			return null;
		}
		public Image getImage(Object obj) {
			return null;
		}
	}

	/**
	 * The constructor.
	 */
	public CodeSearchResultView() {
	}

	protected void computeStatus()
	{
		itsDoubleClickAction.setEnabled(viewer!=null && (!viewer.getSelection().isEmpty()));
	}
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(final SelectionChangedEvent event)
			{
				computeStatus();
			}
		});
	viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		makeActions();
		hookDoubleClickAction();
		contributeToActionBars();
		computeStatus();
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "com.castsoftware.devplugin.help.CAST_artifacts");

	}


	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		final IToolBarManager toolBarManager = bars.getToolBarManager();
		fillLocalToolBar(toolBarManager);

		toolBarManager.add(itsDoubleClickAction);
	}


	
	private void fillLocalToolBar(IToolBarManager manager) {
	}

	private void makeActions() {
		
		
		itsDoubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				
				//showMessage("Double-click detected on "+obj.toString());
				
				
				if (obj instanceof IQueryResultAction) {
					IQueryResultAction action = (IQueryResultAction) obj;
					try
					{
						action.run();
					}
					catch (CoreException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				}
			};
		itsDoubleClickAction.setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/displayincode.png"));
		itsDoubleClickAction.setText("View Code");
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				itsDoubleClickAction.run();
			}
		});
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	protected void reloadTable(List<IQueryResultAction> l)
	{
		if (viewer != null)
		{
			viewer.setInput(l);
			computeStatus();
		}
	}
	
	public static void showResults(List<IQueryResultAction> l)
	{
		IViewPart thePart;
		try
		{
			thePart = Activator.getWorkbenchPage().showView(ID);
			if (thePart instanceof CodeSearchResultView)
			{
				CodeSearchResultView theResultView = (CodeSearchResultView) thePart;
				theResultView.reloadTable(l);
			}
		} catch (PartInitException e)
		{
			MessageDialog.openError(null, "Exception", e.getMessage());
		}

	}
}