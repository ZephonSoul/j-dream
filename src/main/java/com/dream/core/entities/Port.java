package com.dream.core.entities;

/**
 * @author Alessandro Maggi
 *
 */
public class Port {

	final static int BASE_CODE = 9;

	private InteractingEntity owner;
	private String name;

	public Port(String name) {
		this.name = name;
	}

	public Port(String name,InteractingEntity component) {
		this(name);
		this.owner = component;
	}

	public InteractingEntity getEntity() {
		return this.owner;
	}

	public String getName() {
		return this.name;
	}

	public void setOwner(InteractingEntity newOwner) {
		this.owner = newOwner;
	}

	public boolean equals(Port port) {
		return (this.name.equals(port.getName()) 
				&& this.owner.equals(port.getEntity()));
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Port) && equals((Port) o);
	}

	@Override
	public int hashCode() {
		int ownerId = 1;
		if (owner != null) {
			ownerId = owner.getId();
		}
		int nameCode = name.hashCode();

		return BASE_CODE + nameCode + ownerId;
	}
	
	public void trigger() {
		owner.triggerPort(this);
	}

	public String toString() {
		String id = "";
		if (owner != null) {
			id += owner.getId();
		}
		return String.format("%s[%s]", name, id);
	}

}
