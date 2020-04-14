package com.dream.core.coordination;

public class UnboundReferenceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnboundReferenceException(EntityInstance componentReference) {
		super(getMessage(componentReference));
	}
	
	private static String getMessage(EntityInstance componentReference) {
		return String.format(
				"Cannot resolve unbound reference %s", 
				componentReference.getName());
	}
	
}
