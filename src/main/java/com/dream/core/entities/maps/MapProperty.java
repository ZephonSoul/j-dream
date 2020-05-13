/**
 * 
 */
package com.dream.core.entities.maps;

/**
 * @author Alessandro Maggi
 *
 */
@FunctionalInterface
public interface MapProperty<T> {
	
	public T get(MotifMap map);

}
