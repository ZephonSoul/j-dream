package com.ldream.ldream_core.components;

import com.ldream.ldream_core.values.Value;

public class LocalVariable {

	final static int BASE_CODE = 777;

	protected String name;
	protected Value value;
	protected Component owner;

	public LocalVariable(String name, Value value) {
		this.name = name;
		this.value = value;
	}

	public LocalVariable(String name, Value value, Component component) {
		this(name,value);
		this.owner = component;
	}

	public LocalVariable(Value value) {
		this("x"+(int)(Math.random()*30000),value);
	}

	/**
	 * @return the value
	 */
	public Value getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Value value) {
		this.value = value;
	}
	
	public Component getOwner() {
		return owner;
	}
	
	public void setOwner(Component newOwner) {
		this.owner = newOwner;
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return BASE_CODE + getInstanceName().hashCode() + value.hashCode();
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(LocalVariable var) {
		//TODO: make more robust comparing name and component owner
		return getInstanceName().equals(var.getInstanceName());
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof LocalVariable)
				&& equals((LocalVariable) o);
	}

	/**
	 * @return
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getInstanceName() + "[=" + value.toString() + "]";
	}

	/**
	 * @return the fully qualified instance name
	 */
	public String getInstanceName() {
		String name = this.name;
		if (owner != null)
			name += "[" + owner.getId() + "]";
		return name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
