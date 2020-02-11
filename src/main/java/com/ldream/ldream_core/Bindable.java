package com.ldream.ldream_core;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentInstance;

public interface Bindable<T extends Bindable<T>> {

	public T bindActualComponent(ComponentInstance componentInstance, Component actualComponent);
	
}
