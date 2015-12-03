package com.castsoftware.devplugin.querybytype.java;


import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;

import com.castsoftware.devplugin.querybytype.IQueryResultAction;

/**
 * Class used to collect the results from a search engine query.
 * 
 * @author JAR
 *
 */
public class CastSearchRequestor extends SearchRequestor {

	private SearchableObject itsSearchable;
	private List<IQueryResultAction> itsActions=new LinkedList<IQueryResultAction>();
	
	public CastSearchRequestor(SearchableObject o) {
		super();
		itsSearchable=o;
	}
		
	/**
	 * This method returns the number of matching artifacts found.
	 *  
	 * @return	nbMatchFound	Number of matching artifacts found.
	 */
	public int getnbMatchFound(){
		return itsActions.size();
	}

   /**
	* Call-back method: will be invoked each time a matching result is found
	* (example: if the search is on method MyClass.myMethod(), acceptSearchMatch
	* will be called for each matching method (more than once if myMethod() is overloaded).
	* If several matching artifacts are found in the code currently available in Eclipse,
	* we will only display the first match and ignore the others.
	* 
	* @param	match	A search match representing the result of a search query.
	*  
	*/
	public void acceptSearchMatch(SearchMatch match) throws CoreException {
			
				IJavaElement javaElement = (IJavaElement)match.getElement(); 
				itsActions.add(itsSearchable.createAction(javaElement));
	}

public List<IQueryResultAction> getActions()
{
	return itsActions;
}
	

}
