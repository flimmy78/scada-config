package com.ht.scada.config.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class ViewPropertyChange {

	private static final ViewPropertyChange INSTANCE = new ViewPropertyChange();
	private static final Map<String, IPropertyChangeListener> map = new HashMap<String, IPropertyChangeListener>();
	public static ViewPropertyChange getInstance() {
		return INSTANCE;
	}
	
	/**
	 * 触发监听事件
	 * @param property
	 * @param object
	 */
	public void firePropertyChangeListener(String property, Object object) {
		final Iterator iter = map.keySet().iterator();
		while(iter.hasNext()) {
			map.get(iter.next()).propertyChange(new PropertyChangeEvent(this, property, null, object));
		}
		
	}
	
	/**
	 * 注册属性改变监听器
	 * @param listenerId
	 * @param listener
	 */
	public void addPropertyChangeListener(final String listenerId, final IPropertyChangeListener listener) {
		if(!map.containsKey(listenerId)) {
			map.put(listenerId, listener);
		}
	}
	
	/**
	 * 删除属性改变监听器
	 * @param listenerId
	 */
	public void removePropertyChangeListener(final String listenerId) {
		if(map.containsKey(listenerId)) {
			map.remove(listenerId);
		}
	}

}
