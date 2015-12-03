package com.castsoftware.devplugin.violationview.outline;

import java.text.MessageFormat;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.castsoftware.devplugin.castbrowser.CASTBrowser;
import com.castsoftware.devplugin.commoncore.ReflectionMgr;
import com.castsoftware.devplugin.commonui.MyAbstractPage;
import com.castsoftware.devplugin.core.model.MetricHandler;
import com.castsoftware.devplugin.core.model.Violation;
import com.castsoftware.devplugin.core.provider.CentralObjectProvider;
import com.castsoftware.devplugin.core.provider.ProviderException;
import com.castsoftware.devplugin.violationview.Activator;
import com.swtdesigner.ResourceManager;
import com.swtdesigner.SWTResourceManager;

public class ViolationOutline extends MyAbstractPage implements IContentOutlinePage
{

	private Text itsCostComplexity;
	private Text itsStatus;
	private Text itsObjectName;
	private DiagDescComposite itsTotal;
	private DiagDescComposite itsOutput;
	private DiagDescComposite itsRemediationExample;
	private DiagDescComposite itsSample;
	private DiagDescComposite itsRemediation;
	private DiagDescComposite itsDescription;
	private DiagDescComposite itsReference;
	private DiagDescComposite itsRational;
	private Button itsMetricButton;
	private Button itsObjectButton;
	private List itsValueList;
	private CentralObjectProvider itsProvider;

	private Button itsCriticalButton;
	private Text itsIsNew;
	private Text itsComment;
	private Text itsObjectType;
	private Text itsObjectFullName;
	private Text itsDiagName;
	private Composite itsControl;

	private Violation itsViolation;
	private MetricHandler itsDiagnostic;

	/**
	 * Create the PageBookView Page
	 */
	public ViolationOutline(CentralObjectProvider aProvider)
	{
		super();
		itsProvider = aProvider;
	}

