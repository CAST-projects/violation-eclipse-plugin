package com.castsoftware.devplugin.commonui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class ColumnPickerManager
{
	public static final String HIDDEN_KEY = "hidden";
	public static final String HIDEABLE_KEY = "hideable";

	private Table itsTable;
	private List<TableColumn> itsHideableColumns;
	// same size as itsHideableColumns. Stores the previous width
	// of a column before it got hidden
	// if stores 0, this mean the column is not hidden
	private int[] itsPreviousWidths;

	public ColumnPickerManager(Table aTable)
	{
		super();
		itsTable = aTable;
		TableColumn[] theCols = aTable.getColumns();

		itsHideableColumns = new ArrayList<TableColumn>(theCols.length);

		for (TableColumn tc : theCols)
		{
			if (Boolean.parseBoolean((String) tc.getData(HIDEABLE_KEY)))
			{
				itsHideableColumns.add(tc);
			}
		}
		itsPreviousWidths = new int[itsHideableColumns.size()];
		for (TableColumn tc : itsHideableColumns)
		{
			if (Boolean.parseBoolean((String) tc.getData(HIDDEN_KEY)))
			{
				hideColumn(tc);
			}

		}
	}

	public Table getTable()
	{
		return itsTable;
	}

	public List<TableColumn> getHideableColumns()
	{
		return itsHideableColumns;
	}

	private final int indexOf(TableColumn aO)
	{
		int ret= itsHideableColumns.indexOf(aO);
		if(ret<0)
			throw new IllegalArgumentException("TableColumn "+aO+" is not managed by this column picker Manager");
		return ret;
	}

	public boolean isHidden(TableColumn tc)
	{
		int index = indexOf(tc);
		return itsPreviousWidths[index] != 0;
	}

	public void hideColumn(TableColumn tc)
	{
		if (isHidden(tc))
			return;
		int index = indexOf(tc);
		int width = tc.getWidth();
		if (width == 0)
			width = 100;
		itsPreviousWidths[index] = width;
		tc.setWidth(0);
	}
	
	public void showColumn(TableColumn tc)
	{
		if (! isHidden(tc))
			return;

		int index = indexOf(tc);
		
		tc.setWidth(itsPreviousWidths[index]);
		itsPreviousWidths[index]=0;
	}
}
