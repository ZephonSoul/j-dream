package com.dream.core;

public class IDFactory {

	private static IDFactory factoryInstance;
	
	private int instanceIdCounter;
	
	public IDFactory() {
		this.instanceIdCounter = 0;
	}
	
	public static IDFactory getInstance() {
		if (factoryInstance==null)
			factoryInstance = new IDFactory();
		return factoryInstance;
	}

	public synchronized int getFreshId() {
		instanceIdCounter++;
		return instanceIdCounter;
	}
	
}
