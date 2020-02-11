package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.Component;

public class ReferencedComponentInstance implements ComponentInstance {

	private String name;
	
	public ReferencedComponentInstance() {
		this.name = ComponentVariableNamesFactory.getInstance().getFreshName();
	}
	
	public String getName() {
		return name;
	}

	public boolean equals(ReferencedComponentInstance componentInstance) {
		return name.equals(componentInstance.getName());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ReferencedComponentInstance)
			return this.equals((ReferencedComponentInstance)o);
		else
			return false;
	}

	@Override
	public ComponentInstance bindActualComponent(ComponentInstance componentInstance, Component actualComponent) {
		if (this.equals(componentInstance)) {
			return new ActualComponentInstance(actualComponent);
		} else
			return this;
	}
	
	public String toString() {
		return this.name;
	}
	
}
