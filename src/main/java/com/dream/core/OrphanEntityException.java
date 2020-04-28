package com.dream.core;

/**
 * @author Alessandro Maggi
 *
 */
public class OrphanEntityException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OrphanEntityException(Entity orphanEntity) {
		super(getMessage(orphanEntity));
	}
	
	private static String getMessage(Entity orphanEntity) {
		return String.format("No parent component for %s",
				orphanEntity.toString());
	}

}
