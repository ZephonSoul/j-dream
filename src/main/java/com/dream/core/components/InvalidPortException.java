package com.dream.core.components;

public class InvalidPortException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidPortException(String portName,Component owner) {
		super(getMessage(portName,owner));
	}
	
	private static String getMessage(String portName,Component owner) {
		return String.format(
				"Invalid port name %s: no such port for component %s", 
				portName, 
				owner.getInstanceName());
	}
}
