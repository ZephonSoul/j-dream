package com.dream.core.components;

import com.dream.core.coordination.Interaction;

public class ComponentInteractionIterator {
	
	private Port[] cInterface;
	private PoolInteractionIterator cPoolIter;
	private Interaction lastPoolInteraction;
	private int portIndex;
	
	public ComponentInteractionIterator() {
		cPoolIter = null;
		portIndex = 0;
		lastPoolInteraction = null;
		cInterface = new Port[0];
	}

	public ComponentInteractionIterator(Interface cInterface) {
		this();
		this.cInterface = cInterface.getPorts().toArray(Port[]::new);
	}
	
	public ComponentInteractionIterator(Interface cInterface,PoolInteractionIterator cPoolIter) {
		this(cInterface);
		this.cPoolIter = cPoolIter;
	}

	public void setInterface(Interface cInterface) {
		this.cInterface = cInterface.getPorts().toArray(Port[]::new);
	}
	
	public void setPoolIterator(PoolInteractionIterator cPoolIter) {
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
