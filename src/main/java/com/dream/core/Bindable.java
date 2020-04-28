package com.dream.core;

/**
 * @author Alessandro Maggi
 *
 */
public interface Bindable<T> {

	public <I> T bindInstance(
			Instance<I> reference, 
			Instance<I> actual);
	
}
