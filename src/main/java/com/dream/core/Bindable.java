package com.dream.core;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;

public interface Bindable<T extends Bindable<T>> {

	public T bindEntityReference(
			EntityInstanceReference entityReference, 
			EntityInstanceActual entityActual);
	
}
