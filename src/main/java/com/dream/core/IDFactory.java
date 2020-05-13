package com.dream.core;

/**
 * @author Alessandro Maggi
 *
 */
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
	
	public void resetFactory() {
		this.instanceIdCounter = 0;
	}
	
}
