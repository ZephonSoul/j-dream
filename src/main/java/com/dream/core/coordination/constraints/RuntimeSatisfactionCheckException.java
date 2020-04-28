package com.dream.core.coordination.constraints;

/**
 * @author Alessandro Maggi
 *
 */
public class RuntimeSatisfactionCheckException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeSatisfactionCheckException(Formula formula) {
		super(getMessage(formula));
	}
	
	private static String getMessage(Formula formula) {
		return String.format(
				"Cannot check satisfaction of formula %s", 
				formula.toString());
	}
	
}
