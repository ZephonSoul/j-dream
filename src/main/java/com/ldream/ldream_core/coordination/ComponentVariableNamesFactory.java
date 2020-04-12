package com.ldream.ldream_core.coordination;

public class ComponentVariableNamesFactory {

	private static ComponentVariableNamesFactory factoryInstance;
	
	private int variableIdCounter;
	
	public ComponentVariableNamesFactory() {
		this.variableIdCounter = 0;
	}
	
	public static ComponentVariableNamesFactory getInstance() {
		if (factoryInstance==null)
			factoryInstance = new ComponentVariableNamesFactory();
		return factoryInstance;
	}

	public synchronized String getFreshName() {
		variableIdCounter++;
		return String.format("c%d",variableIdCounter);
	}
	
}
