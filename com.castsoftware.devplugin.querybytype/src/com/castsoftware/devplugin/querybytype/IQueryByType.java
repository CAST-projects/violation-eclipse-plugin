package com.castsoftware.devplugin.querybytype;

import java.util.List;


/**
 * Interface that needs to be implemented by the classes handling
 * the specific artifact types
 * (e.g.: class com.castsoftware.devplugin.querybytype.java.JavaConstructor)
 * 
 *  @author CDO
 *  @author JAR
 */
public interface IQueryByType {
	List<IQueryResultAction> openEditorFor(String fullname);

}
