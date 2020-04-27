package com.dream.core;

public interface Bindable<T> {

	public <I> T bindInstance(
			Instance<I> reference, 
			Instance<I> actual);
	
}
