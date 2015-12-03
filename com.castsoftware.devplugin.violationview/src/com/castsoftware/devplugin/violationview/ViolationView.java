package com.castsoftware.devplugin.violationview;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.castsoftware.devplugin.commonui.CentralObjectProviderBasedViewPart;
import com.castsoftware.devplugin.commonui.ColumnPickerDialog;
import com.castsoftware.devplugin.commonui.ColumnPickerManager;
import com.castsoftware.devplugin.commonui.ColumnSorter;
import com.castsoftware.devplugin.commonui.TableColumnSizeController;
import com.castsoftware.devplugin.core.model.Violation;
import com.castsoftware.devplugin.core.provider.MapModelFetcherNotification;
import com.castsoftware.devplugin.core.provider.ProviderException;
import com.castsoftware.devplugin.violationview.Activator;
import com.castsoftware.devplugin.violationview.outline.ViolationOutline;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

public class ViolationView extends CentralObjectProviderBasedViewPart
{

	private Action itsViewCodeAction;
	private Action itsChooseColumnsAction;
	private Action itsRemoveViolationAction;
	private Composite itsContainer;
	private Action itsFetchViolationsAction;
	private Action itsShowFilterDialogAction;
	private Label itsWarningLabel;
	@SuppressWarnings("unused")
	private DataBindingContext m_bindingContext;
	private TableViewer itsTableViewer;
	private TableColumn itsStatusColumn;
	private TableColumn itsObjectColumn;
	private TableColumn itsDiagnosticColumn;
	private TableColumn itsCommentColumn;
	private Table itsTable;
	public static final String ID = "com.castsoftware.devplugin.violationview.ViolationView"; //$NON-NLS-1$

	private List<Violation> itsViolations = null;
	private ColumnSorter itsSorter;
	@SuppressWarnings("unused")
	private TableColumnSizeController itsSizeController;

	private ViolationFilterDialog itsViolationFilterDialog;

	private boolean itsIsFetching = false;
	private StackLayout itsStackLayout;

