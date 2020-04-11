package com.dream.core.coordination;

import com.dream.core.Bindable;
import com.dream.core.components.Component;

public interface ComponentInstance extends Bindable<ComponentInstance> {
	
	public String getName();
	
	public Component getComponent();
	
}
