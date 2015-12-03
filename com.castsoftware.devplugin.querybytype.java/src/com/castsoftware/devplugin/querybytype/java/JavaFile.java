package com.castsoftware.devplugin.querybytype.java;

import java.util.List;

import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchPattern;

import com.castsoftware.devplugin.querybytype.IQueryByType;
import com.castsoftware.devplugin.querybytype.IQueryResultAction;

/**
 * 
 * Class used to handle source code display queries of
 * "Java Files". 
 * We will open the Java Class or the Java Method stored in the file.
 * Used so far for violations on Java Imports and J2EE Scoped Bean
 * (e.g. "Avoid unused Java Import".
 * 
 * @author JAR
 *
 */
public class JavaFile extends SearchableObjectWithEditorAction implements IQueryByType {

	public JavaFile() {
		super();
	}

	/**
	 * Starts the the artifact search (search scope: whole work-space)
	 * 
	 *  @param	fullname	Full name of the artifact to search for
	 *  @return	nbMatch		Number of matching artifacts found
	 */
	public List<IQueryResultAction> openEditorFor(String fullname)  {
			
		// We will try to re-build the FQN of the class/Interface based on its source code file path.
		// The fullname is provided in the following form (Violation):
		// "[D:\MyProjects\CAST\CCSnapshotDestination\SOURCE_CODE\cdoatleap\atleap\src\web\com\blandware\atleap\webapp\action\questionnaire\questionnaire\CallUpdateQuestionnaireAnnotationAction.java].[com.blandware.atleap.webapp.util.core.WebappUtil]"
		// We need to end up searching for the following artifact:
		// com.blandware.atleap.webapp.action.questionnaire.questionnaire.CallUpdateQuestionnaireAnnotationAction
		
		if (fullname==null || fullname=="") return null;
		
		StringBuffer var = new StringBuffer();
		String output=null;
		int nbMatch=0;
		
		int end=0;
		int start=0; 
		int newEnd=-1;
		
		List<IQueryResultAction> aList=null; 
		
		var.append(fullname);
		//System.out.println(fullname);
		end=var.indexOf(".java]");
		if (end!=-1) {
			output=var.substring(start, end).toString().replace("\\", "/");
			var.delete(0, var.length());
			var.append(output);
					
			// We will search recursively, stripping at each iteration the left most part of the name,
			// until we find a matching artifact name or give up if no match is found
			while ((nbMatch==0)&&(newEnd!=0)){
					newEnd=var.indexOf("/")+1;
					if (newEnd!=0){
						output=var.delete(0, newEnd).toString();
						var.delete(0, var.length());
						var.append(output);
					}
					
					aList = startSearch(
							var.toString().replace("/", "."),
							IJavaSearchConstants.CLASS_AND_INTERFACE,
							IJavaSearchConstants.DECLARATIONS, 
							SearchPattern.R_PATTERN_MATCH);
					
							
					nbMatch=aList.size();
					
			}
		}

		return aList;
				
	}
}
	

