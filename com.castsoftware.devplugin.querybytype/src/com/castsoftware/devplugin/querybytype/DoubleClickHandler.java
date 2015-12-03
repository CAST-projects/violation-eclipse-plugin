package com.castsoftware.devplugin.querybytype;

import org.eclipse.core.runtime.CoreException;

import com.castsoftware.devplugin.core.model.Violation;
import com.castsoftware.devplugin.violationview.ViolationOpener;

/**
 * Class used to respond a double-click action on an item 
 * of the CAST violation list.
 * 
 * @author CDO
 * @author JAR
 *
 */
public class DoubleClickHandler implements ViolationOpener {

	public DoubleClickHandler() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * This method is the one to be called by other plug-ins willing to display
	 * the code corresponding to one violation. 
	 * It will instantiate QueryByTypeManager that will redirect the openEditor() 
	 * call to the appropriate class, corresponding to the type of artifact 
	 * for which we need to display the code.
	 * 
	 * @param v	Violation 
	 */
	public void openViolation(Violation v)
	{
		try
		{
			QueryByTypeManager.getSingleton().openEditorFor(v.getTypeId(), v.getFullName());
		}
		catch (CoreException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * This method returns a boolean reporting whether or not the provided violation is
	 * supported for source code display.  
	 * 
	 * @param v	Violation 
	 * @return	Boolean reporting whether the violation is supported (false: not supported)
	 */
	public boolean isViolationSupported(Violation v)
	{
		return QueryByTypeManager.getSingleton().isViolationSupported(v.getTypeId(), v.getFullName());
	}

}
