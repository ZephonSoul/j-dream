package com.dream.core.coordination;

public class EntityVariableNamesFactory {

	private static EntityVariableNamesFactory factoryInstance;
	
	private int variableIdCounter;
	
	public EntityVariableNamesFactory() {
		this.variableIdCounter = 0;
	}
	
	public static EntityVariableNamesFactory getInstance() {
		if (factoryInstance==null)
			factoryInstance = new EntityVariableNamesFactory();
		return factoryInstance;
	}

	public synchronized String getFreshName() {
		variableIdCounter++;
		return String.format("c%d",variableIdCounter);
	}
	
}