	/**
	 * Create contents of the PageBookView Page
	 * 
	 * @param parent
	 */
	@Override
	public void createControl(Composite parent)
	{
		itsControl = new Composite(parent, SWT.NULL);
		itsControl.setLayout(new FormLayout());
		PlatformUI.getWorkbench().getHelpSystem().setHelp(itsControl, "com.castsoftware.devplugin.help.CAST_outline_view");

		final ScrolledComposite scrolledComposite = new ScrolledComposite(itsControl, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.getVerticalBar().setPageIncrement(50);
		scrolledComposite.setExpandHorizontal(true);
		final FormData fd_scrolledComposite = new FormData();
		fd_scrolledComposite.bottom = new FormAttachment(100, -10);
		fd_scrolledComposite.right = new FormAttachment(100, -9);
		fd_scrolledComposite.top = new FormAttachment(0, 5);
		fd_scrolledComposite.left = new FormAttachment(0, 5);
		scrolledComposite.setLayoutData(fd_scrolledComposite);

		final Composite theComposite = new Composite(scrolledComposite, SWT.NONE);
		theComposite.setLocation(0, 0);
		theComposite.setLayout(new FormLayout());

		itsObjectButton = new Button(theComposite, SWT.FLAT);
		itsObjectButton.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(final MouseEvent e)
			{
				showObjectPortalPage();
			}
		});
		itsObjectButton.setToolTipText("Show portal page about Object");
		itsObjectButton.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "icons/outline/browser.png"));
		final FormData fd_objectButton = new FormData();
		fd_objectButton.top = new FormAttachment(0, 83);
		fd_objectButton.left = new FormAttachment(0, 50);
		itsObjectButton.setLayoutData(fd_objectButton);

		itsMetricButton = new Button(theComposite, SWT.FLAT);
		itsMetricButton.addMouseListener(new MouseAdapter()
		{
			public void mouseUp(final MouseEvent e)
			{
				showDiagPortalPage();
			}
		});
		itsMetricButton.setImage(ResourceManager.getPluginImage(Activator.getDefault(), "icons/outline/browser.png"));
		final FormData fd_metricButton = new FormData();
		fd_metricButton.left = new FormAttachment(0, 46);
		itsMetricButton.setLayoutData(fd_metricButton);
		itsMetricButton.setToolTipText("Show portal page about Object");

		final Label theNameLabel = new Label(theComposite, SWT.NONE);
		final FormData fd_theNameLabel = new FormData();
		fd_theNameLabel.bottom = new FormAttachment(0, 28);
		fd_theNameLabel.top = new FormAttachment(0, 15);
		fd_theNameLabel.left = new FormAttachment(0, 5);
		theNameLabel.setLayoutData(fd_theNameLabel);
		theNameLabel.setText("Name:");

		itsDiagName = new Text(theComposite, SWT.READ_ONLY | SWT.BORDER);
		final FormData fd_diagName = new FormData();
		fd_diagName.right = new FormAttachment(100, -4);
		fd_diagName.left = new FormAttachment(theNameLabel, 5, SWT.RIGHT);
		fd_diagName.bottom = new FormAttachment(0, 30);
		fd_diagName.top = new FormAttachment(0, 12);
		itsDiagName.setLayoutData(fd_diagName);

		Group theObjectGroup;
		theObjectGroup = new Group(theComposite, SWT.NONE);
		fd_metricButton.top = new FormAttachment(theObjectGroup, 5, SWT.DEFAULT);
		theObjectGroup.setLayout(new FormLayout());
		theObjectGroup.setText("Object");
		final FormData fd_theObjectGroup = new FormData();
		fd_theObjectGroup.bottom = new FormAttachment(0, 295);
		fd_theObjectGroup.right = new FormAttachment(100, -4);
		fd_theObjectGroup.top = new FormAttachment(0, 95);
		fd_theObjectGroup.left = new FormAttachment(theNameLabel, 0, SWT.LEFT);
		theObjectGroup.setLayoutData(fd_theObjectGroup);

		final Label theFullNameLabel = new Label(theObjectGroup, SWT.NONE);
		FormData fd_theFullNameLabel = new FormData();
		;
		fd_theFullNameLabel.left = new FormAttachment(theFullNameLabel, 0, SWT.LEFT);
		fd_theFullNameLabel.top = new FormAttachment(0, 38);
		fd_theFullNameLabel.bottom = new FormAttachment(0, 50);
		fd_theFullNameLabel.right = new FormAttachment(0, 62);
		theFullNameLabel.setLayoutData(fd_theFullNameLabel);
		theFullNameLabel.setText("Full Name:");

		itsObjectFullName = new Text(theObjectGroup, SWT.READ_ONLY | SWT.BORDER);
		final FormData fd_objectFullName = new FormData();
		fd_objectFullName.right = new FormAttachment(100, -5);
		fd_objectFullName.top = new FormAttachment(0, 32);
		fd_objectFullName.bottom = new FormAttachment(0, 50);
		fd_objectFullName.left = new FormAttachment(0, 65);
		itsObjectFullName.setLayoutData(fd_objectFullName);

		final Label objectTypeLabel = new Label(theObjectGroup, SWT.NONE);
		final FormData fd_objectTypeLabel = new FormData();
		fd_objectTypeLabel.right = new FormAttachment(0, 51);
		fd_objectTypeLabel.left = new FormAttachment(theFullNameLabel, 0, SWT.LEFT);
		objectTypeLabel.setLayoutData(fd_objectTypeLabel);
		objectTypeLabel.setText("Type:");

		itsObjectType = new Text(theObjectGroup, SWT.READ_ONLY | SWT.BORDER);
		fd_objectTypeLabel.top = new FormAttachment(itsObjectType, -17, SWT.BOTTOM);
		fd_objectTypeLabel.bottom = new FormAttachment(itsObjectType, 0, SWT.BOTTOM);
		final FormData fd_objectType = new FormData();
		fd_objectType.right = new FormAttachment(100, -5);
		fd_objectType.bottom = new FormAttachment(0, 73);
		fd_objectType.top = new FormAttachment(0, 55);
		fd_objectType.left = new FormAttachment(0, 65);
		itsObjectType.setLayoutData(fd_objectType);

		Group theValueGroup;
		theValueGroup = new Group(theObjectGroup, SWT.NONE);
		theValueGroup.setText("Values");
		final FormData fd_theValueGroup = new FormData();
		fd_theValueGroup.right = new FormAttachment(100, -5);
		theValueGroup.setLayoutData(fd_theValueGroup);
		theValueGroup.setLayout(new FormLayout());

		itsValueList = new List(theValueGroup, SWT.BORDER);
		itsValueList.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		final FormData fd_valueList = new FormData();
		fd_valueList.bottom = new FormAttachment(100, -6);
		fd_valueList.right = new FormAttachment(100, -14);
		fd_valueList.left = new FormAttachment(0, 10);
		fd_valueList.top = new FormAttachment(0, 5);
		itsValueList.setLayoutData(fd_valueList);

		final Label theNameLabel_1 = new Label(theObjectGroup, SWT.NONE);
		final FormData fd_theNameLabel_1 = new FormData();
		fd_theNameLabel_1.bottom = new FormAttachment(0, 28);
		fd_theNameLabel_1.top = new FormAttachment(0, 15);
		fd_theNameLabel_1.left = new FormAttachment(theFullNameLabel, 0, SWT.LEFT);
		theNameLabel_1.setLayoutData(fd_theNameLabel_1);
		theNameLabel_1.setText("Name:");

		itsObjectName = new Text(theObjectGroup, SWT.READ_ONLY | SWT.BORDER);
		final FormData fd_name = new FormData();
		fd_name.right = new FormAttachment(100, -5);
		fd_name.top = new FormAttachment(itsObjectFullName, -23, SWT.TOP);
		fd_name.bottom = new FormAttachment(itsObjectFullName, -5, SWT.TOP);
		fd_name.left = new FormAttachment(itsObjectFullName, 0, SWT.LEFT);
		itsObjectName.setLayoutData(fd_name);

		Label theStatusLabel_1;
		theStatusLabel_1 = new Label(theObjectGroup, SWT.NONE);
		fd_theValueGroup.left = new FormAttachment(theStatusLabel_1, 0, SWT.LEFT);
		final FormData fd_theStatusLabel_1 = new FormData();
		fd_theStatusLabel_1.left = new FormAttachment(theFullNameLabel, 0, SWT.LEFT);
		fd_theStatusLabel_1.right = new FormAttachment(0, 40);
		theStatusLabel_1.setLayoutData(fd_theStatusLabel_1);
		theStatusLabel_1.setText("Status:");

		itsStatus = new Text(theObjectGroup, SWT.READ_ONLY | SWT.BORDER);
		fd_theValueGroup.bottom = new FormAttachment(itsStatus, 62, SWT.BOTTOM);
		fd_theValueGroup.top = new FormAttachment(itsStatus, 5, SWT.BOTTOM);
		fd_theStatusLabel_1.bottom = new FormAttachment(itsStatus, 13, SWT.TOP);
		fd_theStatusLabel_1.top = new FormAttachment(itsStatus, 0, SWT.TOP);
		final FormData fd_status = new FormData();
		fd_status.right = new FormAttachment(100, -5);
		fd_status.bottom = new FormAttachment(0, 122);
		fd_status.top = new FormAttachment(0, 104);
		fd_status.left = new FormAttachment(itsObjectType, 0, SWT.LEFT);
		itsStatus.setLayoutData(fd_status);

		final Label theStatusLabel_1_1 = new Label(theObjectGroup, SWT.NONE);
		final FormData fd_theStatusLabel_1_1 = new FormData();
		fd_theStatusLabel_1_1.left = new FormAttachment(theFullNameLabel, 0, SWT.LEFT);
		fd_theStatusLabel_1_1.right = new FormAttachment(0, 90);
		fd_theStatusLabel_1_1.bottom = new FormAttachment(objectTypeLabel, 18, SWT.BOTTOM);
		fd_theStatusLabel_1_1.top = new FormAttachment(objectTypeLabel, 5, SWT.BOTTOM);
		theStatusLabel_1_1.setLayoutData(fd_theStatusLabel_1_1);
		theStatusLabel_1_1.setText("Cost Complexity:");

		itsCostComplexity = new Text(theObjectGroup, SWT.READ_ONLY | SWT.BORDER);
		final FormData fd_costComplexity = new FormData();
		fd_costComplexity.right = new FormAttachment(100, -5);
		fd_costComplexity.bottom = new FormAttachment(theStatusLabel_1_1, 18, SWT.TOP);
		fd_costComplexity.top = new FormAttachment(theStatusLabel_1_1, 0, SWT.TOP);
		fd_costComplexity.left = new FormAttachment(theStatusLabel_1_1, 5, SWT.RIGHT);
		itsCostComplexity.setLayoutData(fd_costComplexity);

		final Label theCommentLabel = new Label(theComposite, SWT.NONE);
		final FormData fd_theCommentLabel = new FormData();
		fd_theCommentLabel.bottom = new FormAttachment(0, 55);
		fd_theCommentLabel.top = new FormAttachment(0, 43);
		fd_theCommentLabel.left = new FormAttachment(0, 4);
		theCommentLabel.setLayoutData(fd_theCommentLabel);
		theCommentLabel.setText("Comment:");

		final Label theStatusLabel = new Label(theComposite, SWT.NONE);
		final FormData fd_theStatusLabel = new FormData();
		fd_theStatusLabel.top = new FormAttachment(0, 65);
		fd_theStatusLabel.left = new FormAttachment(theCommentLabel, 0, SWT.LEFT);
		theStatusLabel.setLayoutData(fd_theStatusLabel);
		theStatusLabel.setText("New?");

		itsComment = new Text(theComposite, SWT.READ_ONLY | SWT.BORDER);
		final FormData fd_comment = new FormData();
		fd_comment.right = new FormAttachment(100, -4);
		fd_comment.bottom = new FormAttachment(theCommentLabel, 18, SWT.TOP);
		fd_comment.top = new FormAttachment(theCommentLabel, 0, SWT.TOP);
		fd_comment.left = new FormAttachment(0, 60);
		itsComment.setLayoutData(fd_comment);

		itsIsNew = new Text(theComposite, SWT.READ_ONLY | SWT.BORDER);
		final FormData fd_isNew = new FormData();
		fd_isNew.right = new FormAttachment(100, -4);
		fd_isNew.bottom = new FormAttachment(0, 80);
		fd_isNew.top = new FormAttachment(0, 62);
		fd_isNew.left = new FormAttachment(itsComment, 0, SWT.LEFT);
		itsIsNew.setLayoutData(fd_isNew);

		final Group theMetricGroup = new Group(theComposite, SWT.NONE);
		theMetricGroup.setLayout(new FormLayout());
		theMetricGroup.setText("Metric");
		final FormData fd_theMetricGroup = new FormData();
		fd_theMetricGroup.bottom = new FormAttachment(100, -5);
		fd_theMetricGroup.top = new FormAttachment(theObjectGroup, 20, SWT.DEFAULT);
		fd_theMetricGroup.right = new FormAttachment(100, -4);
		fd_theMetricGroup.left = new FormAttachment(theObjectGroup, 0, SWT.LEFT);
		theMetricGroup.setLayoutData(fd_theMetricGroup);

		itsCriticalButton = new Button(theMetricGroup, SWT.CHECK);
		itsCriticalButton.setForeground(SWTResourceManager.getColor(0, 0, 0));
		itsCriticalButton.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD | SWT.ITALIC));
		itsCriticalButton.setEnabled(false);
		final FormData fd_criticalButton = new FormData();
		fd_criticalButton.top = new FormAttachment(0, 6);
		fd_criticalButton.left = new FormAttachment(0, 15);
		itsCriticalButton.setLayoutData(fd_criticalButton);

		Label theCriticalLabel;
		theCriticalLabel = new Label(theMetricGroup, SWT.NONE);
		theCriticalLabel.setFont(SWTResourceManager.getFont("", 11, SWT.BOLD));
		final FormData fd_theCriticalLabel = new FormData();
		fd_theCriticalLabel.right = new FormAttachment(0, 126);
		fd_theCriticalLabel.left = new FormAttachment(itsCriticalButton, 3, SWT.RIGHT);
		fd_theCriticalLabel.top = new FormAttachment(0, 5);
		theCriticalLabel.setLayoutData(fd_theCriticalLabel);
		theCriticalLabel.setText("Critical");

		itsRational = new DiagDescComposite(theMetricGroup, SWT.NONE);
		itsRational.setData("label", "Rationale");
		final FormData fd_rational = new FormData();
		fd_rational.bottom = new FormAttachment(0, 105);
		fd_rational.top = new FormAttachment(0, 41);
		fd_rational.right = new FormAttachment(100, -2);
		fd_rational.left = new FormAttachment(0, 5);
		itsRational.setLayoutData(fd_rational);

		itsDescription = new DiagDescComposite(theMetricGroup, SWT.NONE);
		final FormData fd_description = new FormData();
		fd_description.right = new FormAttachment(itsRational, 0, SWT.RIGHT);
		fd_description.top = new FormAttachment(itsRational, 5, SWT.DEFAULT);
		fd_description.bottom = new FormAttachment(itsRational, 68, SWT.BOTTOM);
		fd_description.left = new FormAttachment(itsRational, 0, SWT.LEFT);
		itsDescription.setLayoutData(fd_description);
		itsDescription.setData("label", "Description");

		itsRemediation = new DiagDescComposite(theMetricGroup, SWT.NONE);
		final FormData fd_remediation = new FormData();
		fd_remediation.right = new FormAttachment(itsDescription, 0, SWT.RIGHT);
		fd_remediation.top = new FormAttachment(itsDescription, 5, SWT.DEFAULT);
		fd_remediation.bottom = new FormAttachment(itsDescription, 68, SWT.BOTTOM);
		fd_remediation.left = new FormAttachment(itsDescription, 0, SWT.LEFT);
		itsRemediation.setLayoutData(fd_remediation);
		itsRemediation.setData("label", "Remediation");

		itsReference = new DiagDescComposite(theMetricGroup, SWT.NONE);
		final FormData fd_reference = new FormData();
		fd_reference.right = new FormAttachment(itsRemediation, 0, SWT.RIGHT);
		fd_reference.bottom = new FormAttachment(itsRemediation, 68, SWT.BOTTOM);
		fd_reference.top = new FormAttachment(itsRemediation, 7, SWT.DEFAULT);
		fd_reference.left = new FormAttachment(itsRemediation, 0, SWT.LEFT);
		itsReference.setLayoutData(fd_reference);
		itsReference.setData("label", "References");

		itsSample = new DiagDescComposite(theMetricGroup, SWT.NONE);
		final FormData fd_sample = new FormData();
		fd_sample.right = new FormAttachment(itsReference, 0, SWT.RIGHT);
		fd_sample.bottom = new FormAttachment(itsReference, 68, SWT.BOTTOM);
		fd_sample.top = new FormAttachment(itsReference, 5, SWT.BOTTOM);
		fd_sample.left = new FormAttachment(itsReference, 0, SWT.LEFT);
		itsSample.setLayoutData(fd_sample);
		itsSample.setData("label", "Sample");

		itsRemediationExample = new DiagDescComposite(theMetricGroup, SWT.NONE);
		final FormData fd_remediationExample = new FormData();
		fd_remediationExample.right = new FormAttachment(itsSample, 0, SWT.RIGHT);
		fd_remediationExample.bottom = new FormAttachment(itsSample, 68, SWT.BOTTOM);
		fd_remediationExample.top = new FormAttachment(itsSample, 5, SWT.BOTTOM);
		fd_remediationExample.left = new FormAttachment(itsSample, 0, SWT.LEFT);
		itsRemediationExample.setLayoutData(fd_remediationExample);
		itsRemediationExample.setData("label", "Remediation Sample");

		itsOutput = new DiagDescComposite(theMetricGroup, SWT.NONE);
		final FormData fd_output = new FormData();
		fd_output.right = new FormAttachment(itsRemediationExample, 0, SWT.RIGHT);
		fd_output.bottom = new FormAttachment(itsRemediationExample, 68, SWT.BOTTOM);
		fd_output.top = new FormAttachment(itsRemediationExample, 5, SWT.BOTTOM);
		fd_output.left = new FormAttachment(itsRemediationExample, 0, SWT.LEFT);
		itsOutput.setLayoutData(fd_output);
		itsOutput.setData("label", "Output");

		itsTotal = new DiagDescComposite(theMetricGroup, SWT.NONE);
		final FormData fd_total = new FormData();
		fd_total.right = new FormAttachment(itsOutput, 0, SWT.RIGHT);
		fd_total.bottom = new FormAttachment(itsOutput, 68, SWT.BOTTOM);
		fd_total.top = new FormAttachment(itsOutput, 5, SWT.BOTTOM);
		fd_total.left = new FormAttachment(itsOutput, 0, SWT.LEFT);
		itsTotal.setLayoutData(fd_total);
		itsTotal.setData("label", "Total");

		theComposite.setSize(497, 926);
		scrolledComposite.setContent(theComposite);
		//

		boolean is64 = true;

		itsObjectButton.setEnabled(is64);
		itsMetricButton.setEnabled(is64);
		changeUIContent();
	}

	protected void showDiagPortalPage()
	{
		CASTBrowser.showViewPartWithURL(getDiagURL());

	}

	protected void showObjectPortalPage()
	{
		CASTBrowser.showViewPartWithURL(getObjectURL());
	}

	private String getDiagURL()
	{
		String theURLBase = itsProvider.getPortalURL();
		String theURL;
		theURL = MessageFormat
				.format(
						"{0}?object={1}&frame=FRAME_METRIC_DETAIL&metric={2}&snapshot={3}",
						theURLBase, itsViolation.getApplicationId() + "",
						itsViolation.getMetricId() + "", itsViolation.getLastSnapId() + "");
		return theURL;
	}

	private String getObjectURL()
	{
		String theURLBase = itsProvider.getPortalURL();
		String theURL = MessageFormat.format("{0}?frame=FRAME_PORTAL_OBJECT_DETAILS&treeobject={1}&snapshot={2}&object={3}", 
				theURLBase, itsViolation.getObjectId() + "", itsViolation.getLastSnapId(), itsViolation.getApplicationId());
		return theURL;
	}

	@Override
	public Control getControl()
	{
		return itsControl;
	}

	@Override
	public void init(IPageSite pageSite)
	{
		super.init(pageSite);
		createActions();
		initializeToolBar();
		initializeMenu();
	}

	@Override
	public void setFocus()
	{
		// Set the focus
	}

	/**
	 * Create the actions
	 */
	private void createActions()
	{
		// Create the actions
	}

	/**
	 * Initialize the toolbar
	 */
	private void initializeToolBar()
	{
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getSite().getActionBars().getToolBarManager();
	}

	/**
	 * Initialize the menu
	 */
	private void initializeMenu()
	{
		@SuppressWarnings("unused")
		IMenuManager menuManager = getSite().getActionBars().getMenuManager();
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener)
	{
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener)
	{
	}

	public ISelection getSelection()
	{
		return null;
	}

	public void setSelection(ISelection selection)
	{
	}

	@Override
	protected void handleSelection(ISelection aSelection)
	{
		if (aSelection instanceof IStructuredSelection)
		{
			IStructuredSelection str = (IStructuredSelection) aSelection;
			Object o = str.getFirstElement();

			if (o == null || o instanceof Violation)
			{
				setViolation((Violation) o);
			}
		}

	}

	private static void setValue(Text aText, Object anObj, String aProp)
	{
		String v = "";

		if (aText == null)
			return;
		if (anObj != null)
		{
			Object o = ReflectionMgr.getSingleton().findObjectKeyPathValue(anObj, aProp);
			v = o == null ? "" : o.toString();
		}
		aText.setText(v);
		aText.setToolTipText(v);
	}

	private static void setValue(DiagDescComposite aText, Object anObj, String aProp)
	{
		String v = "";

		if (aText == null)
			return;
		if (anObj != null)
		{
			Object o = ReflectionMgr.getSingleton().findObjectKeyPathValue(anObj, aProp);
			v = o == null ? "" : o.toString();
			aText.setText(v);
			aText.setToolTipText(v);
		}
	}

	private void setViolation(Violation aO)
	{
		itsViolation = aO;
		changeUIContent();
	}
	private void changeUIContent()
	{
		try
		{
			if(itsProvider==null || itsViolation == null)
				return;

			itsDiagnostic = itsProvider.getDiagById(itsViolation.getMetricId());

			itsProvider.setViolationDetails(itsViolation);
			java.util.List<String> theValues = itsViolation.getMetricValues();
			if (itsValueList != null){
				itsValueList.removeAll();
			}
			if(theValues != null){
				for (String s : theValues){
					itsValueList.add(s);
				}
			}
				
			setValue(itsDiagName, itsViolation, "diagName");
			setValue(itsComment, itsViolation, "comment");
			setValue(itsIsNew, itsViolation, "violationStatus");

			setValue(itsObjectName, itsViolation, "name");
			setValue(itsObjectFullName, itsViolation, "fullName");
			setValue(itsObjectType, itsViolation, "typeName");
			setValue(itsCostComplexity, itsViolation, "complexity");
			setValue(itsStatus, itsViolation, "objectStatus");

			setValue(itsRational, itsDiagnostic, "rationale");
			setValue(itsReference, itsDiagnostic, "reference");
			setValue(itsDescription, itsDiagnostic, "description");
			setValue(itsRemediation, itsDiagnostic, "remediation");
			setValue(itsSample, itsDiagnostic, "sample");
			setValue(itsRemediationExample, itsDiagnostic, "remediationSample");
			setValue(itsOutput, itsDiagnostic, "output");
			setValue(itsTotal, itsDiagnostic, "total");

			if (itsCriticalButton != null)
				itsCriticalButton.setSelection(itsDiagnostic != null && itsDiagnostic.isCritical());

			if (itsObjectButton != null)
				itsObjectButton.setEnabled(itsProvider.getPortalURL() != null);
			if (itsMetricButton != null)
				itsMetricButton.setEnabled(itsProvider.getPortalURL() != null);
		}
		catch (ProviderException e)
		{
			MessageDialog.openError(getSite().getShell(), "Exception", e.getMessage());
		}
	}
}
