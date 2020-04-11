package com.dream.core.coordination;

public class UnboundReferenceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnboundReferenceException(ComponentInstance componentReference) {
		super(getMessage(componentReference));
	}
	
	private static String getMessage(ComponentInstance componentReference) {
		return String.format(
				"Cannot resolve unbound reference %s", 
				componentReference.getName());
	}
	
}
