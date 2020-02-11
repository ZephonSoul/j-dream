package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.Component;

public class ActualComponentInstance implements ComponentInstance {
	
	private Component actualComponent;

	public ActualComponentInstance(Component actualComponent) {
		this.actualComponent = actualComponent;
	}

	/**
	 * @return the componentInstance
	 */
	public Component getActualComponent() {
		return actualComponent;
	}

	/**
	 * @param componentInstance the componentInstance to set
	 */
	public void setActualComponent(Component componentInstance) {
		this.actualComponent = componentInstance;
	}

	public boolean equals(ActualComponentInstance componentInstance) {
		return this.actualComponent.equals(componentInstance.getActualComponent());
	}
	
	@Override
	public String getName() {
		return actualComponent.getInstanceName();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof ActualComponentInstance)
			return equals((ActualComponentInstance) o);
		else
			return false;
	}

	@Override
	public ComponentInstance bindActualComponent(ComponentInstance componentInstance, Component actualComponent) {
		return this;
	}

}
