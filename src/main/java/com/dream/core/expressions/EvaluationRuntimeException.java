package com.dream.core.expressions;

/**
 * @author Alessandro Maggi
 *
 */
public class EvaluationRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public <T> EvaluationRuntimeException(Expression ex) {
		super(getMessage(ex));
	}
	
	private static <T> String getMessage(Expression ex) {
		return "Unable to evaluate expression " + ex.toString();
	}
	
}
