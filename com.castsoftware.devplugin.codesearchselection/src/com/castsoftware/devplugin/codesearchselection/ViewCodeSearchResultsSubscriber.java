package com.castsoftware.devplugin.codesearchselection;

import java.util.List;

import com.castsoftware.devplugin.querybytype.IQueryResultAction;
import com.castsoftware.devplugin.querybytype.ISubscriber;

public class ViewCodeSearchResultsSubscriber implements ISubscriber
{

	public ViewCodeSearchResultsSubscriber()
	{
	}

	public void resultsChanged(List<IQueryResultAction> l)
	{
		CodeSearchResultView.showResults(l);

	}

}
