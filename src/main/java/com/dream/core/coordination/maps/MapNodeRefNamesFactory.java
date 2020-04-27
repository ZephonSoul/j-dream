package com.dream.core.coordination.maps;

public class MapNodeRefNamesFactory {

	private static MapNodeRefNamesFactory factoryInstance;
	
	private int variableIdCounter;
	
	public MapNodeRefNamesFactory() {
		this.variableIdCounter = 0;
	}
	
	public static MapNodeRefNamesFactory getInstance() {
		if (factoryInstance==null)
			factoryInstance = new MapNodeRefNamesFactory();
		return factoryInstance;
	}

	public synchronized String getFreshName() {
		variableIdCounter++;
		return String.format("n%d",variableIdCounter);
	}
	
}
