package com.castsoftware.devplugin.violationview.outline;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DiagDescComposite extends Composite
{

	private Text itsText;
	private Label itsLabel;
	/**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
	public DiagDescComposite(Composite parent, int style)
	{
		super(parent, style);
		setLayout(new FormLayout());

		itsLabel = new Label(this, SWT.NONE);
		final FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(0, 2);
		fd_label.right = new FormAttachment(0, 495);
		fd_label.left = new FormAttachment(0, 5);
		itsLabel.setLayoutData(fd_label);
		itsLabel.setText("Label");

		itsText = new Text(this, SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);
		final FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 15);
		fd_text.right = new FormAttachment(100, -5);
		fd_text.bottom = new FormAttachment(100, -5);
		fd_text.left = new FormAttachment(0, 5);
		itsText.setLayoutData(fd_text);
		//
	}

	@Override
	public void setData(String aKey, Object aValue)
	{
		if("label".equals(aKey))
			itsLabel.setText(aValue.toString());
		super.setData(aKey, aValue);
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}

	public void setLabelText(String aString)
	{
		itsLabel.setText(aString);
	}

	public void setText(String aString)
	{
		itsText.setText(aString);
	}

	public void setToolTipText(String aString)
	{
		itsText.setToolTipText(aString);
	}

}
