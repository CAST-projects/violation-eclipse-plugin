package com.castsoftware.devplugin.querybytype.java;

import java.util.List;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchPattern;

import com.castsoftware.devplugin.querybytype.IQueryByType;
import com.castsoftware.devplugin.querybytype.IQueryResultAction;

/**
 * 
 * Class used to handle source code display queries of
 * "Java Filed" artifacts.
 * 
 * @author JAR
 *
 */
public class JavaField extends SearchableObjectWithEditorAction implements IQueryByType {

	public JavaField() {
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
			return startSearch(
					fullname,
					IJavaSearchConstants.FIELD, 
					IJavaSearchConstants.DECLARATIONS, 
					SearchPattern.R_PATTERN_MATCH);

			
		}

	}