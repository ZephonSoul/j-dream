package com.ldream.ldream_core.coordination.interactions;

import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.ReferencedComponentInstance;

public class PortAtom implements Formula {

	private Port port;
	
	public PortAtom(Port p) {
		this.port = p;
	}

	public boolean sat(Interaction i) {
		return i.contains(this.port);
	}
	
	public String toString() {
		return this.port.toString();
	}

	@Override
	public Formula bindActualComponent(ReferencedComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}
	
}
