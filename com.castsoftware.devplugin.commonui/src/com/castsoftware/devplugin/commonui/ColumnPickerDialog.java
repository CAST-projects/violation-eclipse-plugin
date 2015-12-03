package com.castsoftware.devplugin.commonui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import com.castsoftware.devplugin.commonui.ColumnPickerComposite;

public class ColumnPickerDialog extends Dialog
{

	private ColumnPickerComposite itsColumnPickerComposite;
	private ColumnPickerManager itsMgr;
	/**
	 * Create the dialog
	 * @param parentShell
	 */
	public ColumnPickerDialog(Shell parentShell)
	{
		super(parentShell);
	}

	/**
	 * Create contents of the dialog
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent)
	{
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());

		itsColumnPickerComposite = new ColumnPickerComposite(container, SWT.NONE);
		final FormData fd_columnPickerComposite = new FormData();
		fd_columnPickerComposite.bottom = new FormAttachment(100, -5);
		fd_columnPickerComposite.right = new FormAttachment(100, -5);
		fd_columnPickerComposite.top = new FormAttachment(0, 5);
		fd_columnPickerComposite.left = new FormAttachment(0, 5);
		itsColumnPickerComposite.setLayoutData(fd_columnPickerComposite);
		//
		itsColumnPickerComposite.setMgr(itsMgr);
		return container;
	}

	/**
	 * Create contents of the button bar
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent)
	{
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	/**
	 * Return the initial size of the dialog
	 */
	@Override
	protected Point getInitialSize()
	{
		return new Point(269, 226);
	}
	protected void configureShell(Shell newShell)
	{
		super.configureShell(newShell);
		newShell.setText("Column Visibility Control");
	}

	public void setMgr(ColumnPickerManager aMgr)
	{
		itsMgr = aMgr;
	}


}
