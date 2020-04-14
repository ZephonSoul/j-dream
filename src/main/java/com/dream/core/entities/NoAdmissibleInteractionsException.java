package com.dream.core.entities;

import com.dream.core.Entity;

public class NoAdmissibleInteractionsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public NoAdmissibleInteractionsException(Entity scope) {
		super(getMessage(scope));
	}
	
	public static String getMessage(Entity scope) {
		return String.format(
				"No admissible interactions found for component %s", 
				scope.toString());
	}
	
}
