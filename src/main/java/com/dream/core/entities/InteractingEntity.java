/**
 * 
 */
package com.dream.core.entities;

import java.util.Map;
import java.util.Set;

import com.dream.core.Entity;

/**
 * @author Alessandro Maggi
 *
 */
public interface InteractingEntity extends Entity {

	public Store getStore();
	
	public void setStore(Store store);
	
	public Map<String,Port> getInterface();
	
	public void setInterface(Map<String,Port> cInterface);
	
	public void setInterface(Set<Port> ports);

	public Port getPortByName(String portName);

	public default void triggerPort(Port port) {};
}
