package com.castsoftware.devplugin.querybytype.java;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jface.dialogs.MessageDialog;

import com.castsoftware.devplugin.querybytype.IQueryResultAction;

/**
 * Class implementing the startSearch method: each artifact-specific class
 * will inherit from it in order to get the search capability.
 *  
 * @author JAR
 *
 */
public abstract class SearchableObject {
	
	List<IQueryResultAction> startSearch(String stringPattern, int searchFor, int limitTo, int matchRule){
		
		stringPattern=stringPattern.replaceAll("<Default Package>.", "");
		stringPattern=stringPattern.replaceAll("<Default Package>", "");
		
		SearchPattern pattern = SearchPattern.createPattern(
				stringPattern,
				searchFor, // ex: IJavaSearchConstants.CLASS
				limitTo,   //IJavaSearchConstants.DECLARATIONS,
				matchRule  //SearchPattern.R_PATTERN_MATCH
				);
		
		// Create search scope (entire work space).
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
	  
		// Get the search requestor.
		CastSearchRequestor requestor = new CastSearchRequestor(this);
		
		// 	Make the search.
		SearchEngine searchEngine = new SearchEngine();
		try {
			searchEngine.search(pattern, 
								new SearchParticipant[] {SearchEngine.getDefaultSearchParticipant()}, 
								scope, 
								requestor, 
								null);
		} catch (CoreException e) {
			MessageDialog.openError(null, "Exception", e.getMessage());
		}
		
		
		// Retrieve the number of results found		
		//int nbMatch=((CastSearchRequestor) requestor).getnbMatchFound();
		
		return requestor.getActions();
	}

	protected abstract AbstractQueryResultAction createAction(IJavaElement elt);
}
