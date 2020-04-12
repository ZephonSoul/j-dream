package com.ldream.ldream_core.components;

import java.util.HashSet;
import java.util.Set;

import com.ldream.ldream_core.coordination.Interaction;

public class InteractionsIterator {

	private Port[][] portSets; 
	private int[] portCounters;
	private Set<Interaction> cachedInteractions;

	public InteractionsIterator(Interface... interfaces) {
		portSets = new Port[interfaces.length][];
		for (int i=0; i<portSets.length; i++) {
			portSets[i] = interfaces[i].getPorts().toArray(Port[]::new);
		}
		portCounters = new int[portSets.length];
		cachedInteractions = null;
	}

	public boolean hasNext() {
		for (int i=0; i<portCounters.length; i++) {
			if (portCounters[i] >= portSets[i].length)
				return false;
		}
		return true;
	}
	
	private Interaction getInteraction() {
		Port[] port_set = new Port[portCounters.length];
		for (int i=0; i<port_set.length; i++) {
			if (portCounters[i] >= portSets[i].length) {
				return new Interaction();
			}
			port_set[i] = portSets[i][portCounters[i]];
		}
		return new Interaction(port_set);
	}

	public Interaction last() {
		return getInteraction();
	}

	private void nextIndex() {
		for (int i=0; i<portCounters.length; i++) {
			if ((portCounters[i] < portSets[i].length - 1) || (i == portCounters.length-1)) {
				portCounters[i]++;
				break;
			} else
				portCounters[i] = 0;
		}
	}
	
	public void resetIndex() {
		portCounters = new int[portSets.length];
	}

	public Interaction next() {
		Interaction next = getInteraction();
		nextIndex();
		return next;
	}
	
	public Set<Interaction> getAll() {
		if (cachedInteractions == null) {
			cachedInteractions = new HashSet<>();
			while (hasNext())
				cachedInteractions.add(next());
		}
		return cachedInteractions;
	}

}
