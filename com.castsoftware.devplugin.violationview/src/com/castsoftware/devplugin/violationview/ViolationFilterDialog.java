package com.castsoftware.devplugin.violationview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.castsoftware.devplugin.commoncore.ReflectionMgr;
import com.castsoftware.devplugin.commonui.AppModuleTreeViewerGroup;
import com.castsoftware.devplugin.commonui.FilteredTreeViewerGroup;
import com.castsoftware.devplugin.core.provider.CentralObjectProvider;
import com.castsoftware.devplugin.core.provider.ProviderException;
import com.castsoftware.ds.entity.ApplicationEntity;
import com.castsoftware.ds.entity.BaseTechnology;
import com.castsoftware.ds.entity.ModuleEntity;
import com.castsoftware.ds.entity.ProjectEntity;
import com.castsoftware.ds.entity.SubTechnology;
import com.castsoftware.ds.entity.TechnologyEntity;
import com.swtdesigner.SWTResourceManager;

public class ViolationFilterDialog extends Dialog
{

	private Button itsOKButton;
	private AppModuleTreeViewerGroup itsModuleGroup;
	private FilteredTreeViewerGroup itsTechnoGroup;

	protected Collection<Integer> itsObjectTypeIds;

	private Object[] itsGrayedApps = new Object[0];
	private Object[] itsCheckedApps = new Object[0];
	private Object[] itsGrayedTechnos = new Object[0];
	private Object[] itsCheckedTechos = new Object[0];

	private CentralObjectProvider itsProvider;

