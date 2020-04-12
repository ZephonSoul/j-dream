package com.ldream.ldream_core.components;

public class ComponentsIDFactory {

	private static ComponentsIDFactory factoryInstance;
	
	private int instanceIdCounter;
	
	public ComponentsIDFactory() {
		this.instanceIdCounter = 0;
	}
	
	public static ComponentsIDFactory getInstance() {
		if (factoryInstance==null)
			factoryInstance = new ComponentsIDFactory();
		return factoryInstance;
	}

	public synchronized int getFreshId() {
		instanceIdCounter++;
		return instanceIdCounter;
	}
	
}
