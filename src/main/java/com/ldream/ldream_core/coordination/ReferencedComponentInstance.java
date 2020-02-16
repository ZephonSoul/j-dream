package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.Component;

public class ReferencedComponentInstance implements ComponentInstance {

	final static int BASE_CODE = 111;

	private String name;
	
	public ReferencedComponentInstance() {
		this.name = ComponentVariableNamesFactory.getInstance().getFreshName();
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE + name.hashCode();
	}
	
	public boolean equals(ReferencedComponentInstance instance) {
		return name.equals(instance.getName());
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ReferencedComponentInstance)
			return equals((ReferencedComponentInstance) o);
		else
			return false;
	}

	@Override
	public ComponentInstance bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {
		
		if (this.equals(componentReference)) {
			return actualComponent;
		} else
			return this;
	}
	
	public String toString() {
		return this.name;
	}

	@Override
	public Component getComponent() {
		throw new UnboundReferenceException(this);
	}
	
}
