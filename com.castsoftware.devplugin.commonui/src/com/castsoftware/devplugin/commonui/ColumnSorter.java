package com.castsoftware.devplugin.commonui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.castsoftware.devplugin.commoncore.AbstractModel;
import com.castsoftware.devplugin.commoncore.ReflectionMgr;


public abstract class ColumnSorter {
	private TableColumn itsSortTableColumn=null;
	private boolean itsIsSortingAscending=true;
	private List<? extends AbstractModel> itsList;
	private Table itsTable;
	
	
	public ColumnSorter(List<? extends AbstractModel> aList, Table t) {
		super();
		itsList = aList;
		itsTable=t;
		for(TableColumn col:t.getColumns())
		{
			col.addSelectionListener(new SelectionAdapter()
			{
				public void widgetSelected(SelectionEvent e) {
					sortUsingColumn(e);
				}
			}
			);
		}
	}

	protected void sortUsingColumn(SelectionEvent aE) {
		if(itsList==null)
			return;
		
		TableColumn tc = (TableColumn) aE.getSource();
		final String theProperty = (String) tc.getData("property");

		if (tc == itsSortTableColumn) {
			itsIsSortingAscending = !itsIsSortingAscending;

		} else {
			if (itsSortTableColumn != null)
				itsSortTableColumn.setImage(null);
			itsSortTableColumn = tc;
			itsIsSortingAscending = true;
		}
		
		itsTable.setSortColumn(itsSortTableColumn);
		itsTable.setSortDirection(itsIsSortingAscending ? SWT.UP:SWT.DOWN);
		
		Comparator<AbstractModel> c = new Comparator<AbstractModel>() {
			@SuppressWarnings("unchecked")
			public int compare(AbstractModel aO1, AbstractModel aO2) {
				Comparable<Object> val1 = (Comparable<Object>) ReflectionMgr
						.getSingleton()
						.findObjectKeyPathValue(aO1, theProperty);
				Object val2 = ReflectionMgr.getSingleton()
						.findObjectKeyPathValue(aO2, theProperty);
				int ret;

				if (val1 == null)
					ret = -1;
				else if (val2 == null)
					ret = 1;
				else
					ret = val1.compareTo(val2);
				if (!itsIsSortingAscending)
					ret *= -1;

				return ret;

			}
		};
		Collections.sort(itsList, c);
		reloadTable();
	}

	public void resetSorting()
	{
		itsTable.setSortColumn(null);
	}
	public List<? extends AbstractModel> getList()
	{
		return itsList;
	}

	public void setList(List<? extends AbstractModel> aList)
	{
		itsList = aList;
		resetSorting();
	}

	protected abstract void reloadTable();


}
