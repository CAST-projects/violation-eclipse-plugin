package com.castsoftware.devplugin.commonui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ColumnPickerComposite extends Composite
{

	private CheckboxTableViewer itsTableViewer;
	private TableColumn itsColumnNameTableColumn;
	private Table itsTable;
	@SuppressWarnings("unused")
	private TableColumnSizeController itsSizeController;
	private ColumnPickerManager itsMgr;
	
	/**
	 * Create the composite
	 * @param parent
	 * @param style
	 */
	public ColumnPickerComposite(Composite parent, int style)
	{
		super(parent, style);
		setLayout(new FormLayout());

		itsTableViewer = CheckboxTableViewer.newCheckList(this, SWT.FULL_SELECTION | SWT.BORDER);
		itsTable = itsTableViewer.getTable();
		final FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(100, -5);
		fd_table.right = new FormAttachment(100, -5);
		fd_table.top = new FormAttachment(0, 5);
		fd_table.left = new FormAttachment(0, 5);
		itsTable.setLayoutData(fd_table);
		itsTable.setLinesVisible(true);
		itsTable.setHeaderVisible(true);

		itsColumnNameTableColumn = new TableColumn(itsTable, SWT.NONE);
		itsColumnNameTableColumn.setWidth(391);
		itsColumnNameTableColumn.setText("Column Name");
		//
		
		itsSizeController=new TableColumnSizeController(itsTable,itsColumnNameTableColumn);
		itsTableViewer.setLabelProvider(new LabelProvider ());
		itsTableViewer.setContentProvider(new ArrayContentProvider());
		
		itsTable.addListener(SWT.Paint, new Listener(){

			public void handleEvent(Event aEvent)
			{
				for(TableColumn tc:itsMgr.getHideableColumns())
				{
					boolean b=itsMgr.isHidden(tc);
					
					itsTableViewer.setChecked(tc, ! b);	
				}
				itsTable.removeListener(aEvent.type, this);
				
			}});
		itsTableViewer.addCheckStateListener(new ICheckStateListener(){

			public void checkStateChanged(CheckStateChangedEvent aEvent)
			{
				Object elt=aEvent.getElement();
				if (elt instanceof TableColumn)
				{
					TableColumn tc = (TableColumn) elt;
					if(aEvent.getChecked())
						itsMgr.showColumn(tc);
					else
						itsMgr.hideColumn(tc);
					
				}
				
			}});
	}

	@Override
	protected void checkSubclass()
	{
		// Disable the check that prevents subclassing of SWT components
	}

	public ColumnPickerManager getMgr()
	{
		return itsMgr;
	}

	public void setMgr(ColumnPickerManager aMgr)
	{
		itsMgr = aMgr;
		itsTableViewer.setInput(itsMgr.getHideableColumns());

	}

	private static class LabelProvider extends BaseLabelProvider implements ITableLabelProvider
	{

		public Image getColumnImage(Object aElement, int aColumnIndex)
		{
			return null;
		}

		public String getColumnText(Object aElement, int aColumnIndex)
		{
			return ((TableColumn)aElement).getText();
		}	
	}
	
}
