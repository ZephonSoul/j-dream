package com.ldream.ldream_core.components;

public class Port {

	final static int BASE_CODE = 9;

	private Component component;
	private String name;

	public Port(String name) {
		this.name = name;
	}

	public Port(String name,Component component) {
		this(name);
		this.component = component;
	}

	public Component getComponent() {
		return this.component;
	}

	public String getName() {
		return this.name;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public boolean equals(Port port) {
		return (this.name.equals(port.getName()) 
				&& this.component.equals(port.getComponent()));
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Port) && equals((Port) o);
	}

	@Override
	public int hashCode() {
		int idCode = 1;
		if (component != null) {
			idCode = component.getId()*BASE_CODE;
		}
		int nameCode = name.hashCode();

		return nameCode*idCode;		
	}

	public String toString() {
		String id = "";
		if (component != null) {
			id += component.getId();
		}
		return String.format("%s[%s]", name, id);
	}

	public void trigger() {
		component.activatePort(this);
	}

}
