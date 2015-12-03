package com.castsoftware.devplugin.diagtreeview;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.castsoftware.devplugin.commonui.MyAbstractPage;
import com.castsoftware.devplugin.core.model.DiagDescName;
import com.castsoftware.devplugin.core.model.IDiag;
import com.castsoftware.devplugin.core.provider.CentralObjectProvider;

public class DiagnosticOutlinePage extends MyAbstractPage implements
IContentOutlinePage {

	private Link itsHyperlink;
	private StyledText itsDescriptionlText;
	private StyledText itsRemediationText;
	private StyledText itsRationalText;
	private Composite itsComposite;
	private IDiag itsDiagnostic;
	private CentralObjectProvider itsProvider;


	public DiagnosticOutlinePage(CentralObjectProvider aProvider) {
		super();
		itsProvider = aProvider;
	}

	protected void handleSelection(ISelection aSelection)
	{
		if (aSelection instanceof IStructuredSelection) 
		{
			IStructuredSelection str = (IStructuredSelection) aSelection;
			Object o=str.getFirstElement();

			if (o instanceof IDiag) 
			{
				setDiagnostic((IDiag)o);
			}
		}

	}

	@Override
	public void createControl(Composite aParent) {
		aParent.setLayout(new FormLayout());
		itsComposite = new Composite(aParent, SWT.NONE);
		final FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, 0);
		fd_composite.right = new FormAttachment(100, 0);
		fd_composite.top = new FormAttachment(0, 0);
		fd_composite.left = new FormAttachment(0, 0);
		itsComposite.setLayoutData(fd_composite);
		itsComposite.setLayout(new FormLayout());

		itsRationalText = new StyledText(itsComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY | SWT.FULL_SELECTION | SWT.WRAP);
		final FormData fd_rationalText = new FormData();
		fd_rationalText.right = new FormAttachment(100, -14);
		fd_rationalText.bottom = new FormAttachment(0, 181);
		fd_rationalText.top = new FormAttachment(0, 115);
		itsRationalText.setLayoutData(fd_rationalText);

		Label theRationaleLabel;
		theRationaleLabel = new Label(itsComposite, SWT.NONE);
		FormData fd_theRationaleLabel;
		fd_theRationaleLabel = new FormData();
		fd_theRationaleLabel.bottom = new FormAttachment(0, 113);
		fd_theRationaleLabel.top = new FormAttachment(0, 100);
		theRationaleLabel.setLayoutData(fd_theRationaleLabel);
		theRationaleLabel.setText("Rationale");

		Label theRationaleLabel_1;
		theRationaleLabel_1 = new Label(itsComposite, SWT.NONE);
		final FormData fd_theRationaleLabel_1 = new FormData();
		fd_theRationaleLabel_1.top = new FormAttachment(0, 187);
		fd_theRationaleLabel_1.bottom = new FormAttachment(0, 200);
		fd_theRationaleLabel_1.left = new FormAttachment(0, 5);
		fd_theRationaleLabel_1.right = new FormAttachment(0, 95);
		theRationaleLabel_1.setLayoutData(fd_theRationaleLabel_1);
		theRationaleLabel_1.setText("Remediation");

		itsRemediationText = new StyledText(itsComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY | SWT.FULL_SELECTION | SWT.WRAP);
		final FormData fd_remediationText = new FormData();
		fd_remediationText.right = new FormAttachment(100, -14);
		fd_remediationText.bottom = new FormAttachment(0, 266);
		fd_remediationText.top = new FormAttachment(0, 200);
		fd_remediationText.left = new FormAttachment(itsRationalText, 0, SWT.LEFT);
		itsRemediationText.setLayoutData(fd_remediationText);

		final Label theRationaleLabel_2 = new Label(itsComposite, SWT.NONE);
		final FormData fd_theRationaleLabel_2 = new FormData();
		fd_theRationaleLabel_2.bottom = new FormAttachment(0, 23);
		fd_theRationaleLabel_2.top = new FormAttachment(0, 10);
		fd_theRationaleLabel_2.right = new FormAttachment(0, 85);
		fd_theRationaleLabel_2.left = new FormAttachment(0, 5);
		theRationaleLabel_2.setLayoutData(fd_theRationaleLabel_2);
		theRationaleLabel_2.setText("Description");

		itsDescriptionlText = new StyledText(itsComposite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY | SWT.FULL_SELECTION | SWT.WRAP);
		fd_theRationaleLabel.right = new FormAttachment(itsDescriptionlText, 55, SWT.LEFT);
		fd_theRationaleLabel.left = new FormAttachment(itsDescriptionlText, 0, SWT.LEFT);
		fd_rationalText.left = new FormAttachment(itsDescriptionlText, 0, SWT.LEFT);
		final FormData fd_descriptionlText = new FormData();
		fd_descriptionlText.right = new FormAttachment(100, -14);
		fd_descriptionlText.bottom = new FormAttachment(theRationaleLabel_2, 66, SWT.BOTTOM);
		fd_descriptionlText.top = new FormAttachment(theRationaleLabel_2, 0, SWT.BOTTOM);
		fd_descriptionlText.left = new FormAttachment(theRationaleLabel_2, 0, SWT.LEFT);
		itsDescriptionlText.setLayoutData(fd_descriptionlText);

		itsHyperlink = new Link(itsComposite, SWT.NONE);
		itsHyperlink.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				com.castsoftware.devplugin.castbrowser.CASTBrowser.showViewPartWithURL(getURL());
			}
		});
		final FormData fd_hyperLink = new FormData();
		fd_hyperLink.right = new FormAttachment(0, 141);
		fd_hyperLink.top = new FormAttachment(0, 295);
		fd_hyperLink.left = new FormAttachment(itsRemediationText, 0, SWT.LEFT);
		itsHyperlink.setLayoutData(fd_hyperLink);
		itsHyperlink.setText("<a href=\"http://www.eclipse.org\">Show Diagnostic Portal Page</a>");
		manageHyperlink();
	}

	@Override
	public Control getControl() {
		return itsComposite;
	}

	@Override
	public void setFocus() {
		setDiagnostic(itsDiagnostic);
	}


