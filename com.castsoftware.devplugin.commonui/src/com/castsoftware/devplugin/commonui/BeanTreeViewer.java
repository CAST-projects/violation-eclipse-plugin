package com.castsoftware.devplugin.commonui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.part.DrillDownAdapter;

import com.castsoftware.devplugin.commoncore.ReflectionMgr;

public class BeanTreeViewer extends Composite {

	private TreeViewer viewer;
	private Tree itsTree;
	private DrillDownAdapter drillDownAdapter;

	private Object itsRootBean;
	private String itsChildrenProperty;
	private String itsParentProperty;
	private String itsIsParentProperty;
	private String itsLabelProperty;

	private IImageProvider itsImageProvider;

	private PropertyChangeListener itsLabelListener = new PropertyChangeListener()
	{
		public void propertyChange(PropertyChangeEvent aEvt)
		{
			Object m=aEvt.getSource();
			viewer.refresh(m, true);
		}		
	};
	private PropertyChangeListener itsChildrenListener = new PropertyChangeListener()
	{
		public void propertyChange(PropertyChangeEvent aEvt)
		{
			Object[] oldv=(Object[]) aEvt.getOldValue();
			Object[] newv=(Object[]) aEvt.getNewValue();
			ArrayList<Object> added=new ArrayList<Object>(newv.length);
			ArrayList<Object> removed=new ArrayList<Object>(oldv.length);

			for(Object o:oldv)
				removed.add(o);

			for(Object o:newv)
			{
				removed.remove(o);
				added.add(o);
			}

			for(Object o:oldv)
				added.remove(o);

			for(Object o:added)
			{
				ReflectionMgr.getSingleton().addPropertyChangeListener(o, itsLabelProperty, itsLabelListener);
				ReflectionMgr.getSingleton().addPropertyChangeListener(o, itsChildrenProperty, itsChildrenListener);
			}

			for(Object o:removed)
			{
				ReflectionMgr.getSingleton().removePropertyChangeListener(o, itsLabelProperty, itsLabelListener);
				ReflectionMgr.getSingleton().removePropertyChangeListener(o, itsChildrenProperty, itsChildrenListener);
			}

			Object m=aEvt.getSource();
			viewer.refresh(m, true);
		}		
	};

	class ViewContentProvider implements IStructuredContentProvider, 
	ITreeContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if(oldInput!=null)
				ReflectionMgr.getSingleton().removePropertyChangeListener(oldInput, itsChildrenProperty, itsChildrenListener);
			ReflectionMgr.getSingleton().addPropertyChangeListener(newInput, itsChildrenProperty, itsChildrenListener);
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent == itsRootBean) {
				return (Object[]) ReflectionMgr.getSingleton().findObjectKeyPathValue(itsRootBean, itsChildrenProperty);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			return ReflectionMgr.getSingleton().findObjectKeyPathValue(itsRootBean, itsParentProperty);
		}
		public Object [] getChildren(Object parent) {
			Object[] ret= (Object[]) ReflectionMgr.getSingleton().findObjectKeyPathValue(parent, itsChildrenProperty);

			for(Object o:ret)
			{
				ReflectionMgr.getSingleton().addPropertyChangeListener(o, itsLabelProperty, itsLabelListener);
				ReflectionMgr.getSingleton().addPropertyChangeListener(o, itsChildrenProperty, itsChildrenListener);

			}
			return ret;
		}
		public boolean hasChildren(Object parent) {
			Boolean b= (Boolean) ReflectionMgr.getSingleton().findObjectKeyPathValue(parent, itsIsParentProperty);
			return b!=null && b.booleanValue();
		}
	}
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			return (String) ReflectionMgr.getSingleton().findObjectKeyPathValue(obj, itsLabelProperty);
		}
		public Image getImage(Object obj) {
			return itsImageProvider.getImage(obj);
			//			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
//			if (obj instanceof TreeParent)
//			imageKey = ISharedImages.IMG_OBJ_FOLDER;
//			return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}
	}
	class NameSorter extends ViewerSorter {
	}
	/**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
	public BeanTreeViewer(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		viewer = new TreeViewer(this, SWT.BORDER);
		itsTree = viewer.getTree();
		final FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(100, 0);
		fd_tree.right = new FormAttachment(100, 0);
		fd_tree.top = new FormAttachment(0, 0);
		fd_tree.left = new FormAttachment(0, 0);
		itsTree.setLayoutData(fd_tree);
		//
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
	}

	public void configure(Object aRootBean, String isParentProperty,
			String aChildrenProperty, String aParentProperty,
			String aLabelProperty, IImageProvider anImageProvider) {
		
		itsRootBean = aRootBean ;
		itsChildrenProperty = aChildrenProperty;
		itsParentProperty = aParentProperty;
		itsIsParentProperty = isParentProperty;
		itsLabelProperty = aLabelProperty;
		itsImageProvider = anImageProvider;
		
		ReflectionMgr.getSingleton().addPropertyChangeListener(itsRootBean, itsChildrenProperty, itsChildrenListener);
		viewer.setInput(itsRootBean);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	private void fillContextMenu(IMenuManager manager) {
		drillDownAdapter.addNavigationActions(manager);
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		drillDownAdapter.addNavigationActions(manager);
	}

	public void hookMenus(IWorkbenchPartSite aSite) {
		MenuManager menuMgr = new MenuManager("#PopupMenu");

		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		aSite.registerContextMenu(menuMgr, viewer);
		
		if (aSite instanceof IViewSite) {
			IViewSite theViewSite = (IViewSite) aSite;
			contributeToActionBars(theViewSite);
		}
	}
	private void contributeToActionBars(IViewSite aSite) {
		IActionBars bars = aSite.getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	public TreeViewer getViewer() {
		return viewer;
	}

}
