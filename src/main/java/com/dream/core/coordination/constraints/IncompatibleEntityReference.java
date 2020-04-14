/**
 * 
 */
package com.dream.core.coordination.constraints;

import com.dream.core.Entity;

/**
 * @author Alessandro Maggi
 *
 */
public class IncompatibleEntityReference extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncompatibleEntityReference(Entity entity,String token) {
		super(getMessage(entity,token));
	}

	public static String getMessage(Entity entity,String token) {
		return String.format("Incompatible entity %s for token %s",
				entity.toString(),
				token);
	}
}
