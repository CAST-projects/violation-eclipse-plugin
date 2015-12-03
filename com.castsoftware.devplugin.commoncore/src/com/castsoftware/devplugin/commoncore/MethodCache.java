package com.castsoftware.devplugin.commoncore;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

 class MethodCache {
	private Map<Class<?>,Map<String,Method>> itsClassSetterMap=new HashMap<Class<?>, Map<String,Method>>();
	private String itsPrefix;
	
	

	public MethodCache(String aPrefix) {
		super();
		itsPrefix = aPrefix;
	}

	private synchronized Map<String,Method>fetchSetterMapForClass(Class<?> c)
	{
		Map<String,Method> ret=itsClassSetterMap.get(c);

		if(ret==null)
		{
			ret = new HashMap<String, Method>();
			itsClassSetterMap.put(c, ret);
		}
		return ret;
	}

	private static Method findMethodForClassWithMethodName(Class<?>c,String name)
	{
		try
		{
			for(Method m:c.getMethods())
			{
				if(m.getGenericParameterTypes().length==1 && name.equals(m.getName()))
					return m;
			}
		}
		catch (SecurityException e)
		{
			throw ReflectionMgr.throwError(e);
		}
		return null;
	}

	 Method findMethodForClassWithPropertyName(Class<?>c,String aProperty)
	{
		Map<String,Method> map=fetchSetterMapForClass(c);

		synchronized (map)
		{
			Method ret=map.get(aProperty);

			if(ret==null)
			{
				 ret=findMethodForClassWithMethodName(c, ReflectionMgr.makeMethodName(itsPrefix,aProperty));
				
				if(ret==null)
					throw new AssertionError("cannot find setter for property "+aProperty+" in class "+c.getName());
				
				map.put(aProperty, ret);

			}
			return ret;
		}
	}


}
