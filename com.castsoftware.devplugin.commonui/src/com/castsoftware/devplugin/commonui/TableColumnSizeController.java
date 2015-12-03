package com.castsoftware.devplugin.commonui;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TableColumnSizeController extends ControlAdapter
{

	private Table itsTable;
	private TableColumn itsResizableColumn;
	boolean stopresize = false;

	public TableColumnSizeController(Table aTable, TableColumn aResizableColumn)
	{
		super();
		itsTable = aTable;
		itsResizableColumn = aResizableColumn;
		itsTable.addControlListener(this);
		
		// listen for ech column size change too
		for (TableColumn tc : aTable.getColumns())
		{
			if(tc != aResizableColumn)
				tc.addControlListener(this);
		}
		resize();
	}

	public void controlResized(final ControlEvent e)
	{
		if (stopresize)
			return;
		resize();
	}

	protected void resize()
	{
		int tw = itsTable.getClientArea().width;
		
		// if you dont do this, call to column.setWith will call this gain and again recursively....
		stopresize = true;
		
		try
		{
			for (TableColumn t : itsTable.getColumns())
			{
				if (t != itsResizableColumn)
					tw -= t.getWidth();
			}
			if (tw < 80)
				tw = 80;
			itsResizableColumn.setWidth(tw);
		} finally
		{
			stopresize = false;
		}
	}
}
