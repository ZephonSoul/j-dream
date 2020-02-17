package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.components.Component;

public class ActualComponentInstance implements ComponentInstance {

	public static int BASE_CODE = 555;

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

	@Override
	public String getName() {
		return component.getInstanceName();
	}

	@Override
	public int hashCode() {
		return BASE_CODE + component.hashCode();
	}

	public boolean equals(ActualComponentInstance instance) {
		return component.equals(instance.getComponent());
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof ActualComponentInstance)
				&& equals((ActualComponentInstance) o);
	}

	@Override
	public ComponentInstance bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		return this;
	}
	
	public String toString() {
		return getName();
	}

}
