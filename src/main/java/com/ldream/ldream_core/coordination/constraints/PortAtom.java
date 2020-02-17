package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;

public class PortAtom extends AbstractFormula implements Formula {

	private Port port;

	public PortAtom(Port p) {
		this.port = p;
	}

	/**
	 * @return the port
	 */
	public Port getPort() {
		return port;
	}

	public boolean sat(Interaction i) {
		return i.contains(this.port);
	}

	public String toString() {
		return this.port.toString();
	}

	@Override
	public Formula bindActualComponent(ComponentInstance componentVariable, ActualComponentInstance actualComponent) {
		return this;
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof PortAtom)
				&& port.equals(((PortAtom) formula).getPort());
	}


}
