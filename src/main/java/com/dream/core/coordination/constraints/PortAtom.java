package com.dream.core.coordination.constraints;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.Interaction;
import com.dream.core.entities.Port;

public class PortAtom extends AbstractFormula implements Formula {
	
	private static final int BASE_CODE = 313;

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

	@Override
	public boolean sat(Interaction i) {
		return i.contains(this.port);
	}

	public String toString() {
		return this.port.toString();
	}

	@Override
	public Formula bindEntityReference(EntityInstanceReference componentVariable, EntityInstanceActual actualComponent) {
		return this;
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof PortAtom)
				&& port.equals(((PortAtom) formula).getPort());
	}

	@Override
	public int hashCode() {
		return BASE_CODE + port.hashCode();
	}

}
