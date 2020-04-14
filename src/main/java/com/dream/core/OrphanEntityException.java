package com.dream.core;

public class OrphanEntityException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OrphanEntityException(Entity orphanEntity) {
		super(getMessage(orphanEntity));
	}
	
	private static String getMessage(Entity orphanEntity) {
		return String.format("No parent component for %s",
				orphanEntity.toString());
	}

}