//	public void setSelection(ISelection aSelection) {
//	if (aSelection instanceof IStructuredSelection) {
//	IStructuredSelection str = (IStructuredSelection) aSelection;
//	Object o=str.getFirstElement();

//	System.out.println("sel obj = "+o);
//	if (o instanceof Diagnostic) {
//	Diagnostic d = (Diagnostic) o;
//	setDiagnostic(d);

//	}
//	}		
//	}

	public IDiag getDiagnostic() {
		return itsDiagnostic;
	}

	private void setText(StyledText aText, com.castsoftware.devplugin.core.model.DiagDescName aDesc)
	{
		if(aText==null)
			return;

		String value=null;

		if(itsDiagnostic != null)
			value=itsDiagnostic.getDescription(aDesc);
		if(value=="")
			value=null;

		aText.setVisible(value != null);
		if(value != null)
			aText.setText(value);
	}
	public void setDiagnostic(IDiag aDiagnostic) {
		itsDiagnostic = aDiagnostic;

		setText(itsDescriptionlText,DiagDescName.Description);
		setText(itsRationalText,DiagDescName.Rationale);
		setText(itsRemediationText,DiagDescName.Remediation);

	}

	private void manageHyperlink()
	{
		if(itsHyperlink != null && itsDiagnostic!=null)
		{
			String theURLBase=itsProvider.getPortalURL();
			itsHyperlink.setEnabled(theURLBase != null);

			if(theURLBase != null)
			{
				String theHTML="<A href=\""+getURL()+"\">Show Diagnostic Portal Page</A>";
				itsHyperlink.setText(theHTML);
			}
			else
			{
				itsHyperlink.setText("Undefined portal URL. See Preferences");
			}
		}
		
	}
	private String getURL()
	{
		//String theURLBase=itsProvider.getPortalURL();
		String theURL = null;
		//theURL = MessageFormat.format("{0}?object={1}&frame=FRAME_METRIC_DETAIL&metric={2}&snapshot={3}", theURLBase,Integer.toString(itsProvider.getAppsAndModules().get(0).getEntityId()),Integer.toString(itsDiagnostic.getID()),Integer.toString(itsProvider.getLastSnapshotID()));
		return theURL;
	}

}
