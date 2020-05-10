/**
 * 
 */
package com.dream.core.entities.behavior;

/**
 * @author Alessandro Maggi
 *
 */
public class ControlLocation {

	private String name;
	
	public ControlLocation(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean hasName(String name) {
		return this.name.equals(name);
	}
	
	public String toString() {
		return name;
	}
	
}