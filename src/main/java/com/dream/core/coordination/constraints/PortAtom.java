package com.dream.core.coordination.constraints;

import com.dream.core.Instance;
import com.dream.core.coordination.Interaction;
import com.dream.core.entities.Port;

/**
 * @author Alessandro Maggi
 *
 */
public class PortAtom extends AbstractFormula implements Formula, Instance<Port> {
	
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

//	@Override
//	public Formula bindEntityReference(EntityInstanceRef componentVariable, EntityInstanceActual actualComponent) {
//		return this;
//	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof PortAtom)
				&& port.equals(((PortAtom) formula).getPort());
	}

	@Override
	public int hashCode() {
		return BASE_CODE + port.hashCode();
	}

	@Override
	public <I> Formula bindInstance(Instance<I> reference, Instance<I> actual) {
		return this;
	}

	@Override
	public String getName() {
		return toString();
	}

	@Override
	public Port getActual() {
		return port;
	}

}
