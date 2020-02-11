package com.ldream.ldream_core.components;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.shared.Messages;

public class Interface {

	private Component owner;
	private Set<Port> ports;

	public Interface() {
		ports = new HashSet<>();
	}
	
	public Interface(Set<Port> ports) {
		this.ports = ports;
	}
	
	public Interface(Port... ports) {
		this();
		for (Port p : ports) {
			this.ports.add(p);
		}
	}
	
	private void bindOwner() {
		for (Port p : ports) {
			p.setComponent(owner);
		}
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
		throw new InvalidPortException(Messages.invalidPortName(portName, owner.getInstanceName()));
	}

}
