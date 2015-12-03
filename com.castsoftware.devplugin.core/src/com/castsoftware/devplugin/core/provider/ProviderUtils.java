package com.castsoftware.devplugin.core.provider;

public class ProviderUtils {

	public static String getWebservicesUrlAndCheck(String webservicesUrl) throws ProviderException {
		if(webservicesUrl == null || webservicesUrl.trim().length() == 0){
			throw new ProviderException("Enter your web services Url, please ");
		}
		char lastChar = webservicesUrl.charAt(webservicesUrl.length() - 1);
		if(lastChar != '/' && lastChar != '\\')
		{
			return webservicesUrl + '/';	
		}
		return webservicesUrl;
	}
}
