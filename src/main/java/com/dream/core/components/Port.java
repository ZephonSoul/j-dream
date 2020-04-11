package com.dream.core.components;

public class Port {

	final static int BASE_CODE = 9;

	private Component owner;
	private String name;

	public Port(String name) {
		this.name = name;
	}

	public Port(String name,Component component) {
		this(name);
		this.owner = component;
	}

	public Component getComponent() {
		return this.owner;
	}

	public String getName() {
		return this.name;
	}

	public void setOwner(Component newOwner) {
		this.owner = newOwner;
	}

	public boolean equals(Port port) {
		return (this.name.equals(port.getName()) 
				&& this.owner.equals(port.getComponent()));
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Port) && equals((Port) o);
	}

	@Override
	public int hashCode() {
		int idCode = 1;
		if (owner != null) {
			idCode = owner.getId()*BASE_CODE;
		}
		int nameCode = name.hashCode();

		return nameCode*idCode;		
	}

	public String toString() {
		String id = "";
		if (owner != null) {
			id += owner.getId();
		}
		return String.format("%s[%s]", name, id);
	}

	public void trigger() {
		owner.activatePort(this);
	}

}
