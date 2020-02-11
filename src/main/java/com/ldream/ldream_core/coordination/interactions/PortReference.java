package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

public class PortReference implements Formula {
	
	private ComponentInstance componentInstance;
	private String portName;
	
	public PortReference(ComponentInstance componentInstance,String portName) {
		this.componentInstance = componentInstance;
		this.portName = portName;
	}

	@Override
	public boolean sat(Interaction i) {
		//TODO: handle exception
		return false;
	}

	@Override
	public Formula bindActualComponent(
			ReferencedComponentInstance componentReference, 
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

}
