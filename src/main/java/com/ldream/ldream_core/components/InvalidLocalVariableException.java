package com.ldream.ldream_core.components;

public class InvalidLocalVariableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidLocalVariableException(String varName) {
		super(getMessage(varName));
	}
	
	private static String getMessage(String varName) {
		return String.format("Invalid local variable name %s: no such local variable", 
				varName);
	}
}
