/**
 * 
 */
package com.dream.core.coordination.constraints;

import com.dream.core.Instance;

/**
 * @author Alessandro Maggi
 *
 */
public class IncompatibleEntityReference extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IncompatibleEntityReference(Instance<?> instance, String token) {
		super(getMessage(instance,token));
	}

	public static String getMessage(Instance<?> instance, String token) {
		return String.format("Incompatible instance reference %s for element %s",
				instance.toString(),
				token);
	}
}
