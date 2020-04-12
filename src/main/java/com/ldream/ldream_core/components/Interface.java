package com.ldream.ldream_core.components;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Interface {

	private Component owner;
	private Set<Port> ports;

	public Interface(Component owner, Set<Port> ports) {
		this.owner = owner;
		this.ports = ports;
		bindOwner();
	}

	public Interface(Component owner, Port... ports) {
		this(owner, Arrays.stream(ports).collect(Collectors.toSet()));
	}

	public Interface(Component owner) {
		this(owner,new HashSet<>());
	}

	public Interface() {
		this.ports = new HashSet<>();
	}

	private void bindOwner() {
		ports.stream().forEach(p -> p.setOwner(owner));
	}

	/**
	 * @return the cInterface
	 */
	public Set<Port> getPorts() {
		return ports;
	}

	/**
	 * @param cInterface the cInterface to set
	 */
	public void setPorts(Set<Port> ports) {
		this.ports = ports;
		bindOwner();
	}

	/**
	 * @return the owner
	 */
	public Component getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Component owner) {
		this.owner = owner;
		bindOwner();
	}

	public String toString() {
		return ports.stream().map(Port::toString).collect(Collectors.joining(","));
	}

	public Port getPortByName(String portName) {
		for (Port p : ports) {
			if (p.getName().equals(portName))
				return p;
		};
		throw new InvalidPortException(portName,owner);
	}

}
