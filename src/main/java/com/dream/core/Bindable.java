package com.dream.core;

import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;

public interface Bindable<T extends Bindable<T>> {

	public T bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent);
	
}
