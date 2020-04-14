package com.dream.core.entities;

import java.util.Collection;

import com.dream.core.coordination.Interaction;

public class CompoundInteractionsIterator implements InteractionsIterator {
	
	private Port[] cInterface;
	private PoolInteractionsIterator cPoolIter;
	private Interaction lastPoolInteraction;
	private int portIndex;
	
	public CompoundInteractionsIterator() {
		cPoolIter = null;
		portIndex = 0;
		lastPoolInteraction = null;
		cInterface = new Port[0];
	}

	public CompoundInteractionsIterator(Collection<Port> ports) {
		this();
		this.cInterface = ports.toArray(Port[]::new);
	}
	
	public CompoundInteractionsIterator(
			Collection<Port> ports,
			PoolInteractionsIterator cPoolIter) {
		this(ports);
		this.cPoolIter = cPoolIter;
	}

	public void setInterface(Collection<Port> ports) {
		this.cInterface = ports.toArray(Port[]::new);
	}
	
	public void setPoolIterator(PoolInteractionsIterator cPoolIter) {
		this.cPoolIter = cPoolIter;
	}
	
	public Interaction next() {
		Interaction interaction;
		if (lastPoolInteraction == null && cPoolIter != null) {
			lastPoolInteraction = cPoolIter.next();
		}
		if (portIndex > cInterface.length) {
			portIndex = 0;
			if (cPoolIter != null)
				lastPoolInteraction = cPoolIter.next();
		}
		if (portIndex == cInterface.length)
			interaction = new Interaction();
		else
			interaction = new Interaction(cInterface[portIndex]);
		portIndex++;
		if (cPoolIter == null)
			return interaction;
		else
			return Interaction.mergeAll(interaction,lastPoolInteraction);
	}

}
