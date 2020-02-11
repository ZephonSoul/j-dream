package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.Component;

public class ActualComponentInstance implements ComponentInstance {
	
	private Component component;

	public ActualComponentInstance(Component component) {
		this.component = component;
	}

	/**
	 * @return the componentInstance
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * @param component the componentInstance to set
	 */
	public void setActualComponent(Component component) {
		this.component = component;
	}

	public boolean equals(ActualComponentInstance componentInstance) {
		return this.component.equals(componentInstance.getComponent());
	}
	
	@Override
	public String getName() {
		return component.getInstanceName();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ActualComponentInstance)
			return equals((ActualComponentInstance) o);
		else
			return false;
	}

	@Override
	public ComponentInstance bindActualComponent(ReferencedComponentInstance componentReference, ActualComponentInstance actualComponent) {
		return this;
	}

}
