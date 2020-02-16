package com.ldream.ldream_core.components;

public class LocalVariable {

	final static int BASE_CODE = 777;
	
	protected String name;
	protected Number value;
	protected Component component;

	public LocalVariable(String name, Number value) {
		this.name = name;
		this.value = value;
	}
	
	public LocalVariable(String name, Number value, Component component) {
		this(name,value);
		this.component = component;
	}

	public LocalVariable(String name) {
		this(name,0);
	}
	
	public LocalVariable(Number value) {
		this("x"+(int)(Math.random()*30000),value);
	}

	/**
	 * @return the value
	 */
	public Number getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Number value) {
		this.value = value;
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
		if (o instanceof LocalVariable)
			return equals((LocalVariable) o);
		else
			return false;
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
		if (component != null)
			name += "[" + component.getId() + "]";
		return name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

}
