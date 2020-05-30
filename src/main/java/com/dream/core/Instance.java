/**
 * 
 */
package com.dream.core;

/**
 * @author Alessandro Maggi
 *
 */
public interface Instance<T> {
	
	public T getActual();
	
	public void evaluate();
	
}
