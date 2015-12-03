package com.castsoftware.devplugin.commoncore;

import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionMgr
{
	static private ReflectionMgr singleton=new ReflectionMgr();
	private Map<Class<?>, Map<String,Method>> itsClassGetterMap=new HashMap<Class<?>, Map<String,Method>>();
	// for each method-owning class, methodName,parameter class
	private MethodCache itsSetterMethodCache=new MethodCache("set");
	private MethodCache itsSetAdderMethodCache=new MethodCache("addTo");

	private Map<Class<?>, PropertyListenerMgr> itsClassPropListenerAdderMap=new HashMap<Class<?>, PropertyListenerMgr>();
	private Map<Class<?>, PropertyListenerMgr> itsClassPropListenerRemoverMap=new HashMap<Class<?>, PropertyListenerMgr>();

	private static interface PropertyListenerMgr
	{
		public void managePropertyListener(Object o,String prop, PropertyChangeListener l);
	}

	private static class DetailedPropertyListenerMgr implements PropertyListenerMgr
	{
		Method its2ArgMethod;

		public DetailedPropertyListenerMgr(Method aIts2ArgMethod)
		{
			super();
			its2ArgMethod = aIts2ArgMethod;
		}

		public void managePropertyListener(Object aO,String aProp, PropertyChangeListener aL)
		{
			try
			{
				its2ArgMethod.invoke(aO, new Object[]{aProp,aL});
			}
			catch (IllegalArgumentException e)
			{
				throw throwError(e);
			}
			catch (IllegalAccessException e)
			{
				throw throwError(e);
			}
			catch (InvocationTargetException e)
			{
				throw throwError(e);
			}
		}
	}

	private static class BasicPropertyListenerMgr implements PropertyListenerMgr
	{
		Method its1ArgMethod;

		public BasicPropertyListenerMgr(Method aIts2ArgMethod)
		{
			super();
			its1ArgMethod = aIts2ArgMethod;
		}

		public void managePropertyListener(Object aO,String aProp, PropertyChangeListener aL)
		{
			try
			{
				its1ArgMethod.invoke(aO, new Object[]{aL});
			}
			catch (IllegalArgumentException e)
			{
				throw throwError(e);
			}
			catch (IllegalAccessException e)
			{
				throw throwError(e);
			}
			catch (InvocationTargetException e)
			{
				throw throwError(e);
			}
		}
	}
	
	private ReflectionMgr(){}

	 static Error throwError(Exception e)
	{
		return new Error(e);
	}
	static String makeMethodName(String aPrefix,String aProp)
	{
		char c=aProp.charAt(0);
		String remainder=aProp.length()>1? aProp.substring(1):"";
		StringBuilder ret=new StringBuilder();

		ret.append(aPrefix);
		ret.append(Character.toUpperCase(c));
		ret.append(remainder);
		return ret.toString();
	}

	
	synchronized public void addPropertyChangeListener(Object o,String aProp,PropertyChangeListener l)
	{
		if(o==null)
			return;
		Class<?> c=o.getClass();
		PropertyListenerMgr mgr=itsClassPropListenerAdderMap.get(c);
		
		if(mgr==null)
		{
			mgr=createPropertyMgr(c,"addPropertyChangeListener");
			itsClassPropListenerAdderMap.put(c, mgr);
		}
		
		mgr.managePropertyListener(o, aProp, l);
		
	}

	synchronized public void removePropertyChangeListener(Object o,String aProp,PropertyChangeListener l)
	{
		if(o==null)
			return;
		Class<?> c=o.getClass();
		PropertyListenerMgr mgr=itsClassPropListenerRemoverMap.get(c);
		
		if(mgr==null)
		{
			mgr=createPropertyMgr(c,"removePropertyChangeListener");
			itsClassPropListenerRemoverMap.put(c, mgr);
		}
		
		mgr.managePropertyListener(o, aProp, l);
		
	}

	private PropertyListenerMgr createPropertyMgr(Class<?> aC, String aString)
	{
		try
		{
			Method m=aC.getMethod(aString, new Class[]{String.class,PropertyChangeListener.class});
			return new DetailedPropertyListenerMgr(m);
		}
		catch (SecurityException e)
		{
			throw throwError(e);
		}
		catch (NoSuchMethodException e)
		{
			Method m;
			try
			{
				m = aC.getMethod(aString, new Class[]{PropertyChangeListener.class});
				return new BasicPropertyListenerMgr(m);
			}
			catch (SecurityException e1)
			{
				throw throwError(e);
			}
			catch (NoSuchMethodException e1)
			{
				throw throwError(e);
			}
		}
		
	}

	synchronized private  Map<String,Method> fetchGetterMapForClass(Class<?> c)
	{
		Map<String,Method> ret=itsClassGetterMap.get(c);

		if(ret==null)
		{
			ret=new HashMap<String, Method>();
			itsClassGetterMap.put(c,ret);
		}
		return ret;
	}


	private static Method findGetterForClassWithGetterName(Class<?>c,String name)
	{
		try
		{
			return c.getMethod(name, new Class[0]);
		}
		catch (SecurityException e)
		{
			throw throwError(e);
		}
		catch (NoSuchMethodException e)
		{
			return null;
		}
	}

	
	private Method findGetterForClassWithPropertyName(Class<?>c,String aProperty)
	{
		Map<String,Method> map=fetchGetterMapForClass(c);

		synchronized (map)
		{
			Method ret=map.get(aProperty);

			if(ret==null)
			{
				 ret=findGetterForClassWithGetterName(c, makeMethodName("get",aProperty));

				if(ret==null)
					ret=findGetterForClassWithGetterName(c,makeMethodName("is", aProperty));
				
				if(ret==null)
					throw new AssertionError("cannot find getter for property "+aProperty+" in class "+c.getName());
				
				map.put(aProperty, ret);

			}
			return ret;
		}
	}


	private Object findObjectValue(Object anObject,String aProperty)
	{
		Method m=findGetterForClassWithPropertyName(anObject.getClass(), aProperty);
		Object ret=null;
		try
		{
			ret= m.invoke(anObject,(Object[]) null);
		}
		catch (IllegalArgumentException e)
		{
			throw throwError(e);
		}
		catch (IllegalAccessException e)
		{
			throw throwError(e);
		}
		catch (InvocationTargetException e)
		{
			throw throwError(e);
		}
		return ret;
	}


	public Object findObjectKeyPathValue(Object anObject, String aKeyPath)
	{
		String[] theKeys=aKeyPath.split("\\.");
		Object ret=anObject;

		for(String k:theKeys)
		{
			ret = findObjectValue(ret, k);
			if(ret==null)
				return null;
		}
		return ret;
	}

	private void changeObjectValue(Object anObject, String aKeyPath,Object aValue, MethodCache aCache)
	{
		String[] theKeys=aKeyPath.split("\\.");
		int l=theKeys.length;
		Object ret=anObject;
		String lastKey=theKeys[l-1];

		for(int i=0;i<l-1;i++)
		{
			String k=theKeys[i];
			ret = findObjectValue(ret, k);
			if(ret==null)
				throw new AssertionError("incomplete key path for object "+anObject);
		}

		Method m=aCache.findMethodForClassWithPropertyName(ret.getClass(), lastKey);
		
		try
		{
			m.invoke(ret, new Object[]{aValue});
		}
		catch (IllegalArgumentException e)
		{
			throw throwError(e);
		}
		catch (IllegalAccessException e)
		{
			throw throwError(e);
		}
		catch (InvocationTargetException e)
		{
			throw throwError(e);
		}
	}

	public void setObjectValuetoKeyPath(Object anObject, String aKeyPath,Object aValue)
	{
		changeObjectValue(anObject,aKeyPath,aValue,itsSetterMethodCache);
	}

	public void addObjectToKeyPath(Object anObject, String aKeyPath,Object aValue)
	{
		changeObjectValue(anObject,aKeyPath,aValue,itsSetAdderMethodCache);
	}

	public static ReflectionMgr getSingleton()
	{
		return singleton;
	}


}
