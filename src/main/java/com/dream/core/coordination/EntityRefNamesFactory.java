package com.dream.core.coordination;

public class EntityRefNamesFactory {

	private static EntityRefNamesFactory factoryInstance;
	
	private int variableIdCounter;
	
	public EntityRefNamesFactory() {
		this.variableIdCounter = 0;
	}
	
	public static EntityRefNamesFactory getInstance() {
		if (factoryInstance==null)
			factoryInstance = new EntityRefNamesFactory();
		return factoryInstance;
	}

	public synchronized String getFreshName() {
		variableIdCounter++;
		return String.format("c%d",variableIdCounter);
	}
	
}
