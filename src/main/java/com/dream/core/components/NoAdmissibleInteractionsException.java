package com.dream.core.components;

public class NoAdmissibleInteractionsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoAdmissibleInteractionsException(Component scope) {
		super(getMessage(scope));
	}

	public static String getMessage(Component scope) {
		return String.format(
				"No admissible interactions found for component %s", 
				scope.getInstanceName());
	}
	
}
