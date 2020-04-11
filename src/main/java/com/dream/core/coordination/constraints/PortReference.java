package com.dream.core.coordination.constraints;

import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.coordination.Interaction;

public class PortReference extends AbstractFormula implements Formula {
	
	private static final int BASE_CODE = 25;
	
	private ComponentInstance componentInstance;

	private String portName;
	
	public PortReference(ComponentInstance componentInstance,String portName) {
		this.componentInstance = componentInstance;
		this.portName = portName;
	}
	/**
	 * @return the componentInstance
	 */
	public ComponentInstance getComponentInstance() {
		return componentInstance;
	}

	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}

	@Override
	public boolean sat(Interaction i) {
		//TODO: handle exception
		return false;
	}

	@Override
	public Formula bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {
		
		if (componentReference.equals(this.componentInstance)) {
			return new PortAtom(actualComponent.getComponent().getPortByName(portName));
		} else
			return this;
	}
	
	@Override
	public String toString() {
		return String.format("%s.%s", 
				componentInstance.toString(),
				portName);
	}
	
	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof PortReference)
			&& componentInstance.equals(((PortReference) formula).getComponentInstance())
			&& portName.equals(((PortReference) formula).getPortName());
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE + componentInstance.hashCode() + portName.hashCode();
	}

}
