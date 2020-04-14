/**
 * 
 */
package com.dream.core.coordination;

import com.dream.core.Entity;

/**
 * @author Alessandro Maggi
 *
 */
public class IllegalScopeException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IllegalScopeException(Entity scope) {
		super(getMessage(scope));
	}
	
	public static String getMessage(Entity scope) {
		return String.format(
				"Illegal entity for declaration scoping: %s", 
				scope.toString());
	}

}
