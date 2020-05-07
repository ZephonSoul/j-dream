package com.dream.core.coordination.maps;

import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public class IllegalAddressingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalAddressingException(Instance<?> entity,String comment) {
		super(getMessage(entity,comment));
	}
	
	private static String getMessage(Instance<?> entity,String comment) {
		return String.format(
				"Illegal addressing request for %s: %s", 
				entity.toString(),
				comment);
	}
	
}
