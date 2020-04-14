package com.dream.core.entities;

public class InvalidPortException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidPortException(String portName,InteractingEntity owner) {
		super(getMessage(portName,owner));
	}
	
	private static String getMessage(String portName,InteractingEntity owner) {
		return String.format(
				"Invalid port name %s: no such port for entity %s", 
				portName, 
				owner.toString());
	}
}
