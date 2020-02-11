package com.ldream.ldream_core.output;

import com.ldream.ldream_core.components.Component;

public class ComponentStateWritable implements Writable {
	
	private Component component;

	public ComponentStateWritable(Component component) {
		this.component = component;
	}
	
	@Override
	public String getString() {
		return component.toString(true,"");
	}

}
