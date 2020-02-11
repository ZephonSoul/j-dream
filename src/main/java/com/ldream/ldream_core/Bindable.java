package com.ldream.ldream_core;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

public interface Bindable<T extends Bindable<T>> {

	public T bindActualComponent(
			ReferencedComponentInstance componentReference, 
			ActualComponentInstance actualComponent);
	
}
