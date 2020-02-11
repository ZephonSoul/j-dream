package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.ComponentVariable;
import com.ldream.ldream_core.coordination.Interaction;

public class PortReference implements Formula {
	
	private ComponentVariable componentVariable;
	private String portName;
	
	public PortReference(ComponentVariable componentVariable,String portName) {
		this.componentVariable = componentVariable;
		this.portName = portName;
	}

	@Override
	public boolean sat(Interaction i) {
		//TODO: handle exception
		return false;
	}

	@Override
	public Formula instantiateComponentVariable(ComponentVariable componentVariable, Component actualComponent) {
		if (componentVariable.equals(this.componentVariable)) {
			return new PortAtom(actualComponent.getPortByName(portName));
		} else
			return this;
	}
	
	@Override
	public String toString() {
		return String.format("%s.%s", 
				componentVariable.getName(),
				portName);
	}

}