	private ColumnPickerManager itsColumnPickerManager;
	private ColumnPickerDialog itsColumnPickerDialog;
	private TableColumn itsAppColumn;
	private TableColumn itsModuleColumn;
	private TableColumn itsObjectTypeColumn;
	private TableColumn itsCriticalColumn;

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent)
	{
		itsContainer = new Composite(parent, SWT.NONE);

		itsStackLayout = new StackLayout();
		itsContainer.setLayout(itsStackLayout);

		itsWarningLabel = new Label(itsContainer, SWT.NONE);
		itsWarningLabel.setForeground(SWTResourceManager.getColor(0, 0, 255));
		itsWarningLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD | SWT.ITALIC));
		itsWarningLabel.setAlignment(SWT.CENTER);
		final FormData fd_warningLabel = new FormData();
		fd_warningLabel.bottom = new FormAttachment(100, -211);
		fd_warningLabel.right = new FormAttachment(100, -19);
		fd_warningLabel.left = new FormAttachment(0, 15);
		fd_warningLabel.top = new FormAttachment(0, 115);
		itsWarningLabel.setLayoutData(fd_warningLabel);
		itsWarningLabel.setText("Please fetch violations using the Fetch button");

		itsTableViewer = new TableViewer(itsContainer, SWT.FULL_SELECTION | SWT.BORDER);
		//itsTableViewer.setLabelProvider(new PropertyTableLabelProvider(itsTableViewer));
		itsTableViewer.setLabelProvider(new ViolationViewLabelProvider(itsTableViewer));
		itsTableViewer.setContentProvider(new ArrayContentProvider());
		itsTable = itsTableViewer.getTable();
		final FormData fd_table = new FormData();
		fd_table.top = new FormAttachment(0, 5);
		fd_table.left = new FormAttachment(0, 5);
		fd_table.bottom = new FormAttachment(100, -5);
		fd_table.right = new FormAttachment(100, -5);
		itsTable.setLayoutData(fd_table);
		itsTable.setLinesVisible(true);
		itsTable.setHeaderVisible(true);

		itsCriticalColumn = new TableColumn(itsTable, SWT.NONE);
		itsCriticalColumn.setData("hidden", "true");
		itsCriticalColumn.setData("hideable", "true");
		itsCriticalColumn.setMoveable(true);
		itsCriticalColumn.setData("property", "critical");
		itsCriticalColumn.setWidth(65);
		itsCriticalColumn.setText("Critical");

		itsAppColumn = new TableColumn(itsTable, SWT.NONE);
		itsAppColumn.setData("hidden", "true");
		itsAppColumn.setData("hideable", "true");
		itsAppColumn.setMoveable(true);
		itsAppColumn.setData("property", "appName");
		itsAppColumn.setWidth(100);
		itsAppColumn.setText("Application");

		itsModuleColumn = new TableColumn(itsTable, SWT.NONE);
		itsModuleColumn.setData("hidden", "true");
		itsModuleColumn.setData("hideable", "true");
		itsModuleColumn.setMoveable(true);
		itsModuleColumn.setData("property", "moduleName");
		itsModuleColumn.setWidth(100);
		itsModuleColumn.setText("Module");

		itsObjectTypeColumn = new TableColumn(itsTable, SWT.NONE);
		itsObjectTypeColumn.setData("hidden", "true");
		itsObjectTypeColumn.setData("hideable", "true");
		itsObjectTypeColumn.setMoveable(true);
		itsObjectTypeColumn.setData("property", "typeName");
		itsObjectTypeColumn.setWidth(100);
		itsObjectTypeColumn.setText("Object Type");

		itsCommentColumn = new TableColumn(itsTable, SWT.NONE);
		itsCommentColumn.setData("hidden", "true");
		itsCommentColumn.setData("hideable", "true");
		itsCommentColumn.setMoveable(true);
		itsCommentColumn.setData("property", "comment");
		itsCommentColumn.setWidth(100);
		itsCommentColumn.setText("Comment");

		itsDiagnosticColumn = new TableColumn(itsTable, SWT.NONE);
		itsDiagnosticColumn.setMoveable(true);
		itsDiagnosticColumn.setData("property", "diagName");
		itsDiagnosticColumn.setWidth(200);
		itsDiagnosticColumn.setText("Quality Rule");

		itsObjectColumn = new TableColumn(itsTable, SWT.NONE);
		itsObjectColumn.setMoveable(true);
		itsObjectColumn.setData("property", "fullName");
		itsObjectColumn.setWidth(200);
		itsObjectColumn.setText("Object");

		itsStatusColumn = new TableColumn(itsTable, SWT.NONE);
		itsStatusColumn.setData("hideable", "true");
		itsStatusColumn.setMoveable(true);
		itsStatusColumn.setData("property", "violationStatus");
		itsStatusColumn.setWidth(85);
		itsStatusColumn.setText("New?");

		createActions();
		initializeToolBar();
		initializeMenu();
		m_bindingContext = initDataBindings();

		itsTable.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
	    	  // Do Nothing
		      }

		      public void keyReleased(KeyEvent e) {
		    	  if ((e.character == SWT.DEL) || (e.character == SWT.BS))
		    	  {
		    		  ViolationView.this.deleteViolation();
		    	  }
		      }
		    });
		
		itsSorter = new ColumnSorter(itsViolations, itsTable)
		{
			@Override
			protected void reloadTable()
			{
				ViolationView.this.reloadTable();

			}
		};
		itsSizeController = new TableColumnSizeController(itsTable, itsObjectColumn);

		itsTableViewer.addOpenListener(new IOpenListener()
		{

			public void open(OpenEvent aEvent)
			{
				ViolationOpenerManager.getSingleton().openViolationFromEvent(aEvent);
			}
		});
		itsTableViewer.addPostSelectionChangedListener(new ISelectionChangedListener()
		{

			public void selectionChanged(SelectionChangedEvent event)
			{

				itsRemoveViolationAction.setEnabled(true);
				
				ViolationOpenerManager m = ViolationOpenerManager.getSingleton();
				Violation v = m.getSelectedViolation(event.getSelection());
				boolean b = v != null;
				boolean c = m.hasEditorOpener();

				if (b && c)
				{
					boolean d = m.isViolationSupported(v);
					itsViewCodeAction.setEnabled(d);
				}
				else
				{
					itsViewCodeAction.setEnabled(false);
				}

			}
		});

		getSite().setSelectionProvider(itsTableViewer);
		itsColumnPickerManager = new ColumnPickerManager(itsTable);
		itsStackLayout.topControl = itsWarningLabel;
		itsStackLayout.topControl = itsWarningLabel;
		itsColumnPickerDialog = new ColumnPickerDialog(null);
		itsColumnPickerDialog.setMgr(itsColumnPickerManager);

		hookContextMenu();
		computeStatus();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(itsContainer, "com.castsoftware.devplugin.help.CAST_violation_list");
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(itsTable, "com.castsoftware.devplugin.help.CAST_violation_list");
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(itsWarningLabel, "com.castsoftware.devplugin.help.CAST_violation_list");
	}

	private void hookContextMenu()
	{
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener()
		{
			public void menuAboutToShow(IMenuManager manager)
			{
				IStructuredSelection sel = (IStructuredSelection)itsTableViewer.getSelection();
				Object o = sel.getFirstElement();
				if (o != null && o instanceof Violation)
				{
					itsRemoveViolationAction.setEnabled(true);					
				} else {
					itsRemoveViolationAction.setEnabled(false);
				}
					
				ViolationView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(itsTableViewer.getControl());
		itsTableViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, itsTableViewer);
	}

	private void fillContextMenu(IMenuManager manager)
	{
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(new Separator());
		manager.add(itsRemoveViolationAction);
		manager.add(itsChooseColumnsAction);
	}

	protected void reloadTable()
	{
		if (itsTableViewer != null)
		{
			itsTableViewer.setInput(itsViolations);
			computeStatus();
		}
	}
	
	protected void deleteViolation()
	{
		IStructuredSelection sel = (IStructuredSelection)itsTableViewer.getSelection();
		Object o = sel.getFirstElement();
		if (o != null && o instanceof Violation) {
			itsViolations.remove((Violation)o);	
			ViolationView.this.reloadTable();
			computeStatus();
		}
	}

	private void setTopControl(Control t)
	{
		itsStackLayout.topControl = t;
		itsContainer.layout();
	}

	protected void computeStatus()
	{
		IStatusLineManager theMgr = getViewSite().getActionBars().getStatusLineManager();
		boolean hasViolation = itsViolations != null;

		// itsWarningLabel.setVisible

		if (itsIsFetching)
		{
			theMgr.setMessage("Fetching Violations....");
			itsWarningLabel.setText("Fetching Violations....");

		}
		else
		{
			if (hasViolation)
			{
				theMgr.setMessage("" + itsViolations.size() + " violations");
			}
			else
			{
				String msg = "Please Fetch violations using '" + itsFetchViolationsAction.getText() + "' or '" + itsShowFilterDialogAction.getText() + "' buttons";
				theMgr.setMessage(msg);
				itsWarningLabel.setText(msg);
			}
		}
		setTopControl(hasViolation && (!itsIsFetching) ? itsTable : itsWarningLabel);
		itsFetchViolationsAction.setEnabled(hasViolation && (!itsIsFetching) );
		itsRemoveViolationAction.setEnabled(itsTableViewer!=null && (!itsTableViewer.getSelection().isEmpty()));
	}

	@Override
	protected void initFromString(String aSecondaryId)
	{
		super.initFromString(aSecondaryId);
		itsViolationFilterDialog = new ViolationFilterDialog(getProvider());
	}

	protected void showFilterDialog()
	{
		if (itsViolationFilterDialog.open() == ViolationFilterDialog.OK)
		{
			startFetching();
		}
	}

	/**
	 * Create the actions
	 */
	private void createActions()
	{

		itsShowFilterDialogAction = new Action("Show Filter Dialog")
		{
			public void run()
			{
				showFilterDialog();
			}
		};
		itsShowFilterDialogAction.setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/configure.png"));

		itsFetchViolationsAction = new Action("Fetch Violations")
		{
			public void run()
			{
				startFetching();
			}
		};
		itsFetchViolationsAction.setEnabled(false);
		itsFetchViolationsAction.setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/reload.png"));

		itsChooseColumnsAction = new Action("Choose Columns")
		{
			public void run()
			{
				itsColumnPickerDialog.open();
				itsRemoveViolationAction.setEnabled(false);
			}
		};
		itsChooseColumnsAction.setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/insert_table_col.png"));
		
		itsRemoveViolationAction = new Action("Remove Violation")
		{
			public void run()
			{
				ViolationView.this.deleteViolation();
			}
		};
		itsRemoveViolationAction.setEnabled(false);
		itsRemoveViolationAction.setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/violation_delete.png"));

		itsViewCodeAction = new Action("View Code")
		{
			public void run()
			{
				ISelection sel = itsTableViewer.getSelection();
				ViolationOpenerManager.getSingleton().openViolationFromSelection(sel);
			}
		};
		itsViewCodeAction.setImageDescriptor(ResourceManager.getPluginImageDescriptor(Activator.getDefault(), "icons/displayincode.png"));
		itsViewCodeAction.setEnabled(false);
		// Create the actions
	}

	protected void startFetching()
	{
		itsIsFetching = true;
		computeStatus();

		Job theJob = new Job("Fetching Violations...")
		{
			@Override
			protected IStatus run(final IProgressMonitor aMonitor)
			{
				Throwable ex = null;

				aMonitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
				try
				{
					MapModelFetcherNotification fetcherNotification = new MapModelFetcherNotification()
					{
						public void setCurrentStepName(String aN)
						{
							aMonitor.subTask(aN);
						}
						
					};
					itsViolations = getProvider().fetchLatestViolationsFromActionPlan(
							itsViolationFilterDialog.getCheckedAppOrModule(),
							itsViolationFilterDialog.getCheckedTechno(), fetcherNotification);
				}
				catch (ProviderException e)
				{
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage());
				}
				aMonitor.done();

				itsIsFetching = false;
				Display.getDefault().syncExec(new Runnable()
				{

					public void run()
					{
						itsSorter.setList(itsViolations);
						reloadTable();
						computeStatus();
					}
				});

				return new Status(ex == null ? IStatus.OK : IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK, ex == null ? "Fetched violations" : "failed to fetch violations", ex);
			}
		};
		theJob.setUser(true);
		theJob.schedule();

	}

	/**
	 * Initialize the toolbar
	 */
	private void initializeToolBar()
	{
		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();

		toolbarManager.add(itsShowFilterDialogAction);

		toolbarManager.add(itsFetchViolationsAction);

		toolbarManager.add(itsViewCodeAction);

		toolbarManager.add(itsRemoveViolationAction);
	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu()
	{
		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();

		menuManager.add(itsShowFilterDialogAction);

		menuManager.add(itsFetchViolationsAction);

		menuManager.add(itsChooseColumnsAction);
	}

	@Override
	public void setFocus()
	{
		// Set the focus
	}

	protected DataBindingContext initDataBindings()
	{
		//
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		//
		return bindingContext;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class aAdapter) {
		if (aAdapter == IContentOutlinePage.class) {
			IContentOutlinePage thePage = new ViolationOutline(getProvider());
			return thePage;
		}
		return super.getAdapter(aAdapter);
	}
}
