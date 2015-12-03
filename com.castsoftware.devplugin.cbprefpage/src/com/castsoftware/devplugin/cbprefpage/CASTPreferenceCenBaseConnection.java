package com.castsoftware.devplugin.cbprefpage;

import static com.castsoftware.devplugin.core.provider.ProviderUtils.getWebservicesUrlAndCheck;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.castsoftware.devplugin.core.provider.MapModelFetcher;
import com.castsoftware.devplugin.core.provider.ProviderException;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class CASTPreferenceCenBaseConnection extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
	private Composite container;
	private Text wsUrlLabel;

	private StringFieldEditor wsUrl;

	private boolean itsTestIsOK = false;
	private Button itsTestConn;

	private Map<String, String> itsInitValueMap = new HashMap<String, String>();
	private Label itsErrorLabel;

	public CASTPreferenceCenBaseConnection()
	{
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("CAST Preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors()
	{

		container = new Composite(this.getFieldEditorParent(), SWT.NONE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(container, "com.castsoftware.devplugin.help.CAST_preferences");

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		wsUrl = createStringFieldEditor(CASTPreferenceConstants.CAST_WS_URL, "Web services URL", container);
		addField(wsUrl);
		wsUrlLabel = wsUrl.getTextControl(container);

		itsTestConn = new Button(container, SWT.PUSH);
		itsTestConn.setText("Validate Connection");
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 3;
		itsTestConn.setLayoutData(gridData);

		itsTestConn.addSelectionListener(new SelectionAdapter()
		{

			public void widgetSelected(SelectionEvent event)
			{
				try
				{
					itsTestIsOK = false;
					String urlToTest = wsUrlLabel.getText();
					urlToTest = getWebservicesUrlAndCheck(urlToTest) + "portfoliotree" ;
					itsTestIsOK = MapModelFetcher.test(urlToTest);
					setTestIsOK(itsTestIsOK);
					if(itsTestIsOK){
						MessageDialog.openInformation(container.getShell(), "CAST", "Connection valid!!!");	
					} else {
						MessageDialog.openError(container.getShell(), "CAST", "Can't connect to Webservices or Webservices not working correctly");
					}
				}
				catch (ProviderException e)
				{
					e.printStackTrace();
				}
			}

		});
		GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
		labelData.horizontalSpan = 3;
		Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(labelData);

		final StringFieldEditor ret = new StringFieldEditor(CASTPreferenceConstants.CAST_PORTAL_URL, "Portal URL", container);
		String value_portal = Activator.getDefault().getPreferenceStore().getString(ret.getPreferenceName());
		itsInitValueMap.put(ret.getPreferenceName(), value_portal);
		
		addField(ret);

		itsErrorLabel = new Label(container, SWT.NONE);
		itsErrorLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		
		GridData labData = new GridData();
		labData.horizontalSpan = 2;
		labData.heightHint=100;
		labData.verticalAlignment=SWT.BOTTOM;
		labData.horizontalAlignment=SWT.FILL;
		itsErrorLabel.setLayoutData(labData);

		setTestIsOK(CASTPrefStore.getInstance().isInitialized());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench)
	{
	}

	private void showErrorLabel(String s)
	{
		if (s == null)
		{
			itsErrorLabel.setText("");
		} else
		{
			itsErrorLabel.setText(s);
		}
	}

	private void setTestIsOK(boolean aTestIsOK)
	{
		itsTestIsOK = aTestIsOK;
		itsTestConn.setEnabled(!itsTestIsOK);
		if (!itsTestIsOK)
		{
			showErrorLabel(CASTPrefStore.getInstance().isInitialized() ? "the Connection must be validated" : "You must define a validated connection to be able to connect to the CAST Webservices");
		}
		else
			showErrorLabel(null);
	}

	private StringFieldEditor createStringFieldEditor(String name, String labelText, Composite parent)
	{
		final StringFieldEditor ret = new StringFieldEditor(name, labelText, parent);
		final Text c = ret.getTextControl(parent);
		String value = Activator.getDefault().getPreferenceStore().getString(ret.getPreferenceName());

		itsInitValueMap.put(ret.getPreferenceName(), value);

		c.addModifyListener(new ModifyListener()
		{

			public void modifyText(ModifyEvent aE)
			{
				String value = ret.getPreferenceStore().getString(ret.getPreferenceName());

				setTestIsOK(value != null && value.length() > 0 && value.equals(c.getText()));
			}
		});

		return ret;
	}

	@Override
	public boolean performOk()
	{
		CASTPrefStore st = CASTPrefStore.getInstance();
		/*
		String oldUrlWebservices = st.getWebServicesURL();
		String currentUrlWebservices = wsUrlLabel.getText();
		// update all informations if the url of webservices has been changed.
		if(currentUrlWebservices != null && !currentUrlWebservices.equals(oldUrlWebservices)){
			CentralObjectProviderRegistry centralProvider = CentralObjectProviderRegistry.getSSingleton();
			MapModelFetcher f = new MapModelFetcher(currentUrlWebservices, st.getPortalURL());
			centralProvider.putProvider(null, f);
		}
		*/
		st.setInitialized(true);
		return super.performOk();
	}

}