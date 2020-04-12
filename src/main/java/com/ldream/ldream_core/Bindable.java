package com.ldream.ldream_core;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;

public interface Bindable<T extends Bindable<T>> {

	public T bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent);
	
}
