package com.ldream.ldream_core.components;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public class Pool {

	private Set<Component> components;
	private PoolInteractionIterator interactionIterator;

	public Pool() {
		components = new HashSet<>();
	}

	public Pool(Set<Component> components) {
		this.components = components;
		interactionIterator = new PoolInteractionIterator(components);
	}

	public Pool(Component... components) {
		this();
		for (Component component : components)
			this.components.add(component);
		interactionIterator = new PoolInteractionIterator(components);
	}

	/**
	 * @return the components
	 */
	public Set<Component> getComponents() {
		return components;
	}

	/**
	 * @param components the components to set
	 */
	public void setComponents(Set<Component> components) {
		this.components = components;
		interactionIterator = new PoolInteractionIterator(components);
	}

	public void setComponentsParent(Component parent) {
		components.stream().forEach(c -> c.setParent(parent));
	}

	public PoolInteractionIterator getInteractionIterator() {
		return interactionIterator;
	}

	public boolean isEmpty() {
		return components.isEmpty();
	}

	public void add(Component component) {
		components.add(component);
		refresh();
	}

	public void remove(Component component) {
		components.remove(component);
		refresh();
	}

	public String toString() {
		return components.stream().map(Component::getInstanceName).collect(Collectors.joining(","));
	}

	//	public Interaction getAllowedInteraction() {
	//		return Interaction.mergeAll(
	//				components.stream()
	//				.map(Component::getAllowedInteractions)
	//				.toArray(Interaction[]::new)
	//				);
	//	}

	public Interaction getAllowedInteraction() {
		return interactionIterator.next();
	}

	public Set<Interaction> getAllowedInteractions() {
		Set<Interaction> allowedInteractions = new HashSet<>();
		List<Interaction[]> componentsInteractions = 
				components.stream().map(Component::getAllAllowedInteractions).collect(Collectors.toList());
		int[] interactionCounter = new int[components.size()];

		boolean moreCombinations = false;
		while(true) {
			Interaction inter = new Interaction();
			for (int i=0; i<interactionCounter.length; i++) {
				inter.merge(componentsInteractions.get(i)[interactionCounter[i]]);
			}
			allowedInteractions.add(inter);
			for (int i=0; i<interactionCounter.length; i++) {
				if (interactionCounter[i] < componentsInteractions.get(i).length - 1) {
					interactionCounter[i]++;
					moreCombinations = true;
					break;
				} else
					interactionCounter[i] = 0;
			}
			if (moreCombinations)
				moreCombinations = false;
			else
				break;
		}
		return allowedInteractions;
	}

	public OperationsSet getOperationsForInteraction(Interaction interaction) {
		OperationsSet opSet = new OperationsSet();
		for (Component c : components) {
			opSet.addOperationsSet(c.getOperationsForInteraction(interaction));
		}
		return opSet;
	}

	public String toString(boolean exhaustive, String offset) {
		if (!exhaustive)
			return toString();
		else
			return components.stream().<String>map(c -> c.toString(true,offset)).collect(Collectors.joining(",\n"));
	}

	public void refresh() {
		interactionIterator = new PoolInteractionIterator(components.toArray(Component[]::new));
		components.stream().forEach(Component::refresh);
	}

	public int size() {
		return components.size();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Pool) && equals((Pool) o);
	}

	public boolean equals(Pool pool) {
		return components.equals(pool.getComponents());
	}

}
