package com.ldream.ldream_core.components;

public class OrphanComponentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrphanComponentException(Component orphanComponent) {
		super(getMessage(orphanComponent));
	}
	
	private static String getMessage(Component orphanComponent) {
		return String.format("No parent component for %s",
				orphanComponent.getInstanceName());
	}

}
