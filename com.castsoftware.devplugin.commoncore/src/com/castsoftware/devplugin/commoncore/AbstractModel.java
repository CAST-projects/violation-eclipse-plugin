package com.castsoftware.devplugin.commoncore;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel
{
	private PropertyChangeSupport itsSupport= new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener aListener)
	{
		itsSupport.addPropertyChangeListener(aListener);
	}

	public void addPropertyChangeListener(String aPropertyName,
			PropertyChangeListener aListener)
	{
		itsSupport.addPropertyChangeListener(aPropertyName, aListener);
	}

	protected void fireIndexedPropertyChange(String aPropertyName, int aIndex,
			boolean aOldValue, boolean aNewValue)
	{
		itsSupport.fireIndexedPropertyChange(aPropertyName, aIndex, aOldValue,
				aNewValue);
	}

	protected void fireIndexedPropertyChange(String aPropertyName, int aIndex,
			int aOldValue, int aNewValue)
	{
		itsSupport.fireIndexedPropertyChange(aPropertyName, aIndex, aOldValue,
				aNewValue);
	}

	protected void fireIndexedPropertyChange(String aPropertyName, int aIndex,
			Object aOldValue, Object aNewValue)
	{
		itsSupport.fireIndexedPropertyChange(aPropertyName, aIndex, aOldValue,
				aNewValue);
	}

	protected void firePropertyChange(PropertyChangeEvent aEvt)
	{
		itsSupport.firePropertyChange(aEvt);
	}

	protected void firePropertyChange(String aPropertyName, boolean aOldValue,
			boolean aNewValue)
	{
		itsSupport.firePropertyChange(aPropertyName, aOldValue, aNewValue);
	}

	protected void firePropertyChange(String aPropertyName, int aOldValue,
			int aNewValue)
	{
		itsSupport.firePropertyChange(aPropertyName, aOldValue, aNewValue);
	}

	protected void firePropertyChange(String aPropertyName, Object aOldValue,
			Object aNewValue)
	{
		itsSupport.firePropertyChange(aPropertyName, aOldValue, aNewValue);
	}

	public void removePropertyChangeListener(PropertyChangeListener aListener)
	{
		itsSupport.removePropertyChangeListener(aListener);
	}

	public void removePropertyChangeListener(String aPropertyName,
			PropertyChangeListener aListener)
	{
		itsSupport.removePropertyChangeListener(aPropertyName, aListener);
	}


}
