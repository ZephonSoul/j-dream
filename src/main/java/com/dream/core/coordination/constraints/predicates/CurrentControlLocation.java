/**
 * 
 */
package com.dream.core.coordination.constraints.predicates;

import com.dream.core.Bindable;
import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.coordination.constraints.IncompatibleEntityReference;
import com.dream.core.entities.AbstractComponent;

/**
 * @author Alessandro Maggi
 *
 */
//TODO: needs caching if used in operations
public class CurrentControlLocation extends AbstractPredicate {

	private Instance<Entity> componentInstance;
	private String controlLocationName;
	
	public CurrentControlLocation(
			Instance<Entity> componentInstance,
			String controlLocationName) {
		
		this.componentInstance = componentInstance;
		this.controlLocationName = controlLocationName;
	}

	/**
	 * @return the componentInstance
	 */
	public Instance<Entity> getComponentInstance() {
		return componentInstance;
	}

	/**
	 * @return the controlLocationName
	 */
	public String getControlLocationName() {
		return controlLocationName;
	}

	@Override
	public boolean sat() {
		AbstractComponent component = (AbstractComponent) componentInstance.getActual();
		if (component instanceof AbstractComponent) {
			return component.getBehavior().getCurrentControlLocation().hasName(controlLocationName);
		} else
			throw new IncompatibleEntityReference(componentInstance, this.toString());
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof CurrentControlLocation) &&
				((CurrentControlLocation)formula).getComponentInstance().equals(componentInstance) &&
				((CurrentControlLocation)formula).getControlLocationName().equals(controlLocationName);
	}

	@Override
	public <I> Formula bindInstance(Instance<I> reference, Instance<I> actual) {
		return new CurrentControlLocation(
				Bindable.bindInstance(componentInstance, reference, actual),
				controlLocationName);
	}

	@Override
	public void clearCache() {}
	
	public String toString() {
		return String.format("%s.%s", 
				componentInstance.toString(), 
				controlLocationName);
	}

}
