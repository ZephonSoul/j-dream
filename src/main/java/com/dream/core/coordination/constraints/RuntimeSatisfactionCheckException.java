package com.dream.core.coordination.constraints;

public class RuntimeSatisfactionCheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeSatisfactionCheckException(Formula formula) {
		super(getMessage(formula));
	}
	
	private static String getMessage(Formula formula) {
		return String.format(
				"Cannot statically check satisfaction of formula %s", 
				formula.toString());
	}
	
}
