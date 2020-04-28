package com.dream.core.entities;

import java.util.Collection;

import com.dream.core.coordination.Interaction;

/**
 * @author Alessandro Maggi
 *
 */
public class EntityInteractionsIterator implements InteractionsIterator {
	
	private Port[] eInterface;
	private int portIndex;
	
	public EntityInteractionsIterator() {
		portIndex = 0;
		eInterface = new Port[0];
	}

	public EntityInteractionsIterator(Collection<Port> ports) {
		this();
		eInterface = ports.toArray(Port[]::new);
	}

	public void setInterface(Collection<Port> ports) {
		eInterface = ports.toArray(Port[]::new);
	}
	
	public Interaction next() {
		Interaction interaction;
		if (portIndex > eInterface.length) {
			portIndex = 0;
		}
		if (portIndex == eInterface.length)
			interaction = new Interaction();
		else
			interaction = new Interaction(eInterface[portIndex]);
		portIndex++;
		return interaction;
	}

}
