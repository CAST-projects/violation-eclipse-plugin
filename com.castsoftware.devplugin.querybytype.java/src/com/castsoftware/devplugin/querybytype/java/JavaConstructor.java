package com.castsoftware.devplugin.querybytype.java;

import java.util.List;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchPattern;

import com.castsoftware.devplugin.querybytype.IQueryByType;
import com.castsoftware.devplugin.querybytype.IQueryResultAction;

/**
 * 
 * Class used to handle source code display queries of
 * "Java Constructor" artifacts.
 * 
 * @author JAR
 *
 */
public class JavaConstructor extends SearchableObjectWithEditorAction implements IQueryByType {

	public JavaConstructor() {
		super();
	}


	/**
	 * Starts the the artifact search (search scope: whole work-space)
	 * 
	 *  @param	fullname	Full name of the artifact to search for
	 *  @return	nbMatch		Number of matching artifacts found
	 */
	public List<IQueryResultAction> openEditorFor(String fullname) {
		if (fullname==null || fullname=="") return null;
		
		// Java Constructor 
		// (we cannot differentiate multiple constructors)
		StringBuffer var = new StringBuffer();
		String newfullname=null;
		
		var.append(fullname);
		int start=0;
		int end=var.lastIndexOf(".");
		if (end<=0) {end=var.length();};
		newfullname=var.substring(start, end).toString();  // We strip the constructor name => pkg.class
		
		
		// Search string is ready. Search can be started.
		return startSearch(
				newfullname,
				IJavaSearchConstants.CONSTRUCTOR, 
				IJavaSearchConstants.DECLARATIONS, 
				SearchPattern.R_PATTERN_MATCH);
		
	
		}
	
		
}
