package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.Bindable;
import com.ldream.ldream_core.components.Component;

public interface ComponentInstance extends Bindable<ComponentInstance> {
	
	public String getName();
	
	public Component getComponent();
	
}
