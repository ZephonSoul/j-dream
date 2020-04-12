package com.ldream.ldream_core.components;

public class OwnerPoolLoopException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OwnerPoolLoopException(Component component) {
		super(getMessage(component));
	}
	
	private static String getMessage(Component component) {
		return String.format("Attempt to add %s to its own pool",
				component.getInstanceName());
	}

}
