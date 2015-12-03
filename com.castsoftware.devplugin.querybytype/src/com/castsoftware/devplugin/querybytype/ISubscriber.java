package com.castsoftware.devplugin.querybytype;

import java.util.List;

public interface ISubscriber
{
	public void resultsChanged(List<IQueryResultAction> l);

}
