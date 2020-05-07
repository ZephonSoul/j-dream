package com.dream.core.coordination;

import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public class UnboundReferenceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnboundReferenceException(Instance<?> reference) {
		super(getMessage(reference));
	}
	
	private static String getMessage(Instance<?> reference) {
		return String.format(
				"Cannot resolve unbound reference %s", 
				reference.toString());
	}
	
}
