package com.castsoftware.devplugin.querybytype.java;

import java.util.List;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchPattern;

import com.castsoftware.devplugin.querybytype.IQueryByType;
import com.castsoftware.devplugin.querybytype.IQueryResultAction;

/**
 * 
 * Class used to handle source code display queries of
 * "Java Initializer" artifacts.
 * 
 * @author JAR
 *
 */
public class JavaInitializer extends SearchableObjectWithEditorAction implements IQueryByType {

	public JavaInitializer() {
		super();
	}
	
	/**
	 * Starts the the artifact search (search scope: whole work-space)
	 * 
	 *  @param	fullname	Full name of the artifact to search for
	 *  @return	nbMatch		Number of matching artifacts found
	 */
	public List<IQueryResultAction> openEditorFor(String fullname)  {
		if (fullname==null || fullname=="") return null;
		// Java Initializer -> will open the corresponding class
		// We will extract the class name from the full name
		StringBuffer var = new StringBuffer();
		String newFullName=null;
		
		var.append(fullname);
		int end=var.indexOf(".{");
		int start=0;
		newFullName=var.substring(start, end).toString();		// newfullname="pkg.class"
		
		// Search string is ready. Search can be started.
		return startSearch(
				newFullName,
				IJavaSearchConstants.CLASS,
				IJavaSearchConstants.DECLARATIONS, 
				SearchPattern.R_PATTERN_MATCH);
		

		
	}
}