	/**
	 * Create the dialog
	 * 
	 * @param parentShell
	 */
	ViolationFilterDialog(CentralObjectProvider aProvider)
	{
		super((Shell) null);
		itsProvider = aProvider;
	}
	
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Action Plan Filter");		
	}
	
	/**
	 * Create contents of the dialog
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(null);

		itsModuleGroup = new AppModuleTreeViewerGroup(container, SWT.NONE);
		itsModuleGroup.setText("Applications/Module Selection (required)");
		itsModuleGroup.setBounds(10, 10, 306, 310);

		itsTechnoGroup = new FilteredTreeViewerGroup(container, SWT.NONE);
		itsTechnoGroup.setText("Technologies Selection (optional)");
		itsTechnoGroup.setBounds(326, 10, 306, 310);
		//

		ICheckStateListener theListener = new ICheckStateListener()
		{
			public void checkStateChanged(CheckStateChangedEvent aEvent)
			{
				if(itsOKButton != null) {
					itsOKButton.setEnabled(itsModuleGroup.getCheckedElements().length > 0);
				}
			}
		};
		
		itsModuleGroup.addCheckStateListener(theListener);
		itsTechnoGroup.addCheckStateListener(theListener);

		final Label thePleaseSelectAtLabel = new Label(container, SWT.NONE);
		thePleaseSelectAtLabel.setFont(SWTResourceManager.getFont("", 9, SWT.ITALIC));
		thePleaseSelectAtLabel.setText("Please select at least one module or an application to be able to fetch the violations");
		thePleaseSelectAtLabel.setBounds(10, 325, 576, 26);
		
		fillContent();
		return container;
	}

	protected void okPressed()
	{
		itsGrayedApps = itsModuleGroup.getGrayedElements();
		itsCheckedApps = itsModuleGroup.getCheckedElements();
		itsGrayedTechnos = itsTechnoGroup.getGrayedElements();
		itsCheckedTechos = itsTechnoGroup.getCheckedElements();

		super.okPressed();
	}

	private void prepareModuleTree()
	{
		itsModuleGroup.setContentProvider(new ITreeContentProvider()
		{
			Object[] itsApps;

			public void dispose()
			{
			}

			public void inputChanged(Viewer aViewer, Object aOldInput, Object aNewInput)
			{
				if (itsApps == null)
				{
					List<ApplicationEntity> l;
					try
					{
						l = itsProvider.getAppsAndModules();
					} catch (ProviderException e)
					{
						MessageDialog.openError(getShell(), "Exception", e.getMessage());
						itsApps = new Object[0];
						return;
					}

					itsApps = new Object[l.size()];
					for (int i = 0; i < itsApps.length; i++)
						itsApps[i] = l.get(i);
				}

			}

			public Object[] getChildren(Object aParentElement)
			{
				if (aParentElement instanceof ApplicationEntity)
				{
					ApplicationEntity app = (ApplicationEntity) aParentElement;
					List<ModuleEntity> modules = app.getModule();
					return modules.toArray();

				}
				return null;
			}

			public Object getParent(Object aElement)
			{
				if (aElement instanceof ModuleEntity)
				{
					ModuleEntity module = (ModuleEntity) aElement;
					Integer appId = module.getParentId();
					for(Object appObj : itsApps){
						ApplicationEntity app = (ApplicationEntity)appObj;
						if(appId != null && app != null
								&& appId.equals(app.getEntityId())){
							return app;
						}
					}
				}
				return null;
			}

			public boolean hasChildren(Object aElement)
			{
				return aElement instanceof ApplicationEntity;
			}

			public Object[] getElements(Object aInputElement)
			{
				return itsApps;
			}
		});

		itsModuleGroup.setLabelProvider(new LabelProvider()
		{

			@Override
			public String getText(Object aElement)
			{
				return ReflectionMgr.getSingleton().findObjectKeyPathValue(aElement, "name").toString();
			}

		});

	}
	private void prepareTechnoTree()
	{
		itsTechnoGroup.setContentProvider(new ITreeContentProvider()
		{
			Object[] itsTechnos;

			public void dispose()
			{
			}

			@SuppressWarnings("unchecked")
			public void inputChanged(Viewer aViewer, Object aOldInput, Object aNewInput)
			{
				if (aNewInput!=null)
				{				
					if (itsTechnos == null)
					{
						List<TechnologyEntity> l = (List<TechnologyEntity>) aNewInput;
						
						if (l.size()>0)
						{	
							itsTechnos = new Object[l.size()];
							for (int i = 0; i < itsTechnos.length; i++)
								itsTechnos[i] = l.get(i);
						}
					}
				}
			}

			public Object[] getChildren(Object aParentElement)
			{
				if (aParentElement instanceof TechnologyEntity)
				{
					TechnologyEntity app = (TechnologyEntity) aParentElement;
					List<SubTechnology> subTechno = app.getSubTechno();
					return subTechno.toArray();
				}
				return null;
			}

			public Object getParent(Object aElement)
			{
				if (aElement instanceof SubTechnology)
				{
					SubTechnology subTechno = (SubTechnology) aElement;
					Integer parentId = subTechno.getParentId();
					for(Object technoObj : itsTechnos){
						TechnologyEntity techno = (TechnologyEntity)technoObj;
						if(parentId != null && techno != null 
								&& parentId.equals(techno.getTechnoId())){
							return techno;
						}
					}
				}
				return null;
			}

			public boolean hasChildren(Object aElement)
			{
				return aElement instanceof TechnologyEntity;
			}

			public Object[] getElements(Object aInputElement)
			{
				return itsTechnos;
			}
		});

		itsTechnoGroup.setLabelProvider(new LabelProvider()
		{

			@Override
			public String getText(Object aElement)
			{
				return ReflectionMgr.getSingleton().findObjectKeyPathValue(aElement, "name").toString();
			}

			@Override
			public Image getImage(Object aElement)
			{
				return null;
			}
		});

	}
	private void fillContent()
	{
		prepareModuleTree();
		prepareTechnoTree();

		itsModuleGroup.setInput(this);
		itsModuleGroup.setCheckedElements(itsCheckedApps);
		itsModuleGroup.setGrayedElements(itsGrayedApps);

		Job theJob = new Job("Fetching Technologies...")
		{
			@Override
			protected IStatus run(IProgressMonitor aMonitor)
			{
				Throwable ex = null;

				aMonitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
				try
				{
					final List<TechnologyEntity> l;
						l = itsProvider.getTechnologiyTree();
						Display.getDefault().syncExec(new Runnable()
						{
							public void run()
							{
								itsTechnoGroup.setInput(l);
								itsTechnoGroup.setCheckedElements(itsCheckedTechos);
								itsTechnoGroup.setGrayedElements(itsGrayedTechnos);
							}
						});

				} catch (ProviderException e)
				{
					ex=e;
				}
				aMonitor.done();
				return new Status(ex == null ? IStatus.OK : IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, ex == null? "Fetched violations" : "failed to fetch violations", ex);
			}
		};
		theJob.setUser(true);
		theJob.schedule();


	}

	/**
	 * Create contents of the button bar
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		itsOKButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		if(itsCheckedApps != null && itsCheckedApps.length > 0){
			itsOKButton.setEnabled(true);
		} else {
			itsOKButton.setEnabled(false);
		}
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(645, 425);
	}

	public List<ProjectEntity> getCheckedAppOrModule() throws ProviderException
	{
		// 1 application or list of module in one application are allowed.
		List<ProjectEntity> appMods = new ArrayList<ProjectEntity>();
		if (itsCheckedApps != null && itsCheckedApps.length > 0)
		{
			for(Object appModule : itsCheckedApps)
			{
				if(appModule != null){
					// select the whole application
					if(appModule instanceof ApplicationEntity){
						ApplicationEntity appOrg =(ApplicationEntity)appModule;
						Integer appId = appOrg.getEntityId();
						// check if this application is not grayed
						if(appId != null && !isGrayedApplication(appId)){
							ApplicationEntity app = new ApplicationEntity();
							app.setEntityId(appId);
							app.setName(appOrg.getName());
							appMods.add(app);
						}
					} else if(appModule instanceof ModuleEntity){
						ModuleEntity modOrg = (ModuleEntity)appModule;
						Integer appId = modOrg.getParentId();
						Integer modId = modOrg.getEntityId();
						// select module if his parent is a grayed application 
						if(appId != null && isGrayedApplication(appId)){
							ModuleEntity mod = new ModuleEntity();
							mod.setEntityId(modId);
							mod.setName(modOrg.getName());
							appMods.add(mod);
						}
					}
				}
			}
		} else {
			throw new ProviderException("Please select at least one module or an application");
		}
		return appMods;
	}

	private boolean isGrayedApplication(Integer appId) {
		if(itsGrayedApps == null || itsGrayedApps.length == 0 || appId == null){
			return false;
		}
		for(Object obj : itsGrayedApps){
			if(obj instanceof ApplicationEntity){
				Integer appGrayedId = ((ApplicationEntity)obj).getEntityId();
				if(appId.equals(appGrayedId)){
					return true;
				}
			}
		}
		return false;
	}

	public List<BaseTechnology> getCheckedTechno()
	{
		if(itsCheckedTechos == null || itsCheckedTechos.length == 0){
			return null;
		}
		List<BaseTechnology> technos = new ArrayList<BaseTechnology>();

		// add subTechnology except those included in technology
		for(Object subTechno : itsCheckedTechos)
		{
			if(subTechno instanceof SubTechnology){
				Integer subTechnoId = ((SubTechnology)subTechno).getTechnoId();
				if(subTechnoId != null){
					SubTechnology subTechInc = new SubTechnology();
					subTechInc.setTechnoId(subTechnoId);
					technos.add(subTechInc);
				}
			}
		}
		return technos;
	}
}
