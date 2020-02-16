package com.ldream.ldream_core.coordination;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ldream.ldream_core.components.Port;

public class Interaction {

	private Set<Port> ports;

	public Interaction() {
		this.ports = new HashSet<Port>();
	}

	public Interaction(Port... ports) {
		this();
		for (Port port : ports) {
			this.ports.add(port);
		}
	}

	public Interaction(Set<Port> ports) {
		this();
		this.ports.addAll(ports);
	}

	public boolean contains(Port p) {
		return this.ports.contains(p);
	}

	//	public Interaction merge(Interaction interaction) {
	//		return new Interaction(
	//				Stream.concat(
	//						ports.stream(),
	//						interaction.getPorts().stream()
	//						)
	//				.collect(Collectors.toSet()));
	//	}

	public void merge(Interaction interaction) {
		ports.addAll(interaction.getPorts());
	}

	public Set<Port> getPorts() {
		return ports;
	}

	public boolean isEmpty() {
		return ports.isEmpty();
	}

	public String toString() {
		return "{" + ports.stream().map(Port::toString).collect(Collectors.joining(",")) + "}";
	}

	public static Interaction mergeAll(Interaction... interactions) {
		return new Interaction(
				Stream.of(interactions)
				.flatMap(x -> x.getPorts().stream())
				.collect(Collectors.toSet())
				);
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Interaction) {
			return equals((Interaction) o);
		} else {
			return false;
		}
	}

	public boolean equals(Interaction interaction) {
		if (interaction == null) {
			return false;
		}
		return ports.size()==interaction.getPorts().size() 
				&& ports.containsAll(interaction.getPorts());
	}

	@Override
	public int hashCode() {
		return ports.stream().mapToInt(Port::hashCode).sum();
	}

	public int size() {
		return ports.size();
	}

	public void trigger() {
		ports.stream().forEach(Port::trigger);
		
	}

}
